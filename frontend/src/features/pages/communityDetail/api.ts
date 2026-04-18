import { authHeader } from '../../member/auth'

export type CommunityDetailPage = {
  community: {
    id: string
    name: string
    description: string
    imageDataUrl: string | null
    global: boolean
    location: {
      country: { code: string; name: string } | null
      region: string | null
      city: string | null
    } | null
    createdAt: string
  }
  dancerCount: number
  viewer: {
    authenticated: boolean
    isDancer: boolean
    alreadyMember: boolean
    myDancerId: string | null
  }
}

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

async function readErrorText(resp: Response): Promise<string> {
  return await resp.text().catch(() => '')
}

export async function getCommunityDetailPage(communityId: string): Promise<CommunityDetailPage | null> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/page/communities/${encodeURIComponent(communityId)}`, {
    headers: { ...authHeader() },
  })
  if (resp.status === 404) return null
  if (!resp.ok) {
    const text = await readErrorText(resp)
    throw new Error(text || `Load community failed (${resp.status})`)
  }
  return (await resp.json()) as CommunityDetailPage
}
