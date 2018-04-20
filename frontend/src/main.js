// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuetify from 'vuetify'
import App from './App'
import router from './router'
import store from './store'
/* eslint no-unused-vars: 0 */
import toast from './toast'
import 'vuetify/dist/vuetify.min.css'
import '@/assets/css/common.css'
import moment from 'moment'

Vue.config.productionTip = false

Vue.use(Vuetify)

Vue.prototype.$eventHub = new Vue()

Vue.filter('format', function (value, format) {
  if (!value) return '-'
  if (!format) {
    format = 'YYYY'
  }
  return moment(value).format(format)
})
Vue.filter('fromNow', function (value) {
  if (!value) return '-'
  return moment(value).fromNow()
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>',
  created () {
    window.addEventListener('keyup', (event) => {
      if (event.keyCode === 13) {
        this.$eventHub.$emit('shortcutEnter')
      }
    })
  }
})
