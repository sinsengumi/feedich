import Vue from 'vue'
import Router from 'vue-router'
import Root from '@/components/Root'
import Public from '@/components/Public'
import User from '@/components/User'
import Dashboard from '@/components/user/Dashboard'
import Items from '@/components/user/Items'
import Subscriptions from '@/components/user/Subscriptions'
import Pins from '@/components/user/Pins'
import Settings from '@/components/user/Settings'
import Login from '@/components/public/Login'
import ApiClient from '../ApiClient'

const api = new ApiClient()

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Root',
      component: Root,
      children: [
        {
          path: '/',
          name: 'Public',
          component: Public,
          children: [
            {
              path: '',
              name: 'Index',
              redirect: '/dashboard'
            },
            {
              path: 'login',
              name: 'Login',
              component: Login,
              meta: { title: 'Login | Feedich', requiresAuth: false }
            }
          ]
        },
        {
          path: '/',
          name: 'User',
          component: User,
          children: [
            {
              path: 'dashboard',
              name: 'Dashboard',
              component: Dashboard,
              meta: { title: 'Feedich', requiresAuth: true }
            },
            {
              path: 'subscriptions/:subscriptionId/items',
              name: 'Items',
              component: Items,
              meta: { title: 'Items | Feedich', requiresAuth: true }
            },
            {
              path: 'subscriptions',
              name: 'Subscriptions',
              component: Subscriptions,
              meta: { title: 'Subscriptions | Feedich', requiresAuth: true }
            },
            {
              path: 'pins',
              name: 'Pins',
              component: Pins,
              meta: { title: 'Pins | Feedich', requiresAuth: true }
            },
            {
              path: 'settings',
              name: 'Settings',
              component: Settings,
              meta: { title: 'Settings | Feedich', requiresAuth: true }
            }
          ]
        }
      ]
    }
  ],
  scrollBehavior (to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    api.sessionValidate()
      .then(response => next())
      .catch(() => next({ path: '/login' }))
  } else {
    next()
  }
})

router.afterEach((to, from) => {
  if (to.meta && to.meta.title) {
    document.title = to.meta.title
  }
})

export default router
