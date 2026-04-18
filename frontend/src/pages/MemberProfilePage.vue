<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-4xl px-4 pb-20 pt-10 sm:px-6 sm:pt-16">
      <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10 sm:p-8">
        <div class="flex flex-wrap items-start justify-between gap-4">
          <div>
            <h1 class="text-2xl font-semibold text-white">My profile</h1>
            <p class="mt-2 text-sm text-slate-200">Your account and dancer profile.</p>
          </div>
          <RouterLink class="text-sm text-slate-200 hover:text-white" to="/">Back</RouterLink>
        </div>

        <div v-if="error" class="mt-6 rounded-xl bg-rose-500/10 p-4 text-sm text-rose-200 ring-1 ring-rose-400/20">
          {{ error }}
        </div>

        <div v-if="loading" class="mt-6 text-sm text-slate-300">Loading…</div>

        <div v-else class="mt-6">
          <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10">
            <div class="text-sm font-semibold text-white">Account</div>
            <p class="mt-1 text-sm text-slate-200">Your login information.</p>

            <div class="mt-5 grid gap-4 sm:grid-cols-2">
              <div>
                <label class="block text-sm font-medium text-slate-200">Email</label>
                <input
                  :value="member?.email ?? ''"
                  type="email"
                  class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-slate-300 ring-1 ring-white/10"
                  readonly
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-slate-200">Phone</label>
                <input
                  :value="member?.phone ?? ''"
                  type="tel"
                  class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-slate-300 ring-1 ring-white/10"
                  readonly
                />
              </div>
            </div>
          </div>

          <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10">
            <div class="text-sm font-semibold text-white">Profile</div>
            <p class="mt-1 text-sm text-slate-200">Your account preferences and active roles.</p>

            <div v-if="activeRoles.length > 0" class="mt-4 flex flex-wrap gap-2">
              <span
                v-for="r in activeRoles"
                :key="r"
                class="rounded-full bg-slate-950/40 px-3 py-1 text-xs text-slate-200 ring-1 ring-white/10"
              >
                {{ r }}
              </span>
            </div>
            <div v-else class="mt-4 rounded-xl bg-slate-950/40 p-4 text-sm text-slate-300 ring-1 ring-white/10">
              You don’t have any active roles yet.
            </div>

            <div class="mt-5 flex flex-wrap items-center gap-3">
              <button
                v-if="!hasDancerRole"
                class="inline-flex items-center justify-center rounded-xl bg-white px-5 py-3 text-sm font-semibold text-slate-900 hover:bg-slate-100 disabled:cursor-not-allowed disabled:opacity-60"
                :disabled="activating"
                @click="activateDancer"
              >
                {{ activating ? 'Activating…' : 'I am a dancer' }}
              </button>

              <div
                v-if="activatedMessage"
                class="rounded-xl bg-emerald-500/10 px-4 py-3 text-sm text-emerald-200 ring-1 ring-emerald-400/20"
              >
                {{ activatedMessage }}
              </div>
            </div>

            <div class="mt-6 grid gap-4 sm:grid-cols-2">
              <div>
                <label class="block text-sm font-medium text-slate-200">Name</label>
                <input
                  v-model="name"
                  type="text"
                  class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
                  placeholder="How should we call you?"
                />
                <p v-if="name.trim().length === 0" class="mt-2 text-xs text-rose-200">Name is required.</p>
              </div>

              <div class="relative">
                <label class="block text-sm font-medium text-slate-200">Country</label>
                <div class="mt-2 flex gap-2">
                  <input
                    v-model="countryQuery"
                    type="text"
                    class="w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
                    placeholder="Search country…"
                    @focus="countryOpen = true"
                    @keydown.down.prevent="countryOpen = true"
                  />
                  <button
                    class="rounded-xl bg-white/10 px-3 py-3 text-sm font-semibold text-white ring-1 ring-white/15 hover:bg-white/15 disabled:cursor-not-allowed disabled:opacity-60"
                    type="button"
                    :disabled="!country"
                    @click="clearCountry"
                  >
                    Clear
                  </button>
                </div>

                <div
                  v-if="countryOpen"
                  class="absolute z-10 mt-2 w-full overflow-hidden rounded-2xl bg-slate-950/95 ring-1 ring-white/10 backdrop-blur"
                >
                  <div class="max-h-56 overflow-auto p-2">
                    <button
                      v-for="c in filteredCountries(countryQuery)"
                      :key="c.code"
                      type="button"
                      class="flex w-full items-center justify-between rounded-xl px-3 py-2 text-left text-sm text-slate-200 hover:bg-white/10"
                      @click="selectCountry(c)"
                    >
                      <span>{{ c.name }}</span>
                      <span class="text-xs text-slate-400">{{ c.code }}</span>
                    </button>
                    <div v-if="filteredCountries(countryQuery).length === 0" class="px-3 py-2 text-sm text-slate-400">No matches.</div>
                  </div>
                </div>
              </div>

              <div class="relative">
                <label class="block text-sm font-medium text-slate-200">Nationality</label>
                <div class="mt-2 flex gap-2">
                  <input
                    v-model="nationalityQuery"
                    type="text"
                    class="w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
                    placeholder="Search nationality…"
                    @focus="nationalityOpen = true"
                    @keydown.down.prevent="nationalityOpen = true"
                  />
                  <button
                    class="rounded-xl bg-white/10 px-3 py-3 text-sm font-semibold text-white ring-1 ring-white/15 hover:bg-white/15 disabled:cursor-not-allowed disabled:opacity-60"
                    type="button"
                    :disabled="!nationality"
                    @click="clearNationality"
                  >
                    Clear
                  </button>
                </div>

                <div
                  v-if="nationalityOpen"
                  class="absolute z-10 mt-2 w-full overflow-hidden rounded-2xl bg-slate-950/95 ring-1 ring-white/10 backdrop-blur"
                >
                  <div class="max-h-56 overflow-auto p-2">
                    <button
                      v-for="c in filteredCountries(nationalityQuery)"
                      :key="c.code"
                      type="button"
                      class="flex w-full items-center justify-between rounded-xl px-3 py-2 text-left text-sm text-slate-200 hover:bg-white/10"
                      @click="selectNationality(c)"
                    >
                      <span>{{ c.name }}</span>
                      <span class="text-xs text-slate-400">{{ c.code }}</span>
                    </button>
                    <div
                      v-if="filteredCountries(nationalityQuery).length === 0"
                      class="px-3 py-2 text-sm text-slate-400"
                    >
                      No matches.
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <button
              class="mt-4 w-full rounded-xl px-4 py-3 text-sm font-semibold transition"
              :class="
                profileDirty
                  ? 'bg-white text-slate-900 hover:bg-slate-100'
                  : 'bg-white/10 text-slate-300 ring-1 ring-white/10'
              "
              type="button"
              :disabled="!profileDirty || profileSaving"
              @click="saveProfile"
            >
              {{ profileSaving ? 'Saving…' : 'Save profile' }}
            </button>
          </div>

          <div v-if="hasDancerRole" class="mt-6 rounded-2xl bg-white/5 p-6 ring-1 ring-white/10">
            <div class="text-sm font-semibold text-white">Dancer</div>
            <p class="mt-1 text-sm text-slate-200">Your dancer profile.</p>

            <div class="mt-4">
              <div v-if="dancerLoading" class="text-sm text-slate-300">Loading dancer profile…</div>

              <div
                v-else-if="dancerNotFound"
                class="rounded-xl bg-white/5 p-4 text-sm text-slate-200 ring-1 ring-white/10"
              >
                <div>Your dancer profile is not yet available</div>
                <button
                  v-if="hasDancerRole"
                  class="mt-4 inline-flex items-center justify-center rounded-xl bg-white/10 px-4 py-2 text-sm font-semibold text-white ring-1 ring-white/15 hover:bg-white/15 disabled:cursor-not-allowed disabled:opacity-60"
                  :disabled="dancerLoading"
                  @click="loadDancer"
                >
                  Refresh
                </button>
              </div>

              <form v-else class="space-y-4" @submit.prevent="saveDancer">
                <div>
                  <label class="block text-sm font-medium text-slate-200">Your dancer name</label>
                  <input
                    v-model="dancerName"
                    type="text"
                    class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
                    placeholder="Your dancer name"
                  />
                </div>

                <div>
                  <label class="block text-sm font-medium text-slate-200">Dancer role</label>
                  <div class="mt-2 flex flex-wrap gap-2">
                    <button
                      type="button"
                      class="rounded-xl px-4 py-2 text-sm font-semibold ring-1 transition"
                      :class="
                        leadSelected
                          ? 'bg-white text-slate-900 ring-white/30'
                          : 'bg-white/10 text-white ring-white/15 hover:bg-white/15'
                      "
                      @click="toggleLead"
                    >
                      Lead
                    </button>
                    <button
                      type="button"
                      class="rounded-xl px-4 py-2 text-sm font-semibold ring-1 transition"
                      :class="
                        followerSelected
                          ? 'bg-white text-slate-900 ring-white/30'
                          : 'bg-white/10 text-white ring-white/15 hover:bg-white/15'
                      "
                      @click="toggleFollower"
                    >
                      Follower
                    </button>
                  </div>
                </div>

                <button
                  class="w-full rounded-xl px-4 py-3 text-sm font-semibold transition"
                  :class="dirty ? 'bg-white text-slate-900 hover:bg-slate-100' : 'bg-white/10 text-slate-300 ring-1 ring-white/10'"
                  type="submit"
                  :disabled="!dirty || dancerSaving"
                >
                  {{ dancerSaving ? 'Saving…' : 'Save' }}
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'

