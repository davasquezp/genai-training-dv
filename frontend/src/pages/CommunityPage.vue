<script setup lang="ts">
import { computed, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'

import SiteHeader from '../components/SiteHeader.vue'
import type { Country } from '../data/countries'
import { authHeader, getCachedMember, hasRole, isAuthenticated } from '../features/member/auth'

type CommunityLocation = {
  country?: Country
  region?: string
  city?: string
}

type CommunityDraft = {
  id: string
  name: string
  description: string
  imageDataUrl?: string
  global: boolean
  location?: CommunityLocation
  createdAt: string
}

type MockEvent = {
  title: string
  when: string
  where: string
  kind: 'workshop' | 'festival' | 'social'
}

type PartySlot = {
  day: string
  time: string
  title: string
}

const STORAGE_KEY = 'latinVibe.communities'

const route = useRoute()
const communityId = computed(() => String(route.params.id ?? ''))
const canJoinCommunity = computed(() => isAuthenticated() && hasRole('DANCER'))

const joining = ref(false)
const joinError = ref('')

async function joinCommunity() {
  if (!community.value) return
  joinError.value = ''

  const member = getCachedMember()
  if (!member) {
    joinError.value = 'Please login first.'
    return
  }

  // Communities are currently stored in localStorage and their id is typically a UUID.
  // The membership API expects UUIDs.
  const communityUuid = community.value.id
  if (!/^[0-9a-fA-F-]{36}$/.test(communityUuid)) {
    joinError.value = 'This community has a non-UUID id and cannot be joined yet.'
    return
  }

  joining.value = true
  try {
    const base = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()?.replace(/\/+$/, '') ?? ''
    const resp = await fetch(`${base}/api/community-memberships`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', ...authHeader() },
      body: JSON.stringify({
        dancerId: member.id,
        communityId: communityUuid,
      }),
    })
    if (!resp.ok) {
      const text = await resp.text().catch(() => '')
      throw new Error(text || `Join failed (${resp.status})`)
    }
  } catch (e: any) {
    joinError.value = e?.message || 'Could not join community.'
  } finally {
    joining.value = false
  }
}

function loadCommunities(): CommunityDraft[] {
  const raw = localStorage.getItem(STORAGE_KEY)
  const parsed: unknown = raw ? JSON.parse(raw) : []
  if (!Array.isArray(parsed)) return []
  return (parsed as any[]).map((c) => {
    const id = typeof c?.id === 'string' && c.id.length > 0 ? c.id : `${c?.createdAt ?? 'unknown'}_${c?.name ?? 'community'}`
    return { ...c, id } as CommunityDraft
  })
}

const community = computed(() => loadCommunities().find(c => c.id === communityId.value) ?? null)

function locationLabel(c: CommunityDraft): string {
  if (c.global) return 'Global'
  const parts = [c.location?.city, c.location?.region, c.location?.country?.name].filter(Boolean) as string[]
  return parts.length ? parts.join(', ') : 'No location'
}

function hashSeed(input: string): number {
  let h = 2166136261
  for (let i = 0; i < input.length; i++) {
    h ^= input.charCodeAt(i)
    h = Math.imul(h, 16777619)
  }
  return h >>> 0
}

function pick<T>(arr: T[], idx: number): T {
  return arr[idx % arr.length]
}

const mockEvents = computed<MockEvent[]>(() => {
  const c = community.value
  if (!c) return []
  const seed = hashSeed(c.id)

  const cities = ['Downtown Studio', 'Rooftop Terrace', 'Community Hall', 'Beach Venue', 'Main Ballroom']
  const kinds: MockEvent['kind'][] = ['social', 'workshop', 'festival']
  const titlesByKind: Record<MockEvent['kind'], string[]> = {
    social: ['Salsa Social Night', 'Bachata Social', 'Kizomba Lounge'],
    workshop: ['Technique Lab', 'Musicality Workshop', 'Footwork Intensive'],
    festival: ['Weekend Festival Pass', 'Mini-Festival Day', 'Festival + Accommodation'],
  }

  const base = new Date()
  const next = (daysAhead: number) => {
    const d = new Date(base)
    d.setDate(d.getDate() + daysAhead)
    return d.toLocaleDateString(undefined, { weekday: 'short', month: 'short', day: 'numeric' })
  }

  return [7, 14, 30].map((daysAhead, i) => {
    const kind = pick(kinds, seed + i)
    return {
      kind,
      title: pick(titlesByKind[kind], seed + i * 7),
      when: `${next(daysAhead)} • 7:30 PM`,
      where: c.global ? 'Online / multi-city' : pick(cities, seed + i * 11),
    }
  })
})

