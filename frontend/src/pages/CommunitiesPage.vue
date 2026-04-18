<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'

import SiteHeader from '../components/SiteHeader.vue'
import type { Country } from '../data/countries'
import { listCommunities, type Community } from '../features/community/api'
import { listMembershipsByCommunity } from '../features/communityMembership/api'

const COMMUNITY_UUID_RE = /^[0-9a-fA-F-]{36}$/

type CommunityLocation = {
  country: Country | null
  region: string | null
  city: string | null
}

const query = ref('')

const communities = ref<Community[]>([])
/** Member (dancer) count per community id; `-1` means load failed */
const dancerCountsByCommunityId = ref<Record<string, number>>({})
const loading = ref(false)
const loadError = ref('')

async function loadDancerCountsForCommunities(rows: Community[]): Promise<Record<string, number>> {
  const out: Record<string, number> = {}
  await Promise.all(
    rows.map(async (c) => {
      if (!COMMUNITY_UUID_RE.test(c.id)) return
      try {
        const memberships = await listMembershipsByCommunity(c.id)
        out[c.id] = memberships.length
      } catch {
        out[c.id] = -1
      }
    }),
  )
  return out
}

const dancerPillTextById = computed(() => {
  const counts = dancerCountsByCommunityId.value
  const out: Record<string, string> = {}
  for (const c of communities.value) {
    if (!COMMUNITY_UUID_RE.test(c.id)) continue
    const n = counts[c.id]
    if (n === undefined) out[c.id] = '…'
    else if (n < 0) out[c.id] = '—'
    else out[c.id] = `${n} ${n === 1 ? 'dancer' : 'dancers'}`
  }
  return out
})

const filtered = computed(() => {
  const q = query.value.trim().toLowerCase()
  if (!q) return communities.value
  return communities.value.filter(c => {
    const haystack = [
      c.name,
      c.description,
      c.location?.country?.name,
      c.location?.country?.code,
      c.location?.region,
      c.location?.city,
      c.global ? 'global' : '',
    ]
      .filter(Boolean)
      .join(' ')
      .toLowerCase()
    return haystack.includes(q)
  })
})

function formatLocation(location: CommunityLocation | null | undefined): string {
  const parts = [location?.city, location?.region, location?.country?.name].filter(Boolean) as string[]
  return parts.length ? parts.join(', ') : 'No location'
}

function locationLabel(c: Community): string {
  if (c.global) return 'Global'
  return formatLocation(c.location)
}

async function refresh() {
  loadError.value = ''
  loading.value = true
  dancerCountsByCommunityId.value = {}
  try {
    const rows = await listCommunities()
    communities.value = rows
    dancerCountsByCommunityId.value = await loadDancerCountsForCommunities(rows)
  } catch (e) {
    loadError.value = e instanceof Error ? e.message : 'Could not load communities.'
    communities.value = []
    dancerCountsByCommunityId.value = {}
  } finally {
    loading.value = false
  }
}

onMounted(refresh)
</script>

<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-4xl px-4 pb-20 pt-8 sm:px-6 sm:pt-14">
      <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10 sm:p-8">
        <div class="flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center">
          <div>
            <h1 class="text-2xl font-semibold text-white">Communities</h1>
            <p class="mt-2 text-sm text-slate-200">
              Explore communities and discover what’s happening near you.
            </p>
          </div>
          <div class="flex items-center gap-2">
            <button
              type="button"
              class="inline-flex h-10 w-10 items-center justify-center rounded-xl bg-white/10 text-white ring-1 ring-white/15 transition hover:bg-white/15 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              @click="refresh"
              aria-label="Refresh communities"
              title="Refresh"
            >
              <svg viewBox="0 0 24 24" class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M3 12a9 9 0 0 1 15.36-6.36" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M18 3v5h-5" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 0 1-15.36 6.36" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 21v-5h5" />
              </svg>
            </button>
            <RouterLink
              class="group inline-flex h-10 items-center justify-center gap-2 rounded-xl bg-white px-4 text-sm font-semibold text-slate-900 transition hover:bg-slate-100 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              to="/communities/new"
              aria-label="Create a community"
            >
              <svg
                viewBox="0 0 24 24"
                class="h-5 w-5 text-slate-800 transition group-hover:text-slate-900"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 5v14" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M5 12h14" />
              </svg>
              <span>Create</span>
            </RouterLink>
          </div>
        </div>

        <div class="mt-6">
          <label class="block text-sm font-medium text-slate-200">Search</label>
          <input
            v-model="query"
            type="text"
            class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
            placeholder="Search by name, description, country, region, city…"
          />
        </div>

        <div class="mt-6 space-y-3">
          <div
            v-if="loadError"
            class="rounded-xl bg-rose-500/10 p-4 text-sm text-rose-200 ring-1 ring-rose-400/20"
          >
            {{ loadError }}
          </div>

          <div
            v-if="loading"
            class="rounded-xl bg-slate-950/40 p-4 text-sm text-slate-300 ring-1 ring-white/10"
          >
            Loading communities…
          </div>

          <div
            v-else-if="filtered.length === 0"
            class="rounded-xl bg-slate-950/40 p-4 text-sm text-slate-300 ring-1 ring-white/10"
          >
            No communities yet. Create your first one.
          </div>

          <template v-else>
            <div
              v-for="c in filtered"
              :key="c.id"
              class="rounded-2xl bg-slate-950/40 p-5 ring-1 ring-white/10"
            >
              <RouterLink class="block rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-400/60" :to="`/communities/${c.id}`">
                <div class="flex gap-4">
                  <div class="shrink-0">
                    <div
                      class="h-20 w-28 overflow-hidden rounded-xl bg-white/5 ring-1 ring-white/10"
                      style="aspect-ratio: 4 / 3"
                    >
                      <img
                        v-if="c.imageDataUrl"
                        :src="c.imageDataUrl"
                        alt=""
                        class="h-full w-full object-cover"
                        loading="lazy"
                      />
                      <div v-else class="grid h-full w-full place-items-center text-xs text-slate-400">No image</div>
                    </div>
                  </div>

                  <div class="min-w-0 flex-1">
                    <div class="flex flex-col gap-2 sm:flex-row sm:items-start sm:justify-between">
                      <div class="min-w-0">
                        <div class="flex flex-wrap items-center gap-2">
                          <div class="truncate text-sm font-semibold text-white">{{ c.name }}</div>
                          <span
                            v-if="dancerPillTextById[c.id]"
                            class="shrink-0 rounded-full bg-white/5 px-2.5 py-0.5 text-[11px] font-medium text-slate-300 ring-1 ring-white/10"
                          >
                            {{ dancerPillTextById[c.id] }}
                          </span>
                        </div>
                        <div class="mt-1 text-xs text-slate-300">{{ locationLabel(c) }}</div>
                      </div>
                      <div class="text-xs text-slate-400">Created {{ new Date(c.createdAt).toLocaleString() }}</div>
                    </div>

                    <p class="mt-3 line-clamp-3 whitespace-pre-line text-sm text-slate-200">
                      {{ c.description }}
                    </p>
                  </div>
                </div>
                <div class="mt-4 inline-flex items-center gap-2 text-xs font-semibold text-slate-200">
                  View community
                  <span aria-hidden="true">→</span>
                </div>
              </RouterLink>
            </div>
          </template>
        </div>
      </div>
    </section>
  </div>
</template>

