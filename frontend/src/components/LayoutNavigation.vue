<template>

  <v-navigation-drawer app fixed :clipped="$vuetify.breakpoint.lgAndUp">

    <v-layout>
      <v-flex xs12>
        <v-card class="elevation-1">
          <v-btn-toggle>
            <v-btn flat @click="fetchSubscriptions">
              <v-icon small class="mr-1">autorenew</v-icon>
              <span style="text-transform: none">Refresh</span>
            </v-btn>
            <v-btn flat>
              <v-icon small class="mr-1">add_circle_outline</v-icon>
              <span style="text-transform: none">Add Feed</span>
            </v-btn>
            <div class="text-xs-center">
              <v-menu offset-y>
                <v-btn flat slot="activator">
                  <v-icon small class="mr-1">sort</v-icon>
                  <span style="text-transform: none">Sort</span>
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
            </div>
          </v-btn-toggle>
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
          <v-list-tile ripple @click="fetchFeed(s.feed.id)" :key="'subId_' + s.id">
            <v-list-tile-action style="min-width: 25px;">
              <img v-bind:src="s.feed.favicon" width="16" height="16" />
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title>
                <span v-bind:title="s.feed.title">{{ s.feed.title }}</span>
              </v-list-tile-title>
            </v-list-tile-content>
            <v-list-tile-action>
              <v-chip disabled color="grey lighten-3" style="font-size:10px; height: 16px">7</v-chip>
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
      subscriptions: [],
      loading: false,
      fileterWord: ''
    }
  },
  created () {
    this.fetchSubscriptions()
  },
  computed: {
    filteredSubscriptions () {
      console.log("call computed")
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
          console.log('ERROR!! happend by Backend.')
        })
    },
    fetchFeed (feedId, event) {
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
    }
  }
}
</script>

<style scoped>
</style>
