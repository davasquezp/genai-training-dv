<script setup lang="ts">
import { computed, onBeforeUnmount, ref } from 'vue'

import SiteHeader from '../components/SiteHeader.vue'
import { COUNTRIES, type Country } from '../data/countries'

type Role = 'lead' | 'follower'

type InterestSubmission = {
  name: string
  role: Role
  country: Country
  styles: string[]
  createdAt: string
}

const STYLES = ['Bachata', 'Salsa', 'Kizomba', 'Zouk'] as const

const name = ref('')
const role = ref<Role | ''>('')
const countryQuery = ref('')
const country = ref<Country | null>(null)
const countryOpen = ref(false)
const styles = ref<string[]>([])

const submitted = ref(false)

const nameError = computed(() => (name.value.trim().length === 0 ? 'Name is required.' : ''))
const roleError = computed(() => (!role.value ? 'Please choose lead or follower.' : ''))
const countryError = computed(() => (!country.value ? 'Country is required.' : ''))
const stylesError = computed(() => (styles.value.length === 0 ? 'Select at least one style.' : ''))

const canSubmit = computed(() => !nameError.value && !roleError.value && !countryError.value && !stylesError.value)

const filteredCountries = computed(() => {
  const q = countryQuery.value.trim().toLowerCase()
  if (!q) return COUNTRIES
  return COUNTRIES.filter(c => c.name.toLowerCase().includes(q))
})

function toggleStyle(s: string) {
  const idx = styles.value.indexOf(s)
  if (idx >= 0) styles.value.splice(idx, 1)
  else styles.value.push(s)
}

function selectCountry(c: Country) {
  country.value = c
  countryQuery.value = c.name
  countryOpen.value = false
}

function clearCountry() {
  country.value = null
  countryQuery.value = ''
  countryOpen.value = false
}

const STORAGE_KEY = 'latinVibe.interests'

function saveSubmission(sub: InterestSubmission) {
  const raw = localStorage.getItem(STORAGE_KEY)
  const existing: InterestSubmission[] = raw ? JSON.parse(raw) : []
  existing.unshift(sub)
  localStorage.setItem(STORAGE_KEY, JSON.stringify(existing))
}

function onSubmit() {
  if (!canSubmit.value || !country.value || !role.value) return
  const sub: InterestSubmission = {
    name: name.value.trim(),
    role: role.value,
    country: country.value,
    styles: [...styles.value],
    createdAt: new Date().toISOString(),
  }
  saveSubmission(sub)
  submitted.value = true
}

function onGlobalPointerDown(e: PointerEvent) {
  const target = e.target as HTMLElement | null
  if (!target) return
  if (target.closest('[data-country]')) return
  countryOpen.value = false
}

window.addEventListener('pointerdown', onGlobalPointerDown)
onBeforeUnmount(() => window.removeEventListener('pointerdown', onGlobalPointerDown))
</script>

