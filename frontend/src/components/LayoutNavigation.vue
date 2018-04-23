<template>

  <v-navigation-drawer app fixed :clipped="$vuetify.breakpoint.lgAndUp">
    <v-layout>
      <v-flex xs12>
        <v-card class="elevation-1 pb-0">
          <v-btn flat small @click="fetchSubscriptions" class="ml-1 mr-0">
            <v-icon small class="mr-1">autorenew</v-icon>Refresh
          </v-btn>

          <v-menu offset-y>
            <v-btn flat small slot="activator" class="ml-0 mr-0">
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

          <v-btn flat small class="ml-1 mr-0" @click="subscribeDialog = true">
            <v-icon small class="mr-1">add_circle_outline</v-icon>Add Feed
          </v-btn>
          <subscribe-dialog :dialog-visible="subscribeDialog" @close="closeSubscribeDialog"></subscribe-dialog>
        </v-card>
      </v-flex>
    </v-layout>

    <v-container fill-height v-if="loading">
      <v-progress-circular class="mx-auto" indeterminate :size="70" :width="7" color="blue"></v-progress-circular>
    </v-container>

      <v-list dense subheader v-if="subscriptions">
        <v-subheader><router-link class="subscription-link" to="/subscriptions"><v-icon small class="mr-1">library_books</v-icon>Subscriptions</router-link></v-subheader>

        <v-subheader class="mb-2">
          <v-text-field v-model="fileterWord" flat solo-inverted prepend-icon="fa-search" label="Search" style="min-height:32px; height:32px;"></v-text-field>
        </v-subheader>

        <template v-for="s in filteredSubscriptions">
          <v-divider :key="'navi_' + s.feed.title" />
          <v-list-tile @click="toItems(s)" :key="s.feed.title" :class="{'light-blue lighten-5': s === activeSubscription}">
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
import LocalStorage from '../LocalStorage'
import SubscriptionSorter from '../SubscriptionSorter'

const ls = new LocalStorage()

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
  computed: {
    ...mapState(['subscriptions']),
    filteredSubscriptions () {
      const filteredList = this.subscriptions
        .filter(s => s.unreadCount > 0)
        .filter(s => {
          const title = s.feed.title.toLowerCase()
          const fileterWord = this.fileterWord.toLowerCase()
          return title.indexOf(fileterWord) > -1
        })

      const sorter = new SubscriptionSorter()
      return sorter.sort(filteredList, ls.getSubscriptionSortKey())
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
    toItems (subscription, event) {
      this.activeSubscription = subscription
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
    }
  }
}
</script>

<style scoped>
.subscription-link {
  color: rgba(0,0,0,.87);
}
</style>
