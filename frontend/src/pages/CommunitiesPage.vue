<script setup lang="ts">
import { computed, ref } from 'vue'
import { RouterLink } from 'vue-router'

import SiteHeader from '../components/SiteHeader.vue'
import type { Country } from '../data/countries'

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

const STORAGE_KEY = 'latinVibe.communities'

const query = ref('')

function loadCommunities(): CommunityDraft[] {
  const raw = localStorage.getItem(STORAGE_KEY)
  const parsed: unknown = raw ? JSON.parse(raw) : []
  if (!Array.isArray(parsed)) return []

  // Backwards-compatible migration: older entries may not have an id.
  // We derive a stable id and persist the upgraded list.
  const upgraded = (parsed as any[]).map((c) => {
    const id = typeof c?.id === 'string' && c.id.length > 0 ? c.id : `${c?.createdAt ?? 'unknown'}_${c?.name ?? 'community'}`
    return { ...c, id } as CommunityDraft
  })
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(upgraded))
  } catch {
    // ignore persistence issues; list can still render
  }
  return upgraded
}

const communities = ref<CommunityDraft[]>(loadCommunities())

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

function locationLabel(c: CommunityDraft): string {
  if (c.global) return 'Global'
  const parts = [c.location?.city, c.location?.region, c.location?.country?.name].filter(Boolean) as string[]
  return parts.length ? parts.join(', ') : 'No location'
}

function refresh() {
  communities.value = loadCommunities()
}
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
              This list is stored locally in your browser for now. We’ll connect it to the backend later.
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
          <div v-if="filtered.length === 0" class="rounded-xl bg-slate-950/40 p-4 text-sm text-slate-300 ring-1 ring-white/10">
            No communities yet. Create your first one.
          </div>

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
                      <div class="truncate text-sm font-semibold text-white">{{ c.name }}</div>
                      <div class="mt-1 text-xs text-slate-300">{{ locationLabel(c) }}</div>
                    </div>
                    <div class="text-xs text-slate-400">Created {{ new Date(c.createdAt).toLocaleString() }}</div>
                  </div>

                  <p class="mt-3 line-clamp-3 text-sm text-slate-200 whitespace-pre-line">
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
        </div>
      </div>
    </section>
  </div>
</template>

