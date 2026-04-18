export type DancersListPage = {
  items: Array<{
    id: string
    name: string
    roles: string[]
    styles: string[]
    createdAt: string
  }>
}

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

async function readErrorText(resp: Response): Promise<string> {
  return await resp.text().catch(() => '')
}

export async function getDancersListPage(): Promise<DancersListPage> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/page/dancers`)
  if (!resp.ok) {
    const text = await readErrorText(resp)
    throw new Error(text || `Load dancers failed (${resp.status})`)
  }
  return (await resp.json()) as DancersListPage
}
