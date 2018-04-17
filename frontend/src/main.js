// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuetify from 'vuetify'
import App from './App'
import router from './router'
import 'vuetify/dist/vuetify.min.css'
import '@/assets/css/common.css'

Vue.config.productionTip = false

Vue.use(Vuetify)

Vue.prototype.$eventHub = new Vue()

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
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