<script setup lang="ts">
import { computed, onBeforeUnmount, ref } from 'vue'
import { RouterLink } from 'vue-router'

import SiteHeader from '../components/SiteHeader.vue'
import { COUNTRIES, type Country } from '../data/countries'

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

const name = ref('')
const description = ref('')
const isGlobal = ref(false)

const countryQuery = ref('')
const country = ref<Country | null>(null)
const countryOpen = ref(false)

const region = ref('')
const city = ref('')

const imageDataUrl = ref<string>('')
const imageError = ref<string>('')

const submitted = ref(false)
const submitError = ref('')

const nameError = computed(() => (name.value.trim().length === 0 ? 'Name is required.' : ''))
const descriptionError = computed(() =>
  description.value.trim().length === 0 ? 'Description is required.' : '',
)

const locationError = computed(() => {
  if (isGlobal.value) return ''
  if (!country.value && !region.value.trim() && !city.value.trim()) {
    return 'Add at least one location detail or mark the community as global.'
  }
  return ''
})

const canSubmit = computed(
  () => !nameError.value && !descriptionError.value && !locationError.value && !imageError.value,
)

const filteredCountries = computed(() => {
  const q = countryQuery.value.trim().toLowerCase()
  if (!q) return COUNTRIES
  return COUNTRIES.filter(c => c.name.toLowerCase().includes(q))
})

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

function saveCommunity(draft: CommunityDraft) {
  const raw = localStorage.getItem(STORAGE_KEY)
  const existing: CommunityDraft[] = raw ? JSON.parse(raw) : []
  existing.unshift(draft)
  localStorage.setItem(STORAGE_KEY, JSON.stringify(existing))
}

function newCommunityId(): string {
  if (typeof crypto !== 'undefined' && 'randomUUID' in crypto) {
    return crypto.randomUUID()
  }
  return `c_${Math.random().toString(16).slice(2)}_${Date.now().toString(16)}`
}

function buildLocation(): CommunityLocation | undefined {
  if (isGlobal.value) return undefined
  const loc: CommunityLocation = {}
  if (country.value) loc.country = country.value
  if (region.value.trim()) loc.region = region.value.trim()
  if (city.value.trim()) loc.city = city.value.trim()
  return Object.keys(loc).length ? loc : undefined
}

function readFileAsDataUrl(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onerror = () => reject(new Error('Could not read file'))
    reader.onload = () => resolve(String(reader.result ?? ''))
    reader.readAsDataURL(file)
  })
}

function loadImageDimensions(dataUrl: string): Promise<{ width: number; height: number }> {
  return new Promise((resolve, reject) => {
    const img = new Image()
    img.onerror = () => reject(new Error('Could not load image'))
    img.onload = () => resolve({ width: img.naturalWidth, height: img.naturalHeight })
    img.src = dataUrl
  })
}

async function onImageSelected(e: Event) {
  const input = e.target as HTMLInputElement | null
  const file = input?.files?.[0]
  imageError.value = ''

  if (!file) {
    imageDataUrl.value = ''
    return
  }

  if (!file.type.startsWith('image/')) {
    imageError.value = 'Please select an image file.'
    imageDataUrl.value = ''
    return
  }

  // Keep it small to avoid bloating localStorage.
  const maxBytes = 3 * 1024 * 1024
  if (file.size > maxBytes) {
    imageError.value = 'Image is too large. Please choose an image under 3MB.'
    imageDataUrl.value = ''
    return
  }

  try {
    const dataUrl = await readFileAsDataUrl(file)
    const { width, height } = await loadImageDimensions(dataUrl)

    if (width <= height) {
      imageError.value = 'Please use a landscape image.'
      imageDataUrl.value = ''
      return
    }

    imageDataUrl.value = dataUrl
  } catch (err) {
    imageError.value = err instanceof Error ? err.message : 'Could not process image.'
    imageDataUrl.value = ''
  }
}

function removeImage() {
  imageDataUrl.value = ''
  imageError.value = ''
}

