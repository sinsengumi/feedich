<template>
  <v-dialog v-model="innerDialogVisible" max-width="500px">
    <v-card>
      <v-card-title class="pb-0">
        <span class="headline">Add Feed</span>
      </v-card-title>
      <v-card-text class="pt-0 pl-2">
        <v-container grid-list-md class="mb-0 pb-0">
          <v-layout wrap>
            <v-flex xs12>
              <v-text-field hide-details v-model="discoverUrl" label="Site URL or Feed URL" @blur="discoverFeed"></v-text-field>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>

      <v-container fill-height v-if="loading">
        <v-progress-circular class="mx-auto" indeterminate :size="40" :width="4" color="blue"></v-progress-circular>
      </v-container>
      <v-container fill-height v-if="discoveredFeeds != null && discoveredFeeds.length === 0">
        <p class="mx-auto mb-0">Feed not discovered.</p>
      </v-container>
      <v-container fill-height v-if="error">
        <p class="mx-auto red--text mb-0">Error occured.</p>
      </v-container>

      <v-list two-line class="pl-3 pr-3">
        <template v-for="(feed, index) in discoveredFeeds">
          <v-list-tile @click="subscribe(feed)" :key="feed.title">
            <v-list-tile-content>
              <v-list-tile-title><img :src="feed.favicon" width="16" height="16" /> {{ feed.title }} ({{ feed.feedType }})</v-list-tile-title>
              <v-list-tile-sub-title>{{ feed.description }}</v-list-tile-sub-title>
            </v-list-tile-content>
            <v-list-tile-action>
              <v-list-tile-action-text><span v-if="feed.subscribed">Subscribed</span></v-list-tile-action-text>
            </v-list-tile-action>
          </v-list-tile>
          <v-divider :key="index"></v-divider>
        </template>
      </v-list>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue" flat @click.native="innerDialogVisible = false">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ApiClient from '../ApiClient'

export default {
  props: [
    'dialogVisible'
  ],
  data () {
    return {
      discoverUrl: null,
      discoveredFeeds: null,
      loading: false,
      error: false
    }
  },
  computed: {
    innerDialogVisible: {
      get: function () {
        return this.dialogVisible
      },
      set: function (value) {
        if (!value) {
          this.$emit('close')
        }
      }
    }
  },
  methods: {
    discoverFeed () {
      this.loading = true
      this.error = false
      this.discoveredFeeds = null

      const api = new ApiClient()
      api.discoverFeeds(this.discoverUrl)
        .then((response) => {
          this.discoveredFeeds = response.data
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
          this.error = true
          console.log(error)
        })
    },
    subscribe (feed) {
      this.$store.dispatch('SUBSCRIBE', {feedUrl: feed.feedUrl})
      this.innerDialogVisible = false
    }
  }
}
</script>

<style scoped>
</style>
