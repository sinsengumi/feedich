<template>
  <v-app>

    <layout-header />

    <div class="container-fluid">
      <div class="row">
        <div class="col-2 ">
          <layout-navigation />
        </div>
        <main class="col-10 bd-content">
          <router-view />
        </main>
      </div>
    </div>
    <!-- <v-content>
      <v-container fluid style="padding-top: 0">
        <router-view />
      </v-container>
    </v-content>

    <layout-footer /> -->
  </v-app>
</template>

<script>
import LayoutHeader from './user/LayoutHeader.vue'
import LayoutFooter from './user/LayoutFooter.vue'
import LayoutNavigation from './user/LayoutNavigation.vue'
import { mapActions } from 'vuex'
import * as action from '../store/action-types'

export default {
  components: {
    'layout-footer': LayoutFooter,
    'layout-header': LayoutHeader,
    'layout-navigation': LayoutNavigation
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