const partySchedule = computed<PartySlot[]>(() => {
  const c = community.value
  if (!c) return []
  const seed = hashSeed(c.id)
  const slots: PartySlot[] = [
    { day: 'Thu', time: '9:00 PM', title: 'Thursday Social' },
    { day: 'Fri', time: '10:00 PM', title: 'Friday Party' },
    { day: 'Sat', time: '10:30 PM', title: 'Saturday Social' },
  ]
  return slots.map((s, i) => {
    const variants = ['(DJ set)', '(live band)', '(beginner-friendly)', '(intermediate+)']
    return { ...s, title: `${s.title} ${pick(variants, seed + i * 13)}` }
  })
})
</script>

<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-4xl px-4 pb-20 pt-8 sm:px-6 sm:pt-14">
      <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10 sm:p-8">
        <div class="flex items-start justify-between gap-4">
          <div>
            <h1 class="text-2xl font-semibold text-white">
              {{ community?.name ?? 'Community' }}
            </h1>
            <p v-if="community" class="mt-2 text-sm text-slate-200">
              {{ locationLabel(community) }} • Created {{ new Date(community.createdAt).toLocaleDateString() }}
            </p>
            <p v-else class="mt-2 text-sm text-rose-200">Community not found.</p>
          </div>
          <RouterLink class="text-sm text-slate-200 hover:text-white" to="/communities">Back</RouterLink>
        </div>

        <div v-if="community" class="mt-6 space-y-6">
          <div v-if="canJoinCommunity" class="rounded-2xl bg-white/5 p-5 ring-1 ring-white/10">
            <div class="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
              <div>
                <div class="text-sm font-semibold text-white">Join this community</div>
                <div class="mt-1 text-xs text-slate-300">Visible only to authenticated dancers.</div>
              </div>
              <button
                type="button"
                class="inline-flex items-center justify-center rounded-xl bg-white px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-slate-100 disabled:cursor-not-allowed disabled:opacity-60"
                :disabled="joining"
                @click="joinCommunity"
              >
                {{ joining ? 'Joining…' : 'Join community' }}
              </button>
            </div>
            <div v-if="joinError" class="mt-3 text-xs text-rose-200">{{ joinError }}</div>
          </div>

          <div class="overflow-hidden rounded-2xl bg-slate-950/40 ring-1 ring-white/10">
            <div class="relative w-full" style="aspect-ratio: 4 / 3">
              <img
                v-if="community.imageDataUrl"
                :src="community.imageDataUrl"
                alt=""
                class="h-full w-full object-cover"
              />
              <div
                v-else
                class="grid h-full w-full place-items-center bg-gradient-to-br from-white/10 to-white/5 text-sm text-slate-200"
              >
                Community image (coming soon)
              </div>
            </div>
          </div>

          <div class="rounded-2xl bg-slate-950/40 p-5 ring-1 ring-white/10">
            <div class="text-sm font-semibold text-white">About</div>
            <p class="mt-2 whitespace-pre-line text-sm text-slate-200">{{ community.description }}</p>
          </div>

          <div class="grid gap-6 md:grid-cols-2">
            <div class="rounded-2xl bg-slate-950/40 p-5 ring-1 ring-white/10">
              <div class="flex items-center justify-between">
                <div class="text-sm font-semibold text-white">Related events</div>
                <span class="rounded-full bg-white/5 px-2 py-1 text-xs text-slate-300 ring-1 ring-white/10">
                  Prototype
                </span>
              </div>
              <div class="mt-4 space-y-3">
                <div
                  v-for="e in mockEvents"
                  :key="e.title + e.when"
                  class="rounded-xl bg-white/5 p-4 ring-1 ring-white/10"
                >
                  <div class="flex items-center justify-between gap-3">
                    <div class="text-sm font-semibold text-white">{{ e.title }}</div>
                    <span class="rounded-full bg-white/5 px-2 py-1 text-[11px] text-slate-300 ring-1 ring-white/10">
                      {{ e.kind }}
                    </span>
                  </div>
                  <div class="mt-2 text-xs text-slate-300">{{ e.when }}</div>
                  <div class="mt-1 text-xs text-slate-400">{{ e.where }}</div>
                </div>
              </div>
            </div>

            <div class="rounded-2xl bg-slate-950/40 p-5 ring-1 ring-white/10">
              <div class="flex items-center justify-between">
                <div class="text-sm font-semibold text-white">Party schedule</div>
                <span class="rounded-full bg-white/5 px-2 py-1 text-xs text-slate-300 ring-1 ring-white/10">
                  Prototype
                </span>
              </div>
              <div class="mt-4 space-y-3">
                <div
                  v-for="p in partySchedule"
                  :key="p.day + p.time + p.title"
                  class="flex items-center justify-between gap-4 rounded-xl bg-white/5 p-4 ring-1 ring-white/10"
                >
                  <div>
                    <div class="text-sm font-semibold text-white">{{ p.title }}</div>
                    <div class="mt-1 text-xs text-slate-300">{{ p.day }} • {{ p.time }}</div>
                  </div>
                  <div class="text-xs text-slate-400">Weekly</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

