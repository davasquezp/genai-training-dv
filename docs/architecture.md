# Architecture

## Goals
- Keep the codebase easy to navigate as features grow.
- Make features removable by grouping **all feature artifacts together**.
- Preserve stable external contracts (e.g. REST paths like `/api/users`).

## High-level structure

Backend code lives under:
- `src/main/java/com/dv/genaitraining/features/` (feature vertical slices)
- `src/main/java/com/dv/genaitraining/shared/` (cross-cutting concerns)

Frontend code lives under:
- `frontend/` (Vue 3 + Vite)

## Vertical slices (feature-first)

Each feature is a top-level folder under `com.dv.genaitraining.features.<feature>`.

Examples in this repo:
- `com.dv.genaitraining.features.user`
- `com.dv.genaitraining.features.customer`
- `com.dv.genaitraining.features.dancer`

### What belongs inside a slice
Put **everything** that is specific to the feature inside the feature package:
- **Domain**: records/enums/value objects (e.g. `Dancer`, `DancerId`, `Role`, `DanceStyle`)
- **Inbound ports (use-cases)**: interfaces consumed by adapters (e.g. `RegisterDancerUseCase`)
- **Application service**: implements inbound ports and orchestrates logic (e.g. `DancerService`)
- **Outbound ports**: interfaces the service needs (e.g. `DancerRepository`)
- **Adapters**:
  - REST controllers + request/response DTOs
  - persistence adapters (JPA, in-memory) implementing repositories/ports
- **Feature tests**: unit tests in `src/test/java/com/dv/genaitraining/features/<feature>/...`

### Shared code
Only place code in `shared/` if it is truly cross-cutting and used by multiple features.

Current examples:
- `com.dv.genaitraining.shared.CorsConfig` (CORS for `/api/**`)
- `com.dv.genaitraining.shared.TimeConfig` (if present; shared `Clock` bean)

## Hexagonal (ports & adapters) within a slice

Each slice follows ports & adapters concepts, but **packaged feature-first**:

```text
features/<feature>/
  (domain types)
  (inbound ports)        e.g. RegisterDancerUseCase
  (application service)  e.g. DancerService
  (outbound ports)       e.g. DancerRepository
  (adapters)             REST controller, persistence implementations
```

Guidelines:
- Domain types should not depend on Spring.
- Inbound ports should be simple interfaces (methods + Javadoc).
- Adapters depend on Spring; they convert I/O shapes (JSON ↔ domain).

## REST endpoint stability
- REST paths are part of the external contract. Keep them stable when refactoring packages.
- Controllers should be thin: validate + map + call a use-case.

## Persistence strategy
We can start with an in-memory adapter to ship quickly (e.g. dancer interest registrations) and later replace it with a database adapter **without changing**:
- inbound port signatures
- controller paths/DTOs (unless intentionally versioned)

When switching persistence:
- introduce a new adapter (e.g. JPA) implementing the same repository/port
- switch wiring (Spring bean selection)
- keep domain + service logic unchanged

## Event-driven bounded contexts (EDA)

This codebase uses an **EventBus** abstraction to connect bounded contexts (vertical slices) in a loosely-coupled way.

- **Contracts** live in `com.dv.genaitraining.shared.events.*`
- **Transport now**: in-memory publish/subscribe (`InMemoryEventBus`)
- **Publish timing**: **after commit** when a Spring transaction is active (`AfterCommitEventBus`)

### Member -> Dancer activation flow

When a logged-in member activates the Dancer role, the Member bounded context publishes an event after commit.

```text
Member API (add role DANCER)
  -> persist updated member
  -> publish MemberRoleAdded (after_commit)
  -> Dancer bounded context handler creates Dancer linked to MemberId
  -> publish DancerCreated
```

### Authorization: “me” endpoints

For sensitive, user-scoped data, prefer **principal-derived identity** over path parameters:

- `POST /api/members/me/roles` modifies roles for the currently authenticated member only.
- `GET /api/dancers/me` returns the dancer profile for the currently authenticated member (404 if none).
- `PUT /api/dancers/me` updates the dancer profile for the currently authenticated member only.

This avoids accidental exposure where a client could request or update another member’s data by providing a different id.

