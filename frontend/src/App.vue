<template>
  <div class="min-h-full bg-slate-950">
    <div class="pointer-events-none fixed inset-0 overflow-hidden">
      <div
        class="absolute -top-24 left-1/2 h-[520px] w-[820px] -translate-x-1/2 rounded-full bg-gradient-to-r from-fuchsia-500/20 via-indigo-500/20 to-cyan-500/20 blur-3xl"
      />
    </div>

    <main class="relative">
      <RouterView />
    </main>
  </div>
</template>
