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
import Index from '@/components/public/Index'
import Terms from '@/components/public/Terms'
import Policy from '@/components/public/Policy'
import Contact from '@/components/public/Contact'
import Help from '@/components/public/Help'
import NotFound from '@/components/public/NotFound'
import ApiClient from '../ApiClient'

const api = new ApiClient()

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      component: Root,
      children: [
        {
          path: '/',
          component: Public,
          children: [
            {
              path: '',
              name: 'Index',
              component: Index,
              meta: { title: 'Feedich', requiresAuth: false }
            },
            {
              path: 'help',
              name: 'Help',
              component: Help,
              meta: { title: 'ヘルプ | Feedich', requiresAuth: false }
            },
            {
              path: 'terms',
              name: 'Terms',
              component: Terms,
              meta: { title: '利用規約 | Feedich', requiresAuth: false }
            },
            {
              path: 'policy',
              name: 'Policy',
              component: Policy,
              meta: { title: 'プライバシーポリシー | Feedich', requiresAuth: false }
            },
            {
              path: 'contact',
              name: 'Contact',
              component: Contact,
              meta: { title: 'お問い合わせ | Feedich', requiresAuth: false }
            },
            {
              path: '404',
              name: 'NotFound',
              component: NotFound,
              meta: { title: '404 Not Found | Feedich', requiresAuth: false }
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
              meta: { title: '記事 | Feedich', requiresAuth: true }
            },
            {
              path: 'subscriptions',
              name: 'Subscriptions',
              component: Subscriptions,
              meta: { title: '購読フィード | Feedich', requiresAuth: true }
            },
            {
              path: 'pins',
              name: 'Pins',
              component: Pins,
              meta: { title: 'ピン | Feedich', requiresAuth: true }
            },
            {
              path: 'settings',
              name: 'Settings',
              component: Settings,
              meta: { title: '設定 | Feedich', requiresAuth: true }
            }
          ]
        }
      ]
    },
    {
      path: '*',
      redirect: { name: 'NotFound' }
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
      .catch(() => next({ path: '/' }))
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
