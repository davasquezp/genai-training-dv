<template>
  <div>
    <SiteHeader />

    <section class="mx-auto max-w-6xl px-4 pb-16 pt-10 sm:px-6 sm:pt-16">
      <div class="grid items-center gap-10 lg:grid-cols-2">
        <div>
          <p class="inline-flex items-center gap-2 rounded-full bg-white/5 px-3 py-1 text-xs text-slate-200 ring-1 ring-white/10">
            <span class="h-2 w-2 rounded-full bg-emerald-400" />
            Starting simple — growing step by step
          </p>
          <h1 class="mt-6 text-4xl font-semibold tracking-tight text-white sm:text-5xl">
            Your local dance community,
            <span class="bg-gradient-to-r from-fuchsia-300 via-indigo-300 to-cyan-300 bg-clip-text text-transparent">
              beautifully organized
            </span>.
          </h1>
          <p class="mt-5 text-base leading-relaxed text-slate-200 sm:text-lg">
            Discover events, find classes, meet dancers, and grow together. Next we’ll connect this to your API—starting
            with a simple interest flow.
          </p>

          <div class="mt-8 flex flex-col gap-3 sm:flex-row sm:items-center">
            <RouterLink
              class="inline-flex items-center justify-center rounded-xl bg-white px-5 py-3 text-sm font-semibold text-slate-900 hover:bg-slate-100"
              to="/interest"
            >
              Register interest
            </RouterLink>
            <RouterLink
              class="inline-flex items-center justify-center rounded-xl bg-white/10 px-5 py-3 text-sm font-semibold text-white ring-1 ring-white/15 hover:bg-white/15"
              to="/#features"
            >
              Explore features
            </RouterLink>
          </div>

          <div class="mt-8 flex flex-wrap gap-3 text-xs text-slate-300">
            <span class="rounded-full bg-white/5 px-3 py-1 ring-1 ring-white/10">Events</span>
            <span class="rounded-full bg-white/5 px-3 py-1 ring-1 ring-white/10">Studios</span>
            <span class="rounded-full bg-white/5 px-3 py-1 ring-1 ring-white/10">Levels</span>
            <span class="rounded-full bg-white/5 px-3 py-1 ring-1 ring-white/10">Socials</span>
          </div>
        </div>

        <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10 backdrop-blur">
          <div class="grid gap-4 sm:grid-cols-2">
            <div class="rounded-xl bg-slate-950/60 p-4 ring-1 ring-white/10">
              <div class="text-xs text-slate-300">Tonight</div>
              <div class="mt-1 text-sm font-semibold text-white">Salsa Social</div>
              <div class="mt-2 text-xs text-slate-300">8:00 PM • Downtown Studio</div>
            </div>
            <div class="rounded-xl bg-slate-950/60 p-4 ring-1 ring-white/10">
              <div class="text-xs text-slate-300">This weekend</div>
              <div class="mt-1 text-sm font-semibold text-white">Bachata Bootcamp</div>
              <div class="mt-2 text-xs text-slate-300">2 days • All levels</div>
            </div>
            <div class="rounded-xl bg-slate-950/60 p-4 ring-1 ring-white/10 sm:col-span-2">
              <div class="flex items-center justify-between">
                <div>
                  <div class="text-xs text-slate-300">Next step</div>
                  <div class="mt-1 text-sm font-semibold text-white">Interest page</div>
                </div>
                <span class="rounded-full bg-emerald-500/15 px-3 py-1 text-xs text-emerald-200 ring-1 ring-emerald-400/20">
                  New
                </span>
              </div>
              <div class="mt-3 text-xs text-slate-300">
                Collect preferences (lead/follow, country, styles) and store them locally for now.
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section id="features" class="mx-auto max-w-6xl px-4 pb-16 sm:px-6">
      <div class="grid gap-6 md:grid-cols-3">
        <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10">
          <div class="text-sm font-semibold text-white">Find events</div>
          <p class="mt-2 text-sm text-slate-200">
            One place for socials, workshops, and festivals—organized by style and level.
          </p>
        </div>
        <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10">
          <div class="text-sm font-semibold text-white">Track your progress</div>
          <p class="mt-2 text-sm text-slate-200">Save classes, note what you learned, and keep momentum.</p>
        </div>
        <div class="rounded-2xl bg-white/5 p-6 ring-1 ring-white/10">
          <div class="text-sm font-semibold text-white">Meet dancers</div>
          <p class="mt-2 text-sm text-slate-200">Build your circle: friends, partners, and mentors.</p>
        </div>
      </div>
    </section>

    <section id="how-it-works" class="mx-auto max-w-6xl px-4 pb-16 sm:px-6">
      <div class="rounded-2xl bg-gradient-to-br from-white/10 to-white/5 p-8 ring-1 ring-white/10">
        <h2 class="text-xl font-semibold text-white">How we’ll build this (step by step)</h2>
        <ol class="mt-6 grid gap-4 md:grid-cols-3">
          <li class="rounded-xl bg-slate-950/40 p-4 ring-1 ring-white/10">
            <div class="text-xs text-slate-300">Step 1</div>
            <div class="mt-1 text-sm font-semibold text-white">Landing page</div>
            <div class="mt-2 text-sm text-slate-200">You’re here. Clean UI, easy to extend.</div>
          </li>
          <li class="rounded-xl bg-slate-950/40 p-4 ring-1 ring-white/10">
            <div class="text-xs text-slate-300">Step 2</div>
            <div class="mt-1 text-sm font-semibold text-white">Interest page</div>
            <div class="mt-2 text-sm text-slate-200">Collect preferences and keep them for next steps.</div>
          </li>
          <li class="rounded-xl bg-slate-950/40 p-4 ring-1 ring-white/10">
            <div class="text-xs text-slate-300">Step 3</div>
            <div class="mt-1 text-sm font-semibold text-white">Wire the API</div>
            <div class="mt-2 text-sm text-slate-200">Persist interest and start listing events/studios.</div>
          </li>
        </ol>
      </div>
    </section>

    <section id="cta" class="mx-auto max-w-6xl px-4 pb-20 sm:px-6">
      <div class="flex flex-col items-start justify-between gap-6 rounded-2xl bg-white/5 p-8 ring-1 ring-white/10 md:flex-row md:items-center">
        <div>
          <h2 class="text-xl font-semibold text-white">Ready to join?</h2>
          <p class="mt-2 text-sm text-slate-200">Tell us your interests and we’ll tailor the community.</p>
        </div>
        <div class="flex gap-3">
          <RouterLink
            class="inline-flex items-center justify-center rounded-xl bg-white px-5 py-3 text-sm font-semibold text-slate-900 hover:bg-slate-100"
            to="/interest"
          >
            Register interest
          </RouterLink>
          <a
            class="inline-flex items-center justify-center rounded-xl bg-white/10 px-5 py-3 text-sm font-semibold text-white ring-1 ring-white/15 hover:bg-white/15"
            href="http://localhost:8080/swagger-ui/index.html"
            target="_blank"
            rel="noreferrer"
          >
            View API docs
          </a>
        </div>
      </div>
    </section>

    <footer class="mx-auto max-w-6xl px-4 pb-10 text-xs text-slate-400 sm:px-6">
      <div class="flex flex-col justify-between gap-3 border-t border-white/10 pt-6 sm:flex-row sm:items-center">
        <div>© {{ new Date().getFullYear() }} LatinVibe</div>
        <div class="flex gap-4">
          <RouterLink class="hover:text-slate-200" to="/#features">Features</RouterLink>
          <RouterLink class="hover:text-slate-200" to="/interest">Interest</RouterLink>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import SiteHeader from '../components/SiteHeader.vue'
</script>

