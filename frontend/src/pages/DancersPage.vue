<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-4xl px-4 pb-20 pt-8 sm:px-6 sm:pt-14">
      <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10 sm:p-8">
        <div class="flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center">
          <div>
            <h1 class="text-2xl font-semibold text-white">Dancers</h1>
            <p class="mt-2 text-sm text-slate-200">
              All registered dancers (live from the backend). Search by name, country, role or style.
            </p>
          </div>
          <div class="flex items-center gap-2">
            <button
              type="button"
              class="inline-flex h-10 w-10 items-center justify-center rounded-xl bg-white/10 text-white ring-1 ring-white/15 transition hover:bg-white/15 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              @click="refresh"
              aria-label="Refresh dancers"
              title="Refresh"
            >
              <svg viewBox="0 0 24 24" class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M3 12a9 9 0 0 1 15.36-6.36" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M18 3v5h-5" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 0 1-15.36 6.36" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 21v-5h5" />
              </svg>
            </button>
          </div>
        </div>

        <div class="mt-6">
          <label class="block text-sm font-medium text-slate-200">Search</label>
          <input
            v-model="query"
            type="text"
            class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
            placeholder="Search by name, country, role, style..."
          />
        </div>

        <div class="mt-6 space-y-3">
          <div v-if="loading" class="rounded-xl bg-slate-950/40 p-8 text-center text-slate-400">
            Loading dancers from backend...
          </div>
          <div v-else-if="filtered.length === 0" class="rounded-xl bg-slate-950/40 p-8 text-center text-slate-300 ring-1 ring-white/10">
            {{ error ? error : 'No dancers registered yet.' }}
            <div v-if="!error" class="mt-4">
              <RouterLink
                to="/signup"
                class="inline-flex items-center gap-2 rounded-xl bg-white/10 px-6 py-2.5 text-sm text-white hover:bg-white/20"
              >
                Register the first dancer →
              </RouterLink>
            </div>
          </div>

          <div
            v-for="dancer in filtered"
            :key="dancer.id"
            class="rounded-2xl bg-slate-950/40 p-5 ring-1 ring-white/10 transition hover:ring-white/30"
          >
            <RouterLink
              class="block focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              :to="`/dancers/${dancer.id}`"
            >
              <div class="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                <div class="min-w-0 flex-1">
                  <div class="flex items-baseline gap-3">
                    <div class="text-lg font-semibold text-white">{{ dancer.name }}</div>
                    <div class="flex flex-wrap items-center gap-2">
                      <span
                        v-for="r in dancer.roles"
                        :key="r"
                        class="rounded-full bg-white/10 px-3 py-0.5 text-xs font-semibold uppercase tracking-wider text-slate-200 ring-1 ring-white/10"
                      >
                        {{ r }}
                      </span>
                      <span v-if="!dancer.roles || dancer.roles.length === 0" class="text-xs text-slate-400">—</span>
                    </div>
                  </div>

                  <div class="mt-1 flex flex-wrap gap-x-4 gap-y-1 text-sm text-slate-400">
                    <div v-if="dancer.styles && dancer.styles.length" class="flex flex-wrap gap-1">
                      <span
                        v-for="style in dancer.styles"
                        :key="style"
                        class="inline-block rounded bg-white/5 px-2 py-0.5 text-xs text-indigo-300"
                      >
                        {{ style }}
                      </span>
                    </div>
                  </div>
                </div>

                <div class="text-right text-xs text-slate-500 whitespace-nowrap">
                  {{ formatDate(dancer.createdAt) }}
                </div>
              </div>

              <div class="mt-4 inline-flex items-center gap-2 text-xs font-semibold text-slate-200">
                View profile
                <span aria-hidden="true">→</span>
              </div>
            </RouterLink>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'

import SiteHeader from '../components/SiteHeader.vue'

type Dancer = {
  id: string
  name: string
  roles: string[]
  styles: string[]
  createdAt: string
}

const query = ref('')
const dancers = ref<Dancer[]>([])
const loading = ref(true)
const error = ref('')

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : 'http://localhost:8080'
}

async function fetchDancers() {
  loading.value = true
  error.value = ''

  try {
    const base = apiBaseUrl()
    const resp = await fetch(`${base}/api/dancers`)

    if (!resp.ok) {
      throw new Error(`HTTP ${resp.status}`)
    }

    const data: Dancer[] = await resp.json()
    dancers.value = data
  } catch (err: any) {
    error.value = err.message || 'Failed to load dancers'
    console.error(err)
    // Fallback to empty list so UI doesn't crash
    dancers.value = []
  } finally {
    loading.value = false
  }
}

function refresh() {
  fetchDancers()
}

const filtered = computed(() => {
  const q = query.value.trim().toLowerCase()
  if (!q) return dancers.value

  return dancers.value.filter(d => {
    const haystack = [
      d.name,
      ...(d.roles || []),
      ...(d.styles || []),
    ]
      .filter(Boolean)
      .join(' ')
      .toLowerCase()

    return haystack.includes(q)
  })
})

function formatDate(iso: string): string {
  try {
    return new Date(iso).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    })
  } catch {
    return iso
  }
}

onMounted(() => {
  fetchDancers()
})
</script>
