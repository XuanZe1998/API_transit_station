import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Auth from '../views/Auth.vue'
import ModelMarket from '../views/ModelMarket.vue'
import UserLayout from '../components/UserLayout.vue'
import AdminLayout from '../components/AdminLayout.vue'

// Admin Views
import Channels from '../views/Channels.vue'
import AdminTokens from '../views/Tokens.vue'
import ModelMappings from '../views/ModelMappings.vue'

// User Views
import UserConsole from '../views/UserConsole.vue'
import UserDashboard from '../views/UserDashboard.vue'
import UserTokens from '../views/UserTokens.vue'
import UserLogs from '../views/UserLogs.vue'
import UserWallet from '../views/UserWallet.vue'
import UserApiReference from '../views/UserApiReference.vue'

const routes = [
  {
    path: '/',
    component: UserLayout,
    children: [
      { path: '', component: Home },
      { path: 'market', component: ModelMarket },
      { path: 'login', component: Auth },
      { path: 'register', component: Auth },
      {
        path: 'console',
        component: UserConsole,
        meta: { requiresAuth: true },
        children: [
          { path: '', redirect: '/console/dashboard' },
          { path: 'dashboard', component: UserDashboard },
          { path: 'reference', component: UserApiReference },
          { path: 'tokens', component: UserTokens },
          { path: 'logs', component: UserLogs },
          { path: 'wallet', component: UserWallet }
        ]
      }
    ]
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAdmin: true },
    children: [
      { path: '', redirect: '/admin/channels' },
      { path: 'channels', component: Channels },
      { path: 'tokens', component: AdminTokens },
      { path: 'mappings', component: ModelMappings }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userStr = localStorage.getItem('user')
  const user = userStr ? JSON.parse(userStr) : null

  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.requiresAdmin && (!token || user?.role !== 'ADMIN')) {
    next('/')
  } else {
    next()
  }
})

export default router
