import { authHeader, type Member } from '../../member/auth'

export type MemberProfilePage = {
  member: Member
  dancer: {
    id: string
    name: string
    dancerRoles: string[]
  } | null
}

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

async function readErrorText(resp: Response): Promise<string> {
  return await resp.text().catch(() => '')
}

export async function getMemberProfilePage(): Promise<MemberProfilePage> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/page/me`, { headers: { ...authHeader() } })
  if (resp.status === 401) {
    throw new Error('Unauthorized')
  }
  if (!resp.ok) {
    const text = await readErrorText(resp)
    throw new Error(text || `Load profile failed (${resp.status})`)
  }
  return (await resp.json()) as MemberProfilePage
}
