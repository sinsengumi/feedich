<template>
  <v-app>
    <layout-navigation />

    <layout-header />

    <v-content>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-content>

    <layout-footer />
  </v-app>
</template>

<script>
import LayoutHeader from './components/LayoutHeader.vue'
import LayoutFooter from './components/LayoutFooter.vue'
import LayoutNavigation from './components/LayoutNavigation.vue'
import { mapActions } from 'vuex'
import * as action from './store/action-types'

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

<style>
</style>
