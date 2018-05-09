import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import Items from '@/components/Items'
import Subscriptions from '@/components/Subscriptions'
import Pins from '@/components/Pins'
import Settings from '@/components/Settings'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index,
      meta: { title: 'Feedich' }
    },
    {
      path: '/subscriptions/:subscriptionId/items',
      name: 'Items',
      component: Items,
      meta: { title: 'Items | Feedich' }
    },
    {
      path: '/subscriptions',
      name: 'Subscriptions',
      component: Subscriptions,
      meta: { title: 'Subscriptions | Feedich' }
    },
    {
      path: '/pins',
      name: 'Pins',
      component: Pins,
      meta: { title: 'Pins | Feedich' }
    },
    {
      path: '/settings',
      name: 'Settings',
      component: Settings,
      meta: { title: 'Settings | Feedich' }
    }
  ],
  scrollBehavior (to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
})

router.afterEach((to, from) => {
  if (to.meta && to.meta.title) {
    document.title = to.meta.title
  }
})

export default router