import SiteHeader from '../components/SiteHeader.vue'
import { updateMyDancer } from '../features/dancer/api'
import { getMemberProfilePage } from '../features/pages/memberProfile/api'
import { authHeader, cacheMember, fetchMe, getCachedMember, hasRole, isAuthenticated, setToken, updateMyProfile } from '../features/member/auth'
import { COUNTRIES, type Country } from '../data/countries'

type DancerMe = {
  id: string
  name: string
  dancerRoles: string[]
}

const router = useRouter()

const loading = ref(true)
const error = ref('')
const member = ref(getCachedMember())

const activating = ref(false)
const activatedMessage = ref('')

const dancerLoading = ref(false)
const dancerSaving = ref(false)
const dancerNotFound = ref(false)
const dancer = ref<DancerMe | null>(null)

const country = ref<Country | null>(null)
const nationality = ref<Country | null>(null)
const name = ref('')
const countryQuery = ref('')
const nationalityQuery = ref('')
const countryOpen = ref(false)
const nationalityOpen = ref(false)
const leadSelected = ref(false)
const followerSelected = ref(false)
const dancerName = ref('')

const dancerRoles = computed<string[]>(() => {
  const roles: string[] = []
  if (leadSelected.value) roles.push('LEAD')
  if (followerSelected.value) roles.push('FOLLOWER')
  return roles
})

