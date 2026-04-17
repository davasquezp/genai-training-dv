# ADR 0001: Records + vertical slices

- Status: accepted
- Date: 2026-04-16

## Context
We want to keep a clean, scalable structure while shipping quickly:
- backend in Spring Boot 3
- Java records preferred (no Lombok)
- features should be easy to add/remove
- deployments to Render (backend) and Cloudflare Pages (frontend)

## Decision
- Use **vertical slices** under `src/main/java/com/dv/genaitraining/features/<feature>/`.
- Keep **cross-cutting** code in `src/main/java/com/dv/genaitraining/shared/`.
- Prefer **records** for domain + DTOs where appropriate.
- Use **ports & adapters** concepts, but package **feature-first**.
- Keep REST endpoints stable under `/api/*` (e.g. `/api/members`, `/api/dancers`).
- Start new features with **in-memory persistence** when appropriate, then replace with DB-backed adapters without breaking ports/controllers.

## Consequences
- Easier navigation: everything for a feature is in one place.
- Easier refactors: features are modular and removable.
- Persistence can evolve without rewriting business logic.
- Some duplication may exist across slices; only promote code to `shared/` when truly cross-cutting.

## Alternatives considered
- Layered architecture packages (`controller/service/repository` across the whole app)
- Pure hexagonal with global `ports` and `adapters` packages (not feature-first)

