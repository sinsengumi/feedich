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
              <v-list-tile @click="">
                <v-list-tile-title>Alphabetically asc</v-list-tile-title>
              </v-list-tile>
              <v-list-tile @click="">
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
          <v-list-tile ripple @click="toItems(s)" :key="s.feed.title" :class="{'light-blue lighten-5': s === activeSubscription}">
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
import SubscribeDialog from './SubscribeDialog'
import { mapState } from 'vuex'

export default {
  components: {
    'subscribe-dialog': SubscribeDialog
  },
  data () {
    return {
      loading: false,
      fileterWord: '',
      activeSubscription: null,
      subscribeDialog: false
    }
  },
  created () {
    this.$eventHub.$on('readItem', this.readItem)
    this.$eventHub.$on('unreadItem', this.unreadItem)
  },
  computed: {
    ...mapState(['subscriptions']),
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

      this.$store.dispatch('GET_SUBSCRIPTIONS')
        .then((response) => {
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
          console.log(error)
        })
    },
    toItems (subscription, event) {
      this.activeSubscription = subscription
      this.$router.push({name: 'Items', params: { 'subscriptionId': subscription.id }})
    },
    toSubscriptions () {
      this.$router.push({name: 'Subscriptions'})
    },
    /* sortAlphabeticallyAsc () {
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
    }, */
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
    }
  }
}
</script>

<style scoped>
</style>
