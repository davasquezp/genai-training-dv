# genai-training-dv

Java 21 Maven starter using **Hexagonal Architecture (Ports & Adapters)** with:

- **JUnit 5**
- **Mockito**
- **AssertJ**
- **Testcontainers** (PostgreSQL smoke test)

## Project layout

```
src/main/java/com/dv/genaitraining/
  domain/                    # domain model (no framework dependencies)
  application/
    port/in/                 # inbound ports (use-cases)
    port/out/                # outbound ports (driven)
    service/                 # use-case implementations
  adapters/
    in/                      # inbound adapters (CLI/REST/etc)
    out/                     # outbound adapters (persistence/http/etc)
frontend/                    # Vue 3 + Vite web UI (dev server)
```

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

### User CRUD endpoints

- **POST** `/api/users`
- **GET** `/api/users`
- **GET** `/api/users/{id}`
- **PUT** `/api/users/{id}`
- **DELETE** `/api/users/{id}`
