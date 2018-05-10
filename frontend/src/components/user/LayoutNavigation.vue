<template>

  <v-navigation-drawer app fixed :clipped="$vuetify.breakpoint.lgAndUp">
    <button style="display: none" v-shortkey="['r']" @shortkey="reloadSubscription"></button>
    <button style="display: none" v-shortkey="['s']" @shortkey="nextSubscription"></button>
    <button style="display: none" v-shortkey="['a']" @shortkey="prevSubscription"></button>
    <button style="display: none" v-shortkey="['o']" @shortkey="readPins"></button>

    <v-container class="sticky-menu">
      <v-layout row wrap style="background-color: white; margin-right: 1px;" class="pt-2 pr-2 pb-1 pl-2">
        <v-flex xs12 class="mb-1 pb-1" style="border-bottom:1px dotted #EEEEEE">
          <v-btn flat small @click="fetchSubscriptions" class="ma-0">
            <v-icon small class="mr-1">autorenew</v-icon>Refresh
          </v-btn>

          <v-menu offset-y>
            <v-btn flat small slot="activator" class="ma-0">
              <v-icon small class="mr-1" style="height: 12px;">fas fa-sort-amount-up</v-icon>Sort
            </v-btn>
            <v-list>
              <v-list-tile @click="sortSubscriptions('UPDATED_AT_DESC')">
                <v-list-tile-title v-if="!isActiveSortKey('UPDATED_AT_DESC')">新着順</v-list-tile-title>
                <v-list-tile-title v-if="isActiveSortKey('UPDATED_AT_DESC')"><strong>新着順</strong></v-list-tile-title>
              </v-list-tile>
              <v-list-tile @click="sortSubscriptions('UPDATED_AT_ASC')">
                <v-list-tile-title v-if="!isActiveSortKey('UPDATED_AT_ASC')">旧着順</v-list-tile-title>
                <v-list-tile-title v-if="isActiveSortKey('UPDATED_AT_ASC')"><strong>旧着順</strong></v-list-tile-title>
              </v-list-tile>
              <v-list-tile @click="sortSubscriptions('UNREAD_COUNT_DESC')">
                <v-list-tile-title v-if="!isActiveSortKey('UNREAD_COUNT_DESC')">未読が多い</v-list-tile-title>
                <v-list-tile-title v-if="isActiveSortKey('UNREAD_COUNT_DESC')"><strong>未読が多い</strong></v-list-tile-title>
              </v-list-tile>
              <v-list-tile @click="sortSubscriptions('UNREAD_COUNT_ASC')">
                <v-list-tile-title v-if="!isActiveSortKey('UNREAD_COUNT_ASC')">未読が少ない</v-list-tile-title>
                <v-list-tile-title v-if="isActiveSortKey('UNREAD_COUNT_ASC')"><strong>未読が少ない</strong></v-list-tile-title>
              </v-list-tile>
              <v-list-tile @click="sortSubscriptions('TITLE_ASC')">
                <v-list-tile-title v-if="!isActiveSortKey('TITLE_ASC')">タイトル 昇順</v-list-tile-title>
                <v-list-tile-title v-if="isActiveSortKey('TITLE_ASC')"><strong>タイトル 昇順</strong></v-list-tile-title>
              </v-list-tile>
              <v-list-tile @click="sortSubscriptions('TITLE_DESC')">
                <v-list-tile-title v-if="!isActiveSortKey('TITLE_DESC')">タイトル 降順</v-list-tile-title>
                <v-list-tile-title v-if="isActiveSortKey('TITLE_DESC')"><strong>タイトル 降順</strong></v-list-tile-title>
              </v-list-tile>
            </v-list>
          </v-menu>

          <v-btn flat small class="ma-0" @click="subscribeDialog = true">
            <v-icon small class="mr-1">add_circle_outline</v-icon>Add Feed
          </v-btn>
          <subscribe-dialog :dialog-visible="subscribeDialog" @close="closeSubscribeDialog"></subscribe-dialog>
        </v-flex>

        <v-flex xs12 class="mt-0">
          <v-btn flat small class="ma-0" to="/subscriptions" exact=true>
            <v-icon small class="mr-1">library_books</v-icon>Subscriptions
          </v-btn>
          <v-btn flat small class="ma-0" to="/pins">
            <v-icon small class="mr-1" data-fa-transform="rotate-45" style="height: 12px;">fas fa-thumbtack</v-icon>Pins ({{ pins.length }})
          </v-btn>
        </v-flex>
      </v-layout>
    </v-container>

    <v-container fill-height v-if="loading">
      <v-progress-circular class="mx-auto" indeterminate :size="70" :width="7" color="blue"></v-progress-circular>
    </v-container>

      <v-list dense subheader v-if="subscriptions">
        <div class="sticky-search">
          <v-subheader class="pl-1 pr-1">
            <v-text-field v-model="fileterWord" flat solo-inverted prepend-icon="fa-search" label="Search" style="min-height:32px; height:32px;"></v-text-field>
          </v-subheader>
        </div>

        <template v-for="s in filteredSubscriptions">
          <v-divider :key="'navi_' + s.feed.title" />
          <v-list-tile @click="toItems(s)" :key="s.feed.title" :class="{'light-blue lighten-5': s.id === activeSubscriptionId}">
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
        </template>
      </v-list>
    </v-navigation-drawer>
