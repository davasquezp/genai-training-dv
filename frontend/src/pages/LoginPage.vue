<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-md px-4 pb-20 pt-10 sm:px-6 sm:pt-16">
      <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10 sm:p-8">
        <div class="flex items-start justify-between gap-4">
          <div>
            <h1 class="text-2xl font-semibold text-white">Login</h1>
            <p class="mt-2 text-sm text-slate-200">Welcome back.</p>
          </div>
          <RouterLink class="text-sm text-slate-200 hover:text-white" to="/">Back</RouterLink>
        </div>

        <div
          v-if="error"
          class="mt-6 rounded-xl bg-rose-500/10 p-4 text-sm text-rose-200 ring-1 ring-rose-400/20"
        >
          {{ error }}
        </div>

        <form class="mt-6 space-y-4" @submit.prevent="onSubmit">
          <div>
            <label class="block text-sm font-medium text-slate-200">Email</label>
            <input
              v-model="email"
              type="email"
              autocomplete="email"
              class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="you@example.com"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-200">Password</label>
            <input
              v-model="password"
              type="password"
              autocomplete="current-password"
              class="mt-2 w-full rounded-xl bg-slate-950/60 px-4 py-3 text-sm text-white ring-1 ring-white/10 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-400/60"
              placeholder="Your password"
            />
          </div>

          <button
            class="w-full rounded-xl px-4 py-3 text-sm font-semibold transition"
            :class="canSubmit ? 'bg-white text-slate-900 hover:bg-slate-100' : 'bg-white/10 text-slate-300 ring-1 ring-white/10'"
            type="submit"
            :disabled="!canSubmit || submitting"
          >
            {{ submitting ? 'Logging in…' : 'Login' }}
          </button>

          <p class="text-xs text-slate-400">
            New here?
            <RouterLink class="text-slate-200 underline hover:text-white" to="/signup">Create an account</RouterLink>.
          </p>
        </form>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'

import SiteHeader from '../components/SiteHeader.vue'
import { login } from '../features/member/auth'

const router = useRouter()

const email = ref('')
const password = ref('')
const error = ref('')
const submitting = ref(false)

const canSubmit = computed(() => email.value.trim().length > 0 && password.value.trim().length > 0)

async function onSubmit() {
  if (!canSubmit.value) return
  error.value = ''
  submitting.value = true
  try {
    await login(email.value.trim(), password.value)
    await router.push('/communities')
  } catch (e: any) {
    error.value = e?.message || 'Login failed'
  } finally {
    submitting.value = false
  }
}
</script>

