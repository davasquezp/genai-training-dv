import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

import LandingPage from '../pages/LandingPage.vue'
import InterestPage from '../pages/InterestPage.vue'
import CreateCommunityPage from '../pages/CreateCommunityPage.vue'
import CommunitiesPage from '../pages/CommunitiesPage.vue'
import CommunityPage from '../pages/CommunityPage.vue'

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'home', component: LandingPage },
  { path: '/interest', name: 'interest', component: InterestPage },
  { path: '/communities', name: 'communities', component: CommunitiesPage },
  { path: '/communities/new', name: 'community-create', component: CreateCommunityPage },
  { path: '/communities/:id', name: 'community', component: CommunityPage },
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

