# Development workflow

## Prereqs
- **Java**: 21+ (project targets Java 21; local machine may run newer)
- **Maven**: 3.9+
- **Node**: 20+ (frontend uses Vite 8)
- **Docker Desktop**: required for Testcontainers tests

## Local development (backend)

### Run

```bash
mvn -Dmaven.repo.local=.m2 spring-boot:run -Dspring-boot.run.mainClass=com.dv.genaitraining.GenAiTrainingApplication
```

Open:
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Test

```bash
mvn -Dmaven.repo.local=.m2 test
```

Notes:
- Testcontainers requires Docker Desktop to be running.

## Local development (frontend)

### Install & run

```bash
cd frontend
npm install
npm run dev -- --host 127.0.0.1 --port 5173
```

Open:
- UI: `http://127.0.0.1:5173`

### Local API integration
During local development, Vite proxies `/api/*` to the backend:
- config: `frontend/vite.config.ts`
- proxy target: `http://localhost:8080`

## Branching
- `main` is always deployable.
- Branch naming: `feature/<short-name>` or `fix/<short-name>`

## Commits
- Prefer **imperative** subject lines (e.g. “add dancer registration slice”).
- Keep commits focused; avoid mixing unrelated changes.

## Pull requests (checklist)
- Builds locally: `mvn -Dmaven.repo.local=.m2 test`
- If changing REST APIs: update/add controller/service tests and verify Swagger loads
- If changing frontend API calls: verify Interest page submit works end-to-end
- If changing deploy/env behavior: update `docs/deployment.md`

