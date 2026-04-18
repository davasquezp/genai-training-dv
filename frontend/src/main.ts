import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import { restoreSession } from './features/member/auth'
import { router } from './router'

async function boot() {
  await restoreSession()
  const app = createApp(App)
  app.use(router)
  await router.isReady()
  app.mount('#app')
}

boot()