function onSubmit() {
  if (!canSubmit.value) return
  submitError.value = ''

  const draft: CommunityDraft = {
    id: newCommunityId(),
    name: name.value.trim(),
    description: description.value.trim(),
    imageDataUrl: imageDataUrl.value || undefined,
    global: isGlobal.value,
    location: buildLocation(),
    createdAt: new Date().toISOString(),
  }

  try {
    saveCommunity(draft)
    submitted.value = true
  } catch (e) {
    submitError.value = e instanceof Error ? e.message : 'Could not save. Please try again.'
  }
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
            <h1 class="text-2xl font-semibold text-white">Create a community</h1>
            <p class="mt-2 text-sm text-slate-200">
              Start simple: name, description, and optional location. We’ll connect this to the backend later.
            </p>
          </div>
          <RouterLink class="text-sm text-slate-200 hover:text-white" to="/">Back</RouterLink>
        </div>

        <div v-if="submitted" class="mt-8 rounded-xl bg-emerald-500/10 p-4 ring-1 ring-emerald-400/20">
          <div class="text-sm font-semibold text-emerald-200">Community saved locally.</div>
          <div class="mt-1 text-sm text-slate-200">
            We stored it in your browser for now. Next step: publish it and invite admins.
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
              Create another
            </button>
          </div>
        </div>

        <form v-else class="mt-8 space-y-6" @submit.prevent="onSubmit">
          <div
            v-if="submitError"
            class="rounded-xl bg-rose-500/10 p-4 text-sm text-rose-200 ring-1 ring-rose-400/20"
          >
            {{ submitError }}
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-200">Name</label>
            <input
              v-model="name"
              type="text"
              class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="e.g. Santiago Bachata Community"
            />
            <p v-if="nameError" class="mt-2 text-xs text-rose-200">{{ nameError }}</p>
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-200">Description</label>
            <textarea
              v-model="description"
              rows="4"
              class="mt-2 w-full resize-none rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="What is this community about?"
            />
            <p v-if="descriptionError" class="mt-2 text-xs text-rose-200">{{ descriptionError }}</p>
          </div>

          <div class="rounded-2xl bg-slate-950/40 p-5 ring-1 ring-white/10">
            <div class="flex items-start justify-between gap-4">
              <div>
                <div class="text-sm font-semibold text-white">Image</div>
                <div class="mt-1 text-xs text-slate-300">Landscape image recommended (optional).</div>
              </div>
              <button
                v-if="imageDataUrl"
                type="button"
                class="rounded-xl bg-white/10 px-3 py-2 text-xs font-semibold text-white ring-1 ring-white/15 hover:bg-white/15"
                @click="removeImage"
              >
                Remove
              </button>
            </div>

            <div v-if="imageDataUrl" class="mt-4 overflow-hidden rounded-2xl ring-1 ring-white/10">
              <div class="relative w-full bg-slate-950/40" style="aspect-ratio: 4 / 3">
                <img :src="imageDataUrl" alt="Community image preview" class="h-full w-full object-cover" />
              </div>
            </div>

            <div class="mt-4">
              <input
                type="file"
                accept="image/*"
                class="block w-full text-sm text-slate-200 file:mr-4 file:rounded-xl file:border-0 file:bg-white/10 file:px-4 file:py-2 file:text-sm file:font-semibold file:text-white file:ring-1 file:ring-white/15 hover:file:bg-white/15"
                @change="onImageSelected"
              />
              <p v-if="imageError" class="mt-2 text-xs text-rose-200">{{ imageError }}</p>
            </div>
          </div>

          <div class="rounded-2xl bg-slate-950/40 p-5 ring-1 ring-white/10">
            <div class="flex items-center justify-between gap-4">
              <div>
                <div class="text-sm font-semibold text-white">Location</div>
                <div class="mt-1 text-xs text-slate-300">
                  Optional. Add country/region/city, or mark it as global.
                </div>
              </div>
              <label class="inline-flex items-center gap-3 text-sm text-slate-200">
                <input v-model="isGlobal" type="checkbox" class="h-4 w-4 rounded border-white/20 bg-white/10" />
                Global
              </label>
            </div>

            <div class="mt-5 grid gap-4 sm:grid-cols-2">
              <div data-country class="relative sm:col-span-2">
                <label class="block text-sm font-medium text-slate-200">Country (optional)</label>
                <div class="mt-2 flex gap-2">
                  <input
                    v-model="countryQuery"
                    :disabled="isGlobal"
                    type="text"
                    class="w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60 disabled:cursor-not-allowed disabled:opacity-50"
                    placeholder="Search country…"
                    @focus="countryOpen = true"
                    @keydown.down.prevent="countryOpen = true"
                  />
                  <button
                    class="rounded-xl bg-white/10 px-3 py-3 text-sm font-semibold text-white ring-1 ring-white/15 hover:bg-white/15 disabled:cursor-not-allowed disabled:opacity-50"
                    type="button"
                    :disabled="isGlobal"
                    @click="clearCountry"
                  >
                    Clear
                  </button>
                </div>

                <div
                  v-if="countryOpen && !isGlobal"
                  class="absolute z-10 mt-2 w-full overflow-hidden rounded-2xl bg-slate-950/95 ring-1 ring-white/10 backdrop-blur"
                >
                  <div class="max-h-56 overflow-auto p-2">
                    <button
                      v-for="c in filteredCountries"
                      :key="c.code"
                      type="button"
                      class="flex w-full items-center justify-between rounded-xl px-3 py-2 text-left text-sm text-slate-200 hover:bg-white/10"
                      @click="selectCountry(c)"
                    >
                      <span>{{ c.name }}</span>
                      <span class="text-xs text-slate-400">{{ c.code }}</span>
                    </button>
                    <div v-if="filteredCountries.length === 0" class="px-3 py-2 text-sm text-slate-400">
                      No matches.
                    </div>
                  </div>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-slate-200">Region / area (optional)</label>
                <input
                  v-model="region"
                  :disabled="isGlobal"
                  type="text"
                  class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60 disabled:cursor-not-allowed disabled:opacity-50"
                  placeholder="e.g. Metropolitana"
                />
              </div>

              <div>
                <label class="block text-sm font-medium text-slate-200">City (optional)</label>
                <input
                  v-model="city"
                  :disabled="isGlobal"
                  type="text"
                  class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60 disabled:cursor-not-allowed disabled:opacity-50"
                  placeholder="e.g. Santiago"
                />
              </div>
            </div>

            <p v-if="locationError" class="mt-3 text-xs text-rose-200">{{ locationError }}</p>
          </div>

          <button
            class="w-full rounded-xl px-4 py-3 text-sm font-semibold transition"
            :class="canSubmit ? 'bg-white text-slate-900 hover:bg-slate-100' : 'bg-white/10 text-slate-300 ring-1 ring-white/10'"
            type="submit"
            :disabled="!canSubmit"
          >
            Save community
          </button>

          <p class="text-xs text-slate-400">
            Saved locally under
            <code class="rounded bg-white/10 px-1.5 py-0.5 text-slate-200">latinVibe.communities</code>.
          </p>
        </form>
      </div>
    </section>
  </div>
</template>

