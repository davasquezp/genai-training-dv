# ADR 0002: CQRS-lite BFF with page-shaped query API

- Status: proposed
- Date: 2026-04-18

## Context

The frontend pages in [frontend/src/pages](../../frontend/src/pages) are forced to assemble their views from several feature endpoints. Concrete pain points today:

- `CommunitiesPage.vue` issues one `GET /api/communities` plus one `GET /api/community-memberships?communityId=...` per row to display a dancer count (N+1 over the network).
- `CommunityPage.vue` makes three round-trips (`/api/communities/{id}`, `/api/community-memberships?...`, `/api/dancers/me`) to render a single page.
- `DancerPage.vue` loads the entire dancer roster only to render one profile.
- `MemberProfilePage.vue` calls `/api/auth/me` and `/api/dancers/me` sequentially.

The current backend mixes read and write endpoints inside each vertical slice (e.g. `CommunityController` exposes both `POST /api/communities` and `GET /api/communities`). Read-side DTOs are slice-shaped, not page-shaped, so the frontend pays for either over-fetching or extra round-trips.

Constraints:
- Stay on the existing Spring Boot 3 + Vue stack; do not introduce a new transport layer.
- Most slices still use in-memory repositories; only `member` is JPA-backed today (see ADR 0001 and [src/main/resources/application.yml](../../src/main/resources/application.yml)).
- Keep the vertical-slice convention from ADR 0001.

## Decision

Adopt a CQRS-lite split co-located with a Backend-for-Frontend (BFF) read API:

1. **Write side keeps the existing vertical slices** under `com.dv.genaitraining.features.*`. Each slice's controller becomes write-only and is renamed to `*CommandController`. Read-only use cases inside slices are removed once their callers move to the query side. No new `commands/` wrapper package is introduced.

2. **Read side lives in a new top-level `queries/` package**, organized by frontend page rather than by domain slice:

   ```
   queries/
   ├── api/PageQueryExceptionHandler.java
   ├── pages/
   │   ├── memberprofile/    -> GET /api/page/me
   │   ├── communities/      -> GET /api/page/communities
   │   ├── community/        -> GET /api/page/communities/{id}
   │   ├── dancers/          -> GET /api/page/dancers
   │   └── dancer/           -> GET /api/page/dancers/{id}
   └── infrastructure/jooq/  (future jOOQ-backed implementations)
   ```

   Each page module contains a controller, a `*PageView` response DTO, a `*PageQuery` interface, and an `InMemory*PageQuery` implementation that aggregates data through the existing slice repositories.

3. **URL conventions**:
   - Commands: `/api/{slice}` (POST/PUT/DELETE only).
   - Queries: `/api/page/{pageName}[/{id}]` (GET only).

4. **Read-side technology**: REST page endpoints + jOOQ as the target for hand-tuned SQL. Until the data layer migrates to a real database, query implementations aggregate in Java behind the `*PageQuery` interface; later, jOOQ implementations slot in under `queries/infrastructure/jooq/` without touching controllers or DTOs. GraphQL was rejected to avoid adding a new transport, schema language, and client tooling.

5. **Schema-drift tests**: every page query has a `MockMvc` snapshot test that compares the response to a checked-in golden JSON file under `src/test/resources/queries/`. Variants per page (anonymous, member, dancer, member-of-this-community, ...) get their own golden files. `org.skyscreamer:jsonassert` is added to [pom.xml](../../pom.xml). A `-Dsnapshot.update=true` flag rewrites the golden files. These tests are the contract that survives the future jOOQ swap.

6. **Endpoint mapping** (old to new):
   - `GET /api/auth/me` -> `GET /api/page/me`
   - `GET /api/dancers/me` -> folded into `GET /api/page/me` and the `viewer` block of `GET /api/page/communities/{id}`
   - `GET /api/dancers` -> `GET /api/page/dancers`
   - `GET /api/dancers/{id}` (new) -> `GET /api/page/dancers/{id}` (replaces "load full list and find by id" in `DancerPage.vue`)
   - `GET /api/communities` -> `GET /api/page/communities` (includes `dancerCount` per row)
   - `GET /api/communities/{id}` -> `GET /api/page/communities/{id}` (includes `dancerCount`, `viewer.alreadyMember`, `viewer.myDancerId`)
   - `GET /api/community-memberships?...` -> removed; data is folded into the page queries above.

7. **Frontend mirror**: a new `frontend/src/features/pages/{memberProfile,communitiesList,communityDetail,dancersList,dancerDetail}/api.ts` houses one client per page query. Existing read helpers (`getCommunity`, `listCommunities`, `listMembershipsByCommunity`, `getMyDancer`, `fetchMe`) are deleted. `restoreSession()` in [frontend/src/features/member/auth.ts](../../frontend/src/features/member/auth.ts) targets `/api/page/me`.

## Consequences

What gets easier:
- Each Vue page makes a single GET that returns exactly what it renders; N+1 on the communities list and the three round-trips on community detail go away.
- Read DTOs evolve independently of write commands and aggregates.
- Schema regressions surface immediately in CI through golden-file diffs.
- Migrating storage (in-memory -> Postgres + jOOQ) becomes mechanical: same `*PageQuery` interface, new implementation under `queries/infrastructure/jooq/`, snapshot tests prove no drift.

What gets harder:
- Adding a field consumed by a page now means touching the page query, its DTO, and the snapshot golden files (intentional friction).
- The `queries/` tree introduces a second navigation axis alongside the slice tree; new contributors must know the rule "writes by slice, reads by page".
- Personalized read models on otherwise public pages (e.g. the `viewer` block on community detail) require multiple golden files per page.

Trade-offs:
- Page-shaped REST DTOs are less expressive than GraphQL but trivially testable, reviewable in PRs, and require no new client/runtime.
- jOOQ adds a build-time codegen step once the database lands, but it is the lever that delivers the "minimize over-fetching" goal in real SQL.
- Until the database migration happens, the in-memory query implementations duplicate some aggregation logic that future jOOQ versions will express in SQL; this is accepted to keep the architectural shape stable.

## Alternatives considered

- **Spring for GraphQL** (single `/graphql` endpoint, client-driven shape, DataLoader batching). Rejected to avoid a new transport, schema language, and client. Re-evaluate if the number of page queries explodes or if mobile clients with very different shapes appear.
- **REST page endpoints + Spring Data JPA interface projections / DTO constructor queries** (no jOOQ). Lowest new tech but limited SQL flexibility for cross-aggregate joins and aggregates needed by the page views; rejected as the long-term read engine while keeping JPA on the write side is fine.
- **Keep the current per-slice REST API and merge data on the frontend**. Rejected because it perpetuates over-fetching and N+1 and pushes domain composition into the client.
- **Introduce a `commands/` wrapper package mirroring `queries/`**. Rejected to keep the slice layout from ADR 0001 untouched; the renamed `*CommandController` plus removal of GET methods is enough to signal write-only intent.
- **Read-model store populated by the existing event bus** (CQRS with eventual consistency). Rejected for now; the synchronous in-process events in `shared/events/` do not justify a separate read store yet. Revisit if a true projection store becomes necessary.

