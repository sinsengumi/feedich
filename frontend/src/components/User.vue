<template>
  <div>

    <layout-header />

    <div class="container-fluid">
      <div class="row">
        <nav class="sidebar d-md-block">
          <layout-navigation />
        </nav>
        <main class="main ml-sm-auto">
          <subscribe-modal></subscribe-modal>
          <div class="content">
            <router-view />
          </div>
        </main>
      </div>
    </div>

    <layout-footer />
  </div>
</template>

<script>
import LayoutHeader from './user/LayoutHeader.vue'
import LayoutFooter from './user/LayoutFooter.vue'
import LayoutNavigation from './user/LayoutNavigation.vue'
import SubscribeModal from './user/SubscribeModal'
import { mapActions } from 'vuex'
import * as action from '../store/action-types'

export default {
  components: {
    'layout-footer': LayoutFooter,
    'layout-header': LayoutHeader,
    'layout-navigation': LayoutNavigation,
    'subscribe-modal': SubscribeModal
  },
  created () {
    this.GET_PINS()
    this.GET_SUBSCRIPTIONS()
  },
  methods: {
    ...mapActions([
      action.GET_SUBSCRIPTIONS,
      action.GET_PINS
    ])
  }
}
</script>
