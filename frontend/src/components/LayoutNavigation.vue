<template>

  <v-navigation-drawer app fixed :clipped="$vuetify.breakpoint.lgAndUp">

    <v-layout>
      <v-flex xs12>
        <v-card class="elevation-1">
          <v-btn flat small @click="fetchSubscriptions" class="ml-1 mr-0">
            <v-icon small class="mr-1">autorenew</v-icon>Refresh
          </v-btn>

          <v-dialog v-model="addFeedDialog" persistent max-width="500px">
            <v-btn flat small class="ml-0 mr-0" slot="activator">
              <v-icon small class="mr-1">add_circle_outline</v-icon>Add Feed
            </v-btn>
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

              <v-container fill-height v-if="discoverLoading">
                <v-progress-circular class="mx-auto" indeterminate :size="40" :width="4" color="blue"></v-progress-circular>
              </v-container>
              <v-container fill-height v-if="discoveredFeeds != null && discoveredFeeds.length === 0">
                <p class="mx-auto mb-0">Feed not discovered.</p>
              </v-container>
              <v-container fill-height v-if="discoverError">
                <p class="mx-auto red--text mb-0">Error occured.</p>
              </v-container>

              <v-list two-line class="pl-3 pr-3">
                <template v-for="(feed, index) in discoveredFeeds">
                  <v-list-tile @click="subscribeFeed(feed)" :key="feed.title">
                    <v-list-tile-content>
                      <v-list-tile-title><img :src="feed.favicon" width="16" height="16" /> {{ feed.title }} ({{ feed.feedType }})</v-list-tile-title>
                      <v-list-tile-sub-title>{{ feed.description }}</v-list-tile-sub-title>
                    </v-list-tile-content>
                    <v-list-tile-action>
                      <v-list-tile-action-text><v-icon small>group</v-icon>&nbsp; 1023</v-list-tile-action-text>
                    </v-list-tile-action>
                  </v-list-tile>
                  <v-divider :key="index"></v-divider>
                </template>
              </v-list>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue" flat @click.native="closeAddFeedDialog">Close</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>

          <v-menu offset-y>
            <v-btn flat small slot="activator" class="ml-0 mr-0">
              <v-icon small class="mr-1">sort</v-icon>Sort
            </v-btn>
            <v-list>
              <v-list-tile @click="sortAlphabeticallyAsc">
                <v-list-tile-title>Alphabetically asc</v-list-tile-title>
              </v-list-tile>
              <v-list-tile @click="sortAlphabeticallyDesc">
                <v-list-tile-title>Alphabetically desc</v-list-tile-title>
              </v-list-tile>
            </v-list>
          </v-menu>
        </v-card>
      </v-flex>
    </v-layout>

    <v-container fill-height v-if="loading">
      <v-progress-circular class="mx-auto" indeterminate :size="70" :width="7" color="blue"></v-progress-circular>
    </v-container>

      <v-list dense subheader v-if="subscriptions">
        <v-subheader>Subscriptions</v-subheader>
        <v-subheader>
          <v-text-field v-model="fileterWord" flat solo-inverted prepend-icon="search" label="Filter" class="mb-2" style="min-height:32px; height:32px;"></v-text-field>
        </v-subheader>

        <template v-for="(s, index) in filteredSubscriptions">
          <v-list-tile ripple @click="fetchFeed(s)" :key="'subId_' + s.id" :class="{'light-blue lighten-5': s === activeSubscription}">
            <v-list-tile-action style="min-width: 25px;">
              <img :src="s.feed.favicon" width="16" height="16" />
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title>
                <span :title="s.feed.title">{{ s.feed.title }}</span>
              </v-list-tile-title>
            </v-list-tile-content>
            <v-list-tile-action>
              <v-chip disabled color="grey lighten-3" style="font-size:10px; height: 16px">{{ s.unreadCount }}</v-chip>
            </v-list-tile-action>
          </v-list-tile>
          <v-divider :key="index" />
        </template>
      </v-list>
    </v-navigation-drawer>
</template>

<script>
import ApiClient from '../ApiClient'

export default {
  data () {
    return {
      subscriptions: null,
      loading: false,
      fileterWord: '',
      activeSubscription: null,

      addFeedDialog: false,
      discoverUrl: null,
      discoveredFeeds: null,
      discoverLoading: false,
      discoverError: false
    }
  },
  created () {
    this.fetchSubscriptions()

    this.$eventHub.$on('readItem', this.readItem)
    this.$eventHub.$on('unreadItem', this.unreadItem)
  },
  computed: {
    filteredSubscriptions () {
      const filteredList = this.subscriptions.filter((s) => {
        const title = s.feed.title.toLowerCase()
        const fileterWord = this.fileterWord.toLowerCase()
        return title.indexOf(fileterWord) > -1
      })
      const sortKey = localStorage.getItem('subscriptionSortKey')
      if (sortKey === 'AlphabeticallyAsc') {
        return filteredList.sort()
      } else if (sortKey === 'AlphabeticallyDesc') {
        return filteredList.sort().reverse()
      } else {
        return filteredList
      }
    }
  },
  methods: {
    fetchSubscriptions () {
      this.loading = true
      this.subscriptions = null

      const api = new ApiClient()
      api.getSubscriptions()
        .then((response) => {
          this.subscriptions = response.data
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
          console.log(error)
        })
    },
    fetchFeed (subscription, event) {
      this.activeSubscription = subscription
      const feedId = subscription.feed.id
      this.$router.push({name: 'Item', params: { feedId }})
    },
    sortAlphabeticallyAsc () {
      const tmp = this.subscriptions
      this.subscriptions = null
      localStorage.setItem('subscriptionSortKey', 'AlphabeticallyAsc')
      this.subscriptions = tmp
    },
    sortAlphabeticallyDesc () {
      const tmp = this.subscriptions
      this.subscriptions = null
      localStorage.setItem('subscriptionSortKey', 'AlphabeticallyDesc')
      this.subscriptions = tmp
    },
    discoverFeed () {
      this.discoverLoading = true
      this.discoverError = false
      this.discoveredFeeds = null

      const api = new ApiClient()
      api.discoverFeeds(this.discoverUrl)
        .then((response) => {
          this.discoveredFeeds = response.data
          this.discoverLoading = false
        })
        .catch((error) => {
          this.discoverLoading = false
          this.discoverError = true
          console.log(error)
        })
    },
    subscribeFeed (feed) {
      this.discoveredFeeds = null
      this.discoverLoading = true

      const api = new ApiClient()
      api.subscribeFeed(feed.feedUrl)
        .then((response) => {
          this.closeAddFeedDialog()
          this.fetchSubscriptions()
        })
        .catch((error) => {
          console.log(error)
        })
    },
    closeAddFeedDialog () {
      this.addFeedDialog = false
      this.discoverUrl = null
      this.discoveredFeeds = null
      this.discoverLoading = false
      this.discoverError = false
    },
    readItem (feedId) {
      console.log('read in nav')
      this.subscriptions.forEach((s) => {
        if (s.feed.id === feedId) {
          s.unreadCount = s.unreadCount - 1
        }
      });
    },
    unreadItem (feedId) {
      console.log('unread in nav')
      this.subscriptions.forEach((s) => {
        if (s.feed.id === feedId) {
          s.unreadCount = s.unreadCount + 1
        }
      });
    }
  }
}
</script>

<style scoped>
</style>
