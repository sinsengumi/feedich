import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import Item from '@/components/Item'
import Subscriptions from '@/components/Subscriptions'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/subscriptions/:subscriptionId/items',
      name: 'Item',
      component: Item
    },
    {
      path: '/subscriptions',
      name: 'Subscriptions',
      component: Subscriptions
    }
  ]
})