<template>
  <v-app id="inspire">
    <v-navigation-drawer app fixed :clipped="$vuetify.breakpoint.lgAndUp">
      <v-list dense subheader>
        <v-subheader>Subscriptions</v-subheader>
        <template v-for="s in subscriptions">
          <v-list-tile ripple @click="fetchFeed(s.feed.id)" :key="s.id">
            <v-list-tile-action style="min-width: 25px">
              <img v-bind:src="s.feed.favicon" width="16" height="16" />
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title>
                <span v-bind:title="s.feed.title">{{ s.feed.title }}</span>
              </v-list-tile-title>
            </v-list-tile-content>
            <v-list-tile-action>
              <v-chip disabled text-color="white" color="light-blue" style="font-size:10px; height: 16px">7</v-chip>
            </v-list-tile-action>
          </v-list-tile>
          <v-divider />
        </template>
      </v-list>
    </v-navigation-drawer>

    <v-toolbar app dark fixed color="light-blue darken-1"
      :clipped-left="$vuetify.breakpoint.lgAndUp" height="54"
      class="elevation-1">
      <v-toolbar-title>
        <v-toolbar-side-icon>
          <v-icon>rss_feed</v-icon>
        </v-toolbar-side-icon>
        <span>Feedich</span>
      </v-toolbar-title>
      <v-spacer />
      <v-btn icon>
        <v-icon>settings</v-icon>
      </v-btn>
    </v-toolbar>

    <v-content>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import ApiClient from './ApiClient'

export default {
  data () {
    return {
      subscriptions: []
    }
  },
  created () {
    const api = new ApiClient()
    api.getSubscriptions(
      (response) => (this.subscriptions = response.data),
      (response) => console.log('=====================')
    )
  },
  methods: {
    fetchFeed (feedId, event) {
      this.$router.push({name: 'Item', params: { feedId }})
    }
  }
}
</script>

<style>
</style>
