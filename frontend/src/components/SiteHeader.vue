<template>
  <header class="relative">
    <nav class="mx-auto flex max-w-6xl items-center justify-between px-4 py-5 sm:px-6">
      <RouterLink class="flex items-center gap-3" to="/" aria-label="LatinVibe home">
        <div
          class="grid h-9 w-9 place-items-center rounded-xl bg-white/10 ring-1 ring-white/15 backdrop-blur"
          aria-hidden="true"
        >
          <span class="text-lg font-semibold">LV</span>
        </div>
        <div class="leading-tight">
          <div class="text-sm font-semibold text-white">LatinVibe</div>
          <div class="text-xs text-slate-300">Community • Classes • Events</div>
        </div>
      </RouterLink>

      <div class="hidden items-center gap-6 text-sm text-slate-200 sm:flex">
        <RouterLink class="hover:text-white" to="/#features">Features</RouterLink>
        <RouterLink class="hover:text-white" to="/communities">Communities</RouterLink>
        <RouterLink class="hover:text-white" to="/dancers">Dancers</RouterLink>
        <RouterLink v-if="loggedIn" class="hover:text-white" to="/me">My profile</RouterLink>
        <RouterLink v-if="!loggedIn" class="hover:text-white" to="/login">Login</RouterLink>
        <button v-else class="hover:text-white" type="button" @click="onLogout">Log out</button>
        <RouterLink
          v-if="!loggedIn"
          class="rounded-xl bg-white/10 px-3 py-2 font-medium text-white ring-1 ring-white/15 hover:bg-white/15"
          to="/signup"
        >
          Join!
        </RouterLink>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'

import { authState, logout } from '../features/member/auth'

const router = useRouter()
const loggedIn = computed(() => authState.token.length > 0)

async function onLogout() {
  await logout()
  await router.push('/')
}
</script>
