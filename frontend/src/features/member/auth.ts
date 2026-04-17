export type MemberRole = 'NONE' | 'DANCER' | 'COMMUNITY_MANAGER' | 'INSTRUCTOR'

import { reactive } from 'vue'

export type Member = {
  id: string
  email: string
  name: string
  phone: string
  countryCode?: string | null
  countryName?: string | null
  nationalityCode?: string | null
  nationalityName?: string | null
  roles: MemberRole[]
  createdAt: string
}

type AuthResponse = {
  token: string
  member: Member
}

const TOKEN_KEY = 'latinVibe.auth.token'
const MEMBER_KEY = 'latinVibe.auth.member'

export const authState = reactive<{
  token: string
  member: Member | null
}>({
  token: localStorage.getItem(TOKEN_KEY) ?? '',
  member: (() => {
    const raw = localStorage.getItem(MEMBER_KEY)
    if (!raw) return null
    try {
      return JSON.parse(raw) as Member
    } catch {
      return null
    }
  })(),
})

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

export function getToken(): string {
  return authState.token
}

export function setToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token)
  authState.token = token
}

export function clearAuth() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(MEMBER_KEY)
  authState.token = ''
  authState.member = null
}

export function getCachedMember(): Member | null {
  return authState.member
}

export function cacheMember(member: Member) {
  localStorage.setItem(MEMBER_KEY, JSON.stringify(member))
  authState.member = member
}

export function isAuthenticated(): boolean {
  return authState.token.length > 0
}

export function hasRole(role: MemberRole): boolean {
  const m = authState.member
  return !!m && Array.isArray(m.roles) && m.roles.includes(role)
}

export async function signup(email: string, name: string, phone: string, password: string): Promise<Member> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/members/signup`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, name, phone, password }),
  })
  if (!resp.ok) {
    const text = await resp.text().catch(() => '')
    throw new Error(text || `Signup failed (${resp.status})`)
  }
  const member = (await resp.json()) as Member
  cacheMember(member)
  return member
}

export async function login(email: string, password: string): Promise<AuthResponse> {
  const base = apiBaseUrl()
  const resp = await fetch(`${base}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  })
  if (!resp.ok) {
    const text = await resp.text().catch(() => '')
    throw new Error(text || `Login failed (${resp.status})`)
  }
  const data = (await resp.json()) as AuthResponse
  setToken(data.token)
  cacheMember(data.member)
  return data
}

export async function fetchMe(): Promise<Member> {
  const base = apiBaseUrl()
  const token = getToken()
  const resp = await fetch(`${base}/api/auth/me`, {
    headers: { Authorization: `Bearer ${token}` },
  })
  if (!resp.ok) {
    throw new Error(`Me failed (${resp.status})`)
  }
  const member = (await resp.json()) as Member
  cacheMember(member)
  return member
}

export async function logout(): Promise<void> {
  const base = apiBaseUrl()
  const token = getToken()
  if (!token) {
    clearAuth()
    return
  }
  await fetch(`${base}/api/auth/logout`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token}` },
  }).catch(() => {})
  clearAuth()
}

export async function updateMyProfile(input: {
  name: string
  country: { code: string; name: string } | null
  nationality: { code: string; name: string } | null
}): Promise<Member> {
  const base = apiBaseUrl()
  const token = getToken()
  const resp = await fetch(`${base}/api/members/me/profile`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
    body: JSON.stringify(input),
  })
  if (!resp.ok) {
    const text = await resp.text().catch(() => '')
    throw new Error(text || `Update profile failed (${resp.status})`)
  }
  const member = (await resp.json()) as Member
  cacheMember(member)
  return member
}

export function authHeader(): Record<string, string> {
  const token = getToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
}

