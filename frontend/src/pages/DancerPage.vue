<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-2xl px-4 pb-20 pt-8 sm:px-6 sm:pt-14">
      <div v-if="loading" class="rounded-2xl bg-white/5 p-12 text-center text-slate-400 ring-1 ring-white/10">
        Loading dancer profile...
      </div>

      <div v-else-if="error" class="rounded-2xl bg-white/5 p-8 text-center ring-1 ring-white/10">
        <p class="text-rose-300">{{ error }}</p>
        <RouterLink
          to="/dancers"
          class="mt-6 inline-block rounded-xl bg-white/10 px-6 py-3 text-sm font-medium text-white hover:bg-white/20"
        >
          ← Back to Dancers
        </RouterLink>
      </div>

      <div v-else-if="dancer" class="space-y-8">
        <div class="flex items-center justify-between">
          <RouterLink
            to="/dancers"
            class="inline-flex items-center gap-2 text-sm text-slate-400 hover:text-white"
          >
            ← All dancers
          </RouterLink>
          <div class="rounded-full bg-emerald-400/10 px-4 py-1 text-xs font-medium text-emerald-300 ring-1 ring-emerald-400/30">
            Registered
          </div>
        </div>

        <div class="rounded-3xl bg-white/5 p-8 ring-1 ring-white/10">
          <div class="flex flex-col gap-6 sm:flex-row sm:items-start">
            <div class="flex-1">
              <div class="text-4xl font-semibold text-white">{{ dancer.name }}</div>
              <div class="mt-2 flex items-center gap-3">
                <div class="flex flex-wrap items-center gap-2">
                  <span
                    v-for="r in dancer.roles"
                    :key="r"
                    class="rounded-full bg-white/10 px-3 py-1 text-xs font-semibold uppercase tracking-wider text-slate-200 ring-1 ring-white/10"
                  >
                    {{ r }}
                  </span>
                  <span v-if="!dancer.roles || dancer.roles.length === 0" class="text-sm text-slate-400">—</span>
                </div>
              </div>
            </div>

            <div class="text-right text-sm text-slate-400">
              Joined {{ formatDate(dancer.createdAt) }}
            </div>
          </div>

          <div v-if="dancer.styles && dancer.styles.length" class="mt-8">
            <div class="mb-3 text-xs font-medium uppercase tracking-widest text-slate-400">Dance styles</div>
            <div class="flex flex-wrap gap-2">
              <span
                v-for="style in dancer.styles"
                :key="style"
                class="rounded-2xl bg-indigo-500/10 px-5 py-2 text-sm text-indigo-200 ring-1 ring-indigo-400/30"
              >
                {{ style }}
              </span>
            </div>
          </div>
        </div>

        <div class="rounded-2xl bg-white/5 p-8 ring-1 ring-white/10 text-sm">
          <h3 class="mb-4 text-xs font-medium uppercase tracking-widest text-slate-400">Profile details</h3>
          <dl class="grid grid-cols-1 gap-y-6 sm:grid-cols-2">
            <div>
              <dt class="text-slate-400">Registered on</dt>
              <dd class="mt-1 text-slate-200">{{ new Date(dancer.createdAt).toLocaleString() }}</dd>
            </div>
          </dl>
        </div>

      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'

import SiteHeader from '../components/SiteHeader.vue'

type Dancer = {
  id: string
  name: string
  roles: string[]
  styles: string[]
  createdAt: string
}

const route = useRoute()
const dancerId = route.params.id as string

const dancer = ref<Dancer | null>(null)
const loading = ref(true)
const error = ref('')

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : 'http://localhost:8080'
}

async function fetchDancer() {
  loading.value = true
  error.value = ''

  try {
    const base = apiBaseUrl()
    const resp = await fetch(`${base}/api/dancers`)

    if (!resp.ok) throw new Error('Failed to load data')

    const allDancers: Dancer[] = await resp.json()
    const found = allDancers.find(d => d.id === dancerId)

    if (!found) {
      throw new Error('Dancer not found')
    }

    dancer.value = found
  } catch (err: any) {
    error.value = err.message || 'Could not load dancer profile'
    console.error(err)
  } finally {
    loading.value = false
  }
}

function formatDate(iso: string): string {
  try {
    return new Date(iso).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    })
  } catch {
    return iso.split('T')[0] || ''
  }
}

onMounted(() => {
  if (dancerId) {
    fetchDancer()
  } else {
    error.value = 'No dancer ID provided'
    loading.value = false
  }
})
</script>
