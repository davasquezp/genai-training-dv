import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

import LandingPage from '../pages/LandingPage.vue'
import InterestPage from '../pages/InterestPage.vue'

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'home', component: LandingPage },
  { path: '/interest', name: 'interest', component: InterestPage },
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to) {
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth' }
    }
    return { top: 0 }
  },
})

