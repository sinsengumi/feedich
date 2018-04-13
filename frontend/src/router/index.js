import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import Item from '@/components/Item'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/feeds/:feedId/items',
      name: 'Item',
      component: Item
    }
  ]
})
