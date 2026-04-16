# Contributing

Thanks for contributing to LatinVibe.

## Start here
- Docs index (source of truth): [`docs/index.md`](docs/index.md)
- Architecture: [`docs/architecture.md`](docs/architecture.md)
- Development workflow: [`docs/development-workflow.md`](docs/development-workflow.md)
- API standards: [`docs/api-standards.md`](docs/api-standards.md)
- Deployment: [`docs/deployment.md`](docs/deployment.md)

## Expectations
- Keep changes small and focused.
- Follow the vertical-slice structure under `src/main/java/com/dv/genaitraining/features/<feature>/`.
- If you change an API contract or deployment behavior, update the docs in `docs/`.

## Before you open a PR
- Backend tests pass: `mvn -Dmaven.repo.local=.m2 test`
- Frontend still builds and runs.
- New endpoints are visible in Swagger.

