<template>

  <v-navigation-drawer app fixed :clipped="$vuetify.breakpoint.lgAndUp">

    <v-layout>
      <v-flex xs12>
        <v-card class="elevation-0">
          <v-btn flat small @click="fetchSubscriptions" class="ml-1 mr-0">
            <v-icon small class="mr-1">autorenew</v-icon>Refresh
          </v-btn>

          <v-menu offset-y>
            <v-btn flat small slot="activator" class="ml-0 mr-0">
              <v-icon small class="mr-1">filter_list</v-icon>Filter
            </v-btn>
          </v-menu>

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

        <v-card class="elevation-1">
          <subscribe-dialog :dialog-visible="subscribeDialog" @close="closeSubscribeDialog"></subscribe-dialog>
          <v-btn flat small class="ml-0 mr-0" @click="subscribeDialog = true">
            <v-icon small class="mr-1">add_circle_outline</v-icon>Add Feed
          </v-btn>

          <v-btn flat small @click="toSubscriptions" class="ml-1 mr-0">
            <v-icon small class="mr-1">library_books</v-icon>Subscriptions
          </v-btn>
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
          <v-list-tile ripple @click="fetchFeed(s)" :key="s.feed.title" :class="{'light-blue lighten-5': s === activeSubscription}">
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
import SubscribeDialog from './SubscribeDialog'

export default {
  components: {
    'subscribe-dialog': SubscribeDialog
  },
  data () {
    return {
      subscriptions: null,
      loading: false,
      fileterWord: '',
      activeSubscription: null,
      subscribeDialog: false
    }
  },
  created () {
    this.fetchSubscriptions()

    this.$eventHub.$on('readItem', this.readItem)
    this.$eventHub.$on('unreadItem', this.unreadItem)
    this.$eventHub.$on('subscribe', this.unsubscribe)
    this.$eventHub.$on('unsubscribe', this.unsubscribe)
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
      const subscriptionId = subscription.id
      this.$router.push({name: 'Items', params: { subscriptionId }})
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
    readItem (feedId, unreadItemCount) {
      this.subscriptions.forEach((s) => {
        if (s.feed.id === feedId) {
          s.unreadCount = unreadItemCount
        }
      })
    },
    unreadItem (feedId, unreadItemCount) {
      this.subscriptions.forEach((s) => {
        if (s.feed.id === feedId) {
          s.unreadCount = unreadItemCount
        }
      })
    },
    closeSubscribeDialog (feed) {
      this.subscribeDialog = false
    },
    subscribe () {
      this.fetchSubscriptions()
    },
    unsubscribe () {
      this.fetchSubscriptions()
    },
    toSubscriptions () {
      this.$router.push({name: 'Subscriptions'})
    }
  }
}
</script>

<style scoped>
</style>