</template>

<script>
import SubscribeDialog from './SubscribeDialog'
import { mapState } from 'vuex'
import LocalStorage from '../../LocalStorage'
import SubscriptionSorter from '../../SubscriptionSorter'

const ls = new LocalStorage()

export default {
  components: {
    'subscribe-dialog': SubscribeDialog
  },
  data () {
    return {
      loading: false,
      fileterWord: '',
      subscribeDialog: false
    }
  },
  computed: {
    ...mapState(['subscriptions', 'pins']),
    activeSubscriptionId () {
      return Number(this.$route.params.subscriptionId)
    },
    filteredSubscriptions () {
      const filteredList = this.subscriptions
        .filter(s => s.unreadCountOriginal > 0)
        .filter(s => {
          const title = s.feed.title.toLowerCase()
          const fileterWord = this.fileterWord.toLowerCase()
          return title.indexOf(fileterWord) > -1
        })

      const sorter = new SubscriptionSorter()
      return sorter.sort(filteredList, ls.getSubscriptionSortKey())
    },
    ellipsedPins () {
      return this.pins.slice(0, 10)
    }
  },
  methods: {
    fetchSubscriptions () {
      this.loading = true

      this.$store.dispatch('GET_SUBSCRIPTIONS')
        .then((response) => {
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    toItems (subscription) {
      this.$router.push({name: 'Items', params: { 'subscriptionId': subscription.id }})
    },
    sortSubscriptions (sortKey) {
      const tmp = this.subscriptions
      this.$store.commit('SET_SUBSCRIPTIONS', {subscriptions: null})
      ls.setSubscriptionSortKey(sortKey)
      this.$store.commit('SET_SUBSCRIPTIONS', {subscriptions: tmp})
    },
    isActiveSortKey (targetSortKey) {
      return ls.getSubscriptionSortKey() === targetSortKey
    },
    closeSubscribeDialog () {
      this.subscribeDialog = false
    },
    reloadSubscription () {
      this.fetchSubscriptions()
    },
    nextSubscription () {
      let targetSubscription = null
      if (this.activeSubscriptionId === null) {
        if (this.subscriptions.length > 0) {
          targetSubscription = this.filteredSubscriptions[0]
        }
      } else {
        const foundIndex = this.filteredSubscriptions.findIndex(s => s.id === this.activeSubscriptionId)
        if (foundIndex !== -1) {
          const targetIndex = foundIndex === this.filteredSubscriptions.length - 1 ? foundIndex : foundIndex + 1
          targetSubscription = this.filteredSubscriptions[targetIndex]
        } else {
          targetSubscription = this.filteredSubscriptions[0]
        }
      }

      if (targetSubscription !== null) {
        this.toItems(targetSubscription)
      }
    },
    prevSubscription () {
      let targetSubscription = null
      if (this.activeSubscriptionId === null) {
        if (this.subscriptions.length > 0) {
          targetSubscription = this.filteredSubscriptions[0]
        }
      } else {
        const foundIndex = this.filteredSubscriptions.findIndex(s => s.id === this.activeSubscriptionId)
        if (foundIndex !== -1) {
          const targetIndex = foundIndex === 0 ? 0 : foundIndex - 1
          targetSubscription = this.filteredSubscriptions[targetIndex]
        } else {
          targetSubscription = this.filteredSubscriptions[0]
        }
      }

      if (targetSubscription !== null) {
        this.toItems(targetSubscription)
      }
    },
    readPins () {
      this.ellipsedPins.forEach(pin => {
        window.open(pin.url, '_blank')
        this.$store.dispatch('REMOVE_PIN', {'pin': pin})
      })
    }
  }
}
</script>

<style scoped>
.sticky-menu {
  position: -webkit-sticky;
  position:sticky;
  top: 0;
  z-index: 100;
  padding: 0;
}

.sticky-search {
  position: -webkit-sticky;
  position:sticky;
  top: 78px;
  z-index: 90;
  background-color: white;
  margin-right: 1px;
  border-bottom: 1px solid #E0E0E0;
  border-top: 1px solid #E0E0E0;
}
</style>
