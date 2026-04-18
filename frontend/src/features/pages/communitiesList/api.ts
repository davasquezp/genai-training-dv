export type CommunitiesListPage = {
  items: Array<{
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
    dancerCount: number
  }>
}

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

async function readErrorText(resp: Response): Promise<string> {
  return await resp.text().catch(() => '')
}

export async function getCommunitiesListPage(): Promise<CommunitiesListPage> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/page/communities`)
  if (!resp.ok) {
    const text = await readErrorText(resp)
    throw new Error(text || `Load communities failed (${resp.status})`)
  }
  return (await resp.json()) as CommunitiesListPage
}

