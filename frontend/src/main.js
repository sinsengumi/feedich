// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import App from './App'
import router from './router'
import store from './store'
/* eslint no-unused-vars: 0 */
import '@/assets/css/common.css'
import moment from 'moment'
import VueShortkey from 'vue-shortkey'
import axios from 'axios'

require('jquery/dist/jquery')
require('popper.js/dist/umd/popper')
require('bootstrap/dist/js/bootstrap')

Vue.config.productionTip = false

Vue.use(BootstrapVue)
Vue.use(VueShortkey, { prevent: ['input', 'textarea'] })

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

// axios settings
axios.defaults.withCredentials = true
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.data.status === 401) {
        location.href = '/#/login'
        return Promise.reject(error)
      }
      store.dispatch('SET_NOTIFY_MESSAGE', {dontUndo: true, message: '<span class="text-danger">' + error.response.data.message + '</span>'})
    } else if (error.request) {
      store.dispatch('SET_NOTIFY_MESSAGE', {dontUndo: true, message: '<span class="text-danger">ネットワークエラーが発生しました</span>'})
    } else {
      store.dispatch('SET_NOTIFY_MESSAGE', {dontUndo: true, message: '<span class="text-danger">原因不明のエラーが発生しました</span>'})
    }
    return Promise.reject(error)
  }
)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