function normalizedRoles(raw: string[] | null | undefined): string[] {
  const src = Array.isArray(raw) ? raw : []
  const allowed = new Set(['LEAD', 'FOLLOWER'])
  return Array.from(
    new Set(src.map(r => String(r).toUpperCase().trim()).filter(r => allowed.has(r))),
  ).sort()
}

const initialSnapshot = ref({ roles: [] as string[], name: '' })

const profileInitialSnapshot = ref({
  name: '',
  countryCode: '',
  countryName: '',
  nationalityCode: '',
  nationalityName: '',
})

const profileSaving = ref(false)

const hasDancerRole = computed(() => hasRole('DANCER'))
const activeRoles = computed(() => (member.value?.roles ?? []).filter(r => r !== 'NONE'))

function apiBaseUrl(): string {
  const raw = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  return raw && raw.length > 0 ? raw.replace(/\/+$/, '') : ''
}

function filteredCountries(query: string): Country[] {
  const q = query.trim().toLowerCase()
  if (!q) return COUNTRIES
  return COUNTRIES.filter(c => `${c.name} ${c.code}`.toLowerCase().includes(q))
}

function selectCountry(c: Country) {
  country.value = c
  countryQuery.value = c.name
  countryOpen.value = false
  nationalityOpen.value = false
}

function clearCountry() {
  country.value = null
  countryQuery.value = ''
  countryOpen.value = false
}

function selectNationality(c: Country) {
  nationality.value = c
  nationalityQuery.value = c.name
  nationalityOpen.value = false
  countryOpen.value = false
}

function clearNationality() {
  nationality.value = null
  nationalityQuery.value = ''
  nationalityOpen.value = false
}

async function activateDancer() {
  activating.value = true
  error.value = ''
  try {
    const base = apiBaseUrl()
    const resp = await fetch(`${base}/api/members/me/roles`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', ...authHeader() },
      body: JSON.stringify({ role: 'DANCER' }),
    })
    if (!resp.ok) {
      const text = await resp.text().catch(() => '')
      throw new Error(text || `Activation failed (${resp.status})`)
    }
    const data = (await resp.json()) as any
    if (data?.token) setToken(String(data.token))
    const updatedMember = data?.member ?? data
    cacheMember(updatedMember)
    member.value = updatedMember
    activatedMessage.value = 'Now you are a dancer!'

    // Give the backend a moment to create the dancer profile, then refresh.
    await new Promise(resolve => setTimeout(resolve, 1000))
    await loadDancer()
  } catch (e: any) {
    error.value = e?.message || 'Could not activate dancer'
  } finally {
    activating.value = false
  }
}

