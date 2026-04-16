# Deployment

## Backend: Render (Docker Web Service)

### Service settings
- **Type**: Web Service
- **Environment**: Docker
- **Root directory**: repo root (where `Dockerfile` is)

Render provides `PORT`. The container starts Spring Boot with:
- `java -Dserver.port=$PORT -jar ...`

### Required environment variables

#### CORS (Cloudflare Pages → Render)
Set this on Render:
- `APP_CORS_ALLOWED_ORIGIN` = `https://<your-project>.pages.dev`

Rules:
- must include `https://`
- must be the **origin only** (no path, no `/api`)
- no trailing slash

## Frontend: Cloudflare Pages

### Build settings
- **Build command**: `cd frontend && npm ci && npm run build`
- **Build output directory**: `frontend/dist`

### Required environment variables
Set this on Cloudflare Pages:
- `VITE_API_BASE_URL` = `https://<your-render-service>.onrender.com`

Rules:
- no trailing slash
- do not include `/api` (frontend appends `/api/dancers`)

### SPA routing
If you use Vue Router history mode, configure a redirect so deep links work.

Add a file at `frontend/public/_redirects`:

```text
/* /index.html 200
```

(Only needed once routing breaks on refresh in production.)

## Troubleshooting

### 403 Forbidden when calling backend from Cloudflare Pages
Most commonly: CORS origin mismatch.

1) In browser devtools Network tab, check which request fails:
- if `OPTIONS /api/...` fails → preflight blocked by CORS config

2) Confirm Render env var `APP_CORS_ALLOWED_ORIGIN` exactly matches the browser Origin header:
- expected Origin looks like `https://<something>.pages.dev`
- preview deployments can have a different origin than production

3) Redeploy Render after changing env vars.

### Frontend calls the wrong backend
Confirm Cloudflare Pages has `VITE_API_BASE_URL` set to your Render URL.

### Swagger / health check
- Swagger UI: `https://<render-host>/swagger-ui/index.html`
- OpenAPI JSON: `https://<render-host>/v3/api-docs`