<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-2xl px-4 pb-20 pt-8 sm:px-6 sm:pt-14">
      <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10 sm:p-8">
        <div class="flex items-start justify-between gap-4">
          <div>
            <h1 class="text-2xl font-semibold text-white">Register your interest</h1>
            <p class="mt-2 text-sm text-slate-200">
              Tell us how you dance, and we’ll tailor LatinVibe to your community.
            </p>
          </div>
          <RouterLink class="text-sm text-slate-200 hover:text-white" to="/">Back</RouterLink>
        </div>

        <div
          v-if="submitted"
          class="mt-8 rounded-xl bg-emerald-500/10 p-4 ring-1 ring-emerald-400/20"
        >
          <div class="text-sm font-semibold text-emerald-200">Thanks! You’re on the list.</div>
          <div class="mt-1 text-sm text-slate-200">
            We saved your preferences locally for now. Next step: send these to the backend.
          </div>
          <div class="mt-4 flex gap-3">
            <RouterLink
              class="inline-flex items-center justify-center rounded-xl bg-white px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-slate-100"
              to="/"
            >
              Back to home
            </RouterLink>
            <button
              class="inline-flex items-center justify-center rounded-xl bg-white/10 px-4 py-2 text-sm font-semibold text-white ring-1 ring-white/15 hover:bg-white/15"
              type="button"
              @click="submitted = false"
            >
              Submit another
            </button>
          </div>
        </div>

        <form v-else class="mt-8 space-y-6" @submit.prevent="onSubmit">
          <div>
            <label class="block text-sm font-medium text-slate-200">Name</label>
            <input
              v-model="name"
              type="text"
              autocomplete="name"
              class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="Your name"
            />
            <p v-if="nameError" class="mt-2 text-xs text-rose-200">{{ nameError }}</p>
          </div>

          <div>
            <div class="flex items-center justify-between">
              <label class="block text-sm font-medium text-slate-200">Role</label>
              <span class="text-xs text-slate-400">Choose one</span>
            </div>
            <div class="mt-2 grid grid-cols-2 gap-3">
              <button
                type="button"
                class="rounded-xl px-4 py-3 text-sm font-semibold ring-1 ring-white/10 transition"
                :class="role === 'lead' ? 'bg-white text-slate-900' : 'bg-slate-950/60 text-white hover:bg-white/10'"
                @click="role = 'lead'"
              >
                Lead
              </button>
              <button
                type="button"
                class="rounded-xl px-4 py-3 text-sm font-semibold ring-1 ring-white/10 transition"
                :class="role === 'follower' ? 'bg-white text-slate-900' : 'bg-slate-950/60 text-white hover:bg-white/10'"
                @click="role = 'follower'"
              >
                Follower
              </button>
            </div>
            <p v-if="roleError" class="mt-2 text-xs text-rose-200">{{ roleError }}</p>
          </div>

          <div data-country class="relative">
            <label class="block text-sm font-medium text-slate-200">Country</label>
            <div class="mt-2 flex gap-2">
              <input
                v-model="countryQuery"
                type="text"
                autocomplete="off"
                class="w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
                placeholder="Start typing to search…"
                @focus="countryOpen = true"
                @input="countryOpen = true; country = null"
              />
              <button
                type="button"
                class="rounded-xl bg-white/10 px-3 py-3 text-sm font-semibold text-white ring-1 ring-white/15 hover:bg-white/15"
                @click="clearCountry"
                title="Clear country"
              >
                Clear
              </button>
            </div>

            <div
              v-if="countryOpen"
              class="absolute z-10 mt-2 max-h-64 w-full overflow-auto rounded-xl bg-slate-950 ring-1 ring-white/10"
            >
              <button
                v-for="c in filteredCountries"
                :key="c.code"
                type="button"
                class="flex w-full items-center justify-between px-4 py-3 text-left text-sm text-slate-100 hover:bg-white/10"
                @click="selectCountry(c)"
              >
                <span>{{ c.name }}</span>
                <span class="text-xs text-slate-400">{{ c.code }}</span>
              </button>
              <div v-if="filteredCountries.length === 0" class="px-4 py-3 text-sm text-slate-300">
                No matches.
              </div>
            </div>

            <p v-if="countryError" class="mt-2 text-xs text-rose-200">{{ countryError }}</p>
          </div>

          <div>
            <div class="flex items-center justify-between">
              <label class="block text-sm font-medium text-slate-200">Styles</label>
              <span class="text-xs text-slate-400">Multi-select</span>
            </div>
            <div class="mt-2 flex flex-wrap gap-2">
              <button
                v-for="s in STYLES"
                :key="s"
                type="button"
                class="rounded-full px-4 py-2 text-sm font-semibold ring-1 ring-white/10 transition"
                :class="styles.includes(s) ? 'bg-white text-slate-900' : 'bg-slate-950/60 text-white hover:bg-white/10'"
                @click="toggleStyle(s)"
              >
                {{ s }}
              </button>
            </div>
            <p v-if="stylesError" class="mt-2 text-xs text-rose-200">{{ stylesError }}</p>
          </div>

          <div class="pt-2">
            <button
              class="w-full rounded-xl px-4 py-3 text-sm font-semibold transition"
              :class="canSubmit ? 'bg-white text-slate-900 hover:bg-slate-100' : 'bg-white/10 text-slate-300 ring-1 ring-white/10'"
              type="submit"
              :disabled="!canSubmit"
            >
              Submit interest
            </button>
            <p class="mt-3 text-xs text-slate-400">
              Saved locally under <code class="rounded bg-white/10 px-1.5 py-0.5 text-slate-200">latinVibe.interests</code>.
            </p>
          </div>
        </form>
      </div>

      <div class="mt-6 text-center text-xs text-slate-400">
        Want to see the API? <a class="hover:text-slate-200" href="http://localhost:8080/swagger-ui/index.html" target="_blank" rel="noreferrer">Swagger UI</a>
      </div>
    </section>
  </div>
</template>

