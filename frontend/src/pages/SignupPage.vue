<script setup lang="ts">
import { computed, ref } from 'vue'

import SiteHeader from '../components/SiteHeader.vue'
import { RouterLink } from 'vue-router'
import { login, signup } from '../features/member/auth'

const email = ref('')
const name = ref('')
const phone = ref('')
const password = ref('')
const repeatPassword = ref('')
const showPassword = ref(false)

const submitted = ref(false)
const submitError = ref<string>('')
const submitting = ref(false)

const emailError = computed(() => (email.value.trim().length === 0 ? 'Email is required.' : ''))
const nameError = computed(() => (name.value.trim().length === 0 ? 'Name is required.' : ''))
const phoneError = computed(() => (phone.value.trim().length === 0 ? 'Phone is required.' : ''))
const passwordError = computed(() => (password.value.trim().length === 0 ? 'Password is required.' : ''))
const repeatPasswordError = computed(() => {
  if (repeatPassword.value.trim().length === 0) return 'Repeat password is required.'
  if (repeatPassword.value !== password.value) return 'Passwords do not match.'
  return ''
})

const canSubmit = computed(
  () => !emailError.value && !nameError.value && !phoneError.value && !passwordError.value && !repeatPasswordError.value,
)

async function onSubmit() {
  if (!canSubmit.value) return
  submitError.value = ''
  submitting.value = true
  try {
    await signup(email.value.trim(), name.value.trim(), phone.value.trim(), password.value)
    await login(email.value.trim(), password.value)
    submitted.value = true
  } catch (e) {
    submitError.value = e instanceof Error ? e.message : 'Could not submit. Please try again.'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-2xl px-4 pb-20 pt-8 sm:px-6 sm:pt-14">
      <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10 sm:p-8">
        <div class="flex items-start justify-between gap-4">
          <div>
            <h1 class="text-2xl font-semibold text-white">Create your account</h1>
            <p class="mt-2 text-sm text-slate-200">Start with an account. You can add dancer preferences later.</p>
          </div>
          <RouterLink class="text-sm text-slate-200 hover:text-white" to="/">Back</RouterLink>
        </div>

        <div v-if="submitted" class="mt-8 rounded-xl bg-emerald-500/10 p-4 ring-1 ring-emerald-400/20">
          <div class="text-sm font-semibold text-emerald-200">Welcome! Your account is ready.</div>
          <div class="mt-1 text-sm text-slate-200">You can now join communities and unlock dancer actions.</div>
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
          <div v-if="submitError" class="rounded-xl bg-rose-500/10 p-4 text-sm text-rose-200 ring-1 ring-rose-400/20">
            {{ submitError }}
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-200">Email</label>
            <input
              v-model="email"
              type="email"
              autocomplete="email"
              class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="you@example.com"
            />
            <p v-if="emailError" class="mt-2 text-xs text-rose-200">{{ emailError }}</p>
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-200">Name</label>
            <input
              v-model="name"
              type="text"
              autocomplete="name"
              class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="Daniel"
            />
            <p v-if="nameError" class="mt-2 text-xs text-rose-200">{{ nameError }}</p>
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-200">Phone</label>
            <input
              v-model="phone"
              type="tel"
              autocomplete="tel"
              class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="+56 9 1234 5678"
            />
            <p v-if="phoneError" class="mt-2 text-xs text-rose-200">{{ phoneError }}</p>
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-200">Password</label>
            <div class="relative mt-2">
              <input
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                autocomplete="new-password"
                class="w-full rounded-xl bg-slate-950/60 px-4 py-3 pr-12 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
                placeholder="Choose a password"
              />
              <button
                type="button"
                class="absolute inset-y-0 right-0 flex items-center px-4 text-slate-300 hover:text-white"
                :aria-label="showPassword ? 'Hide password' : 'Show password'"
                :title="showPassword ? 'Hide password' : 'Show password'"
                tabindex="-1"
                @click="showPassword = !showPassword"
              >
                <svg v-if="showPassword" viewBox="0 0 24 24" class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="2">
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M3 3l18 18M10.6 10.6a2 2 0 0 0 2.8 2.8M9.9 5.1A10.5 10.5 0 0 1 12 5c5.5 0 10 7 10 7a18.2 18.2 0 0 1-4.1 4.9M6.2 6.2C3.9 8 2 12 2 12s4.5 7 10 7c1.3 0 2.6-.2 3.8-.7"
                  />
                </svg>
                <svg v-else viewBox="0 0 24 24" class="h-5 w-5" fill="none" stroke="currentColor" stroke-width="2">
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M2 12s4.5-7 10-7 10 7 10 7-4.5 7-10 7-10-7-10-7Z"
                  />
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z" />
                </svg>
              </button>
            </div>
            <p v-if="passwordError" class="mt-2 text-xs text-rose-200">{{ passwordError }}</p>
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-200">Repeat password</label>
            <input
              v-model="repeatPassword"
              :type="showPassword ? 'text' : 'password'"
              autocomplete="new-password"
              class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="Repeat your password"
            />
            <p v-if="repeatPasswordError" class="mt-2 text-xs text-rose-200">{{ repeatPasswordError }}</p>
          </div>

          <div class="pt-2">
            <button
              class="w-full rounded-xl px-4 py-3 text-sm font-semibold transition"
              :class="
                canSubmit
                  ? 'bg-white text-slate-900 hover:bg-slate-100'
                  : 'bg-white/10 text-slate-300 ring-1 ring-white/10'
              "
              type="submit"
              :disabled="!canSubmit || submitting"
            >
              {{ submitting ? 'Creating…' : 'Create account' }}
            </button>
          </div>
        </form>
      </div>

      <div class="mt-6 text-center text-xs text-slate-400">
        Already have an account? <RouterLink to="/login" class="hover:text-white underline">Login</RouterLink>.
      </div>
    </section>
  </div>
</template>

