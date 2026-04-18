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

/**
 * Loads the dancer profile for the authenticated member.
 * @returns null when no dancer row exists yet (activate dancer on profile first)
 */
export async function getMyDancer(): Promise<DancerMe | null> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/dancers/me`, { headers: { ...authHeader() } })
  if (resp.status === 404) return null
  if (!resp.ok) {
    const text = await resp.text().catch(() => '')
    throw new Error(text || `Load dancer failed (${resp.status})`)
  }
  return (await resp.json()) as DancerMe
}