async function loadDancer() {
  dancerLoading.value = true
  dancerNotFound.value = false
  error.value = ''
  try {
    const page = await getMemberProfilePage()
    member.value = page.member
    cacheMember(page.member)
    if (!page.dancer) {
      dancer.value = null
      dancerNotFound.value = true
      return
    }
    const data = page.dancer
    dancer.value = { id: data.id, name: data.name, dancerRoles: data.dancerRoles }
    dancerName.value = data.name ?? ''
    leadSelected.value = Array.isArray(data.dancerRoles) && data.dancerRoles.includes('LEAD')
    followerSelected.value = Array.isArray(data.dancerRoles) && data.dancerRoles.includes('FOLLOWER')
    initialSnapshot.value = { roles: normalizedRoles(data.dancerRoles), name: dancerName.value }
  } catch (e: any) {
    error.value = e?.message || 'Could not load dancer profile'
  } finally {
    dancerLoading.value = false
  }
}

function toggleLead() {
  leadSelected.value = !leadSelected.value
}

function toggleFollower() {
  followerSelected.value = !followerSelected.value
}

const dirty = computed(() => {
  const current = normalizedRoles(dancerRoles.value)
  const initial = normalizedRoles(initialSnapshot.value.roles)
  if (dancerName.value.trim() !== initialSnapshot.value.name.trim()) return true
  if (current.length !== initial.length) return true
  return current.some((r, i) => r !== initial[i])
})

const profileDirty = computed(() => {
  return (
    (name.value ?? '') !== profileInitialSnapshot.value.name ||
    (country.value?.code ?? '') !== profileInitialSnapshot.value.countryCode ||
    (country.value?.name ?? '') !== profileInitialSnapshot.value.countryName ||
    (nationality.value?.code ?? '') !== profileInitialSnapshot.value.nationalityCode ||
    (nationality.value?.name ?? '') !== profileInitialSnapshot.value.nationalityName
  )
})

async function saveProfile() {
  if (!profileDirty.value) return
  profileSaving.value = true
  error.value = ''
  try {
    const updated = await updateMyProfile({
      name: name.value.trim(),
      country: country.value ? { code: country.value.code, name: country.value.name } : null,
      nationality: nationality.value ? { code: nationality.value.code, name: nationality.value.name } : null,
    })
    member.value = updated
    profileInitialSnapshot.value = {
      name: name.value,
      countryCode: country.value?.code ?? '',
      countryName: country.value?.name ?? '',
      nationalityCode: nationality.value?.code ?? '',
      nationalityName: nationality.value?.name ?? '',
    }
  } catch (e: any) {
    error.value = e?.message || 'Could not save profile'
  } finally {
    profileSaving.value = false
  }
}

async function saveDancer() {
  if (!dirty.value) return
  dancerSaving.value = true
  error.value = ''
  try {
    const data = await updateMyDancer({
      name: dancerName.value.trim(),
      dancerRoles: dancerRoles.value,
    })
    dancer.value = data
    dancerName.value = data.name ?? ''
    leadSelected.value = Array.isArray(data.dancerRoles) && data.dancerRoles.includes('LEAD')
    followerSelected.value = Array.isArray(data.dancerRoles) && data.dancerRoles.includes('FOLLOWER')
    initialSnapshot.value = { roles: normalizedRoles(data.dancerRoles), name: dancerName.value }
  } catch (e: any) {
    error.value = e?.message || 'Could not save dancer profile'
  } finally {
    dancerSaving.value = false
  }
}

onMounted(async () => {
  if (!isAuthenticated()) {
    await router.push('/login')
    return
  }
  try {
    member.value = await fetchMe()
  } catch {
    // ignore; page still uses cached member if any
  } finally {
    loading.value = false
  }
  country.value = member.value?.countryCode
    ? { code: member.value.countryCode, name: member.value.countryName ?? '' }
    : null
  nationality.value = member.value?.nationalityCode
    ? { code: member.value.nationalityCode, name: member.value.nationalityName ?? '' }
    : null
  name.value = (member.value?.name ?? '').trim()
  countryQuery.value = country.value ? country.value.name : ''
  nationalityQuery.value = nationality.value ? nationality.value.name : ''
  profileInitialSnapshot.value = {
    name: name.value,
    countryCode: country.value?.code ?? '',
    countryName: country.value?.name ?? '',
    nationalityCode: nationality.value?.code ?? '',
    nationalityName: nationality.value?.name ?? '',
  }
  await loadDancer()
})
</script>

