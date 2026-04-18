import { authHeader } from '../member/auth'

export type CommunityCountry = {
  code: string
  name: string
}

export type CommunityLocation = {
  country: CommunityCountry | null
  region: string | null
  city: string | null
}

export type Community = {
  id: string
  name: string
  description: string
  imageDataUrl: string | null
  global: boolean
  location: CommunityLocation | null
  createdAt: string
}

export type CreateCommunityInput = {
  name: string
  description: string
  imageDataUrl?: string
  global: boolean
  location?: {
    country?: CommunityCountry
    region?: string
    city?: string
  }
}

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

async function readErrorText(resp: Response): Promise<string> {
  return await resp.text().catch(() => '')
}

export async function createCommunity(input: CreateCommunityInput): Promise<Community> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/communities`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...authHeader() },
    body: JSON.stringify(input),
  })
  if (!resp.ok) {
    const text = await readErrorText(resp)
    throw new Error(text || `Create community failed (${resp.status})`)
  }
  return (await resp.json()) as Community
}

