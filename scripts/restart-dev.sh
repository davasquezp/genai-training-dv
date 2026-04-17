#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

BACKEND_PORT="${BACKEND_PORT:-8080}"
FRONTEND_PORT="${FRONTEND_PORT:-5173}"

FRONTEND_HOST="${FRONTEND_HOST:-127.0.0.1}"
FRONTEND_ORIGIN="${FRONTEND_ORIGIN:-http://${FRONTEND_HOST}:${FRONTEND_PORT}}"

# For local dev only. Override via env if needed.
APP_JWT_SECRET="${APP_JWT_SECRET:-dev-secret-change-me}"

kill_port() {
  local port="$1"
  local pids
  pids="$(lsof -ti:"${port}" 2>/dev/null || true)"
  if [[ -n "${pids}" ]]; then
    echo "Killing processes on port ${port}: ${pids}"
    # No confirmation, force kill
    kill -9 ${pids} 2>/dev/null || true
  else
    echo "No process found on port ${port}"
  fi
}

echo "==> Stopping anything on ${BACKEND_PORT} and ${FRONTEND_PORT}"
kill_port "${BACKEND_PORT}"
kill_port "${FRONTEND_PORT}"

echo "==> Starting backend on ${BACKEND_PORT}"
(
  cd "${ROOT_DIR}"
  APP_CORS_ALLOWED_ORIGIN="${FRONTEND_ORIGIN}" \
  APP_JWT_SECRET="${APP_JWT_SECRET}" \
  mvn -Dmaven.repo.local=.m2 spring-boot:run \
    -Dspring-boot.run.mainClass=com.dv.genaitraining.GenAiTrainingApplication \
    -Dspring-boot.run.arguments="--server.port=${BACKEND_PORT}"
) &
BACKEND_PID=$!

echo "==> Starting frontend on ${FRONTEND_PORT} (strict)"
(
  cd "${ROOT_DIR}/frontend"
  # Ensure a modern Node version (Vite 8 requires Node 20+).
  if [[ -z "${SKIP_NVM:-}" ]]; then
    export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
    if [[ -s "${NVM_DIR}/nvm.sh" ]]; then
      # shellcheck disable=SC1090
      source "${NVM_DIR}/nvm.sh"
      nvm use 20 >/dev/null 2>&1 || true
    fi
  fi
  npm run dev -- --host "${FRONTEND_HOST}" --port "${FRONTEND_PORT}" --strictPort
) &
FRONTEND_PID=$!

echo
echo "Backend PID:  ${BACKEND_PID}"
echo "Frontend PID: ${FRONTEND_PID}"
echo "Backend URL:  http://localhost:${BACKEND_PORT}"
echo "Frontend URL: ${FRONTEND_ORIGIN}"
echo
echo "Tip: Ctrl+C will stop this script, but background processes keep running."
echo "     Re-run this script to force-kill and restart them."

