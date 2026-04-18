import { authHeader } from '../member/auth'

export type DancerMe = {
  id: string
  name: string
  dancerRoles: string[]
}

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

async function readErrorText(resp: Response): Promise<string> {
  return await resp.text().catch(() => '')
}

export async function updateMyDancer(payload: { name: string; dancerRoles: string[] }): Promise<DancerMe> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/dancers/me`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', ...authHeader() },
    body: JSON.stringify(payload),
  })
  if (!resp.ok) {
    const text = await readErrorText(resp)
    throw new Error(text || `Save failed (${resp.status})`)
  }
  return (await resp.json()) as DancerMe
}
