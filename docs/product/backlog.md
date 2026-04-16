# Backlog (lean)

Ordered roughly by “value / effort” for early-stage learning.

## 1) Durable persistence for dancers
- Replace in-memory repository with database-backed persistence (H2 → Postgres on Render).
- Acceptance:
  - submissions survive backend restart
  - `GET /api/dancers` returns stored data

## 2) Simple admin view in the UI (done)
- Dancers listing page (`/dancers`) connected to live `GET /api/dancers`
- Searchable by name, country, role, and styles
- Clickable cards lead to individual dancer profile pages

## 3) Data hygiene
- Prevent duplicates (same name + country + role + styles within a short window) or add “confirm submission” UX.

## 4) Consent + privacy text
- Add a short consent statement and a way to delete a submission (MVP-friendly).

## 5) Analytics (minimal)
- Track: page view, submit attempt, submit success/failure.

## 6) Country normalization
- Store ISO country code consistently; ensure UI and API align.

## 7) API hardening
- Standardize error payload shape across endpoints (ADR + implementation).

## 8) Deployment hardening
- Add smoke checks and clear logs for CORS/env var misconfiguration.

## 9) Monetization foundations
- Define community tiers (features + limits) and an upgrade path.
- Add commission-based ticketing integration for events (festivals/workshops) once community core is validated.

