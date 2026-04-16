# genai-training-dv

Java 21 Maven starter using **Hexagonal Architecture (Ports & Adapters)** with:

- **JUnit 5**
- **Mockito**
- **AssertJ**
- **Testcontainers** (PostgreSQL smoke test)

## Project layout

```
src/main/java/com/dv/genaitraining/
  features/                  # vertical slices (user, customer, ...)
  shared/                    # cross-cutting config
frontend/                    # Vue 3 + Vite web UI (dev server)
```

## Docs (source of truth)

Start here: [`docs/index.md`](docs/index.md)

## Build & test

```bash
mvn -Dmaven.repo.local=.m2 test
```

Notes:
- The Testcontainers test requires Docker to be running.
- Change `groupId` / base package from `com.example` as needed.

## Run the REST API

```bash
mvn -Dmaven.repo.local=.m2 spring-boot:run -Dspring-boot.run.mainClass=com.dv.genaitraining.GenAiTrainingApplication
```

## Run the Web UI (Vue)

Requirements:
- Node 20+ (project uses Vite 8)

```bash
cd frontend
npm install
npm run dev
```

Open:
- UI: `http://localhost:5173`
- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

## Deploy backend to Render (Docker)

This repo includes a root `Dockerfile` for the Spring Boot backend. Render will build and run it as a Docker Web Service.

- **Render**: New → Web Service → pick repo → choose **Docker**
- Render sets `PORT`; the container starts Spring Boot with `-Dserver.port=$PORT`.

### User CRUD endpoints

- **POST** `/api/users`
- **GET** `/api/users`
- **GET** `/api/users/{id}`
- **PUT** `/api/users/{id}`
- **DELETE** `/api/users/{id}`
