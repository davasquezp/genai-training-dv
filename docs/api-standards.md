# API standards

## Base path
- APIs live under `/api/*`.
- Keep paths stable when refactoring packages.

## Resources and conventions
- Use plural nouns: `/api/members`, `/api/dancers`
- Prefer simple JSON DTOs in REST adapters.
- Controllers should be thin: validate + map + call a use-case.

## Validation
- Use Jakarta Validation annotations on request DTOs.
- Reject invalid payloads with a clear 4xx response.

## Enums over the wire
When a concept is constrained and reused, prefer enums in the domain.

Example: `DanceStyle`
- Request accepts case-insensitive strings (e.g. `"Salsa"`, `"salsa"`).
- Response returns **lowercase strings** (e.g. `"salsa"`).

## Error responses
Keep error responses consistent across endpoints.

Current baseline:
- 400 for validation / malformed requests
- 404 for not found

If we standardize an error payload, use one shape everywhere (future ADR).

## OpenAPI / Swagger
- Swagger UI: `/swagger-ui/index.html`
- OpenAPI JSON: `/v3/api-docs`

