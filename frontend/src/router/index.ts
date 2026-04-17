import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

import LandingPage from '../pages/LandingPage.vue'
import SignupPage from '../pages/SignupPage.vue'
import CreateCommunityPage from '../pages/CreateCommunityPage.vue'
import CommunitiesPage from '../pages/CommunitiesPage.vue'
import CommunityPage from '../pages/CommunityPage.vue'
import DancersPage from '../pages/DancersPage.vue'
import DancerPage from '../pages/DancerPage.vue'
import LoginPage from '../pages/LoginPage.vue'
import MemberProfilePage from '../pages/MemberProfilePage.vue'

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'home', component: LandingPage },
  { path: '/signup', name: 'signup', component: SignupPage },
  { path: '/login', name: 'login', component: LoginPage },
  { path: '/me', name: 'me', component: MemberProfilePage },
  { path: '/communities', name: 'communities', component: CommunitiesPage },
  { path: '/communities/new', name: 'community-create', component: CreateCommunityPage },
  { path: '/communities/:id', name: 'community', component: CommunityPage },
  { path: '/dancers', name: 'dancers', component: DancersPage },
  { path: '/dancers/:id', name: 'dancer', component: DancerPage },
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

