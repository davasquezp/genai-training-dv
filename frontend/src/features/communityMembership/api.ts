export type CommunityMembership = {
  id: string
  dancerId: string
  communityId: string
  createdAt: string
}

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

async function readErrorText(resp: Response): Promise<string> {
  return await resp.text().catch(() => '')
}

export async function listMembershipsByCommunity(communityId: string): Promise<CommunityMembership[]> {
  const base = apiBaseUrl()
  const q = new URLSearchParams({ communityId })
  const resp = await fetch(`${base}/api/community-memberships?${q.toString()}`)
  if (!resp.ok) {
    const text = await readErrorText(resp)
    throw new Error(text || `List memberships failed (${resp.status})`)
  }
  return (await resp.json()) as CommunityMembership[]
}
