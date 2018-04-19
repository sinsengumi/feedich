<template>
  <div>
    <v-container fill-height v-if="loading">
      <v-progress-circular class="mx-auto" indeterminate :size="70" :width="7" color="blue"></v-progress-circular>
    </v-container>

    <v-alert v-if="error" type="error" :value="true">
      {{ error }}
    </v-alert>

    <div v-if="userItems">
      <pin-toolbar></pin-toolbar>

      <component-subscription :subscription="activeSubscription" :unread-item-count="unreadItemCount"></component-subscription>

      <br />

      <v-expansion-panel focusable>
        <v-expansion-panel-content v-for="userItem in userItems" :key="'itemId_' + userItem.itemId">
          <div slot="header" :class="{title: userItem == activeItem}" @click="readItem(userItem, $event)" :ref="'itemRef_' + userItem.itemId">{{ userItem.item.title }}</div>

          <v-container grid-list-md>
                <v-layout row wrap>
                  <v-flex xs9>
                    <p>
                      Author: {{ userItem.item.author }} | {{ userItem.item.publishedAt | fromNow }} |
                      <v-btn v-if="userItem.pin === null" flat small @click="addPin(userItem)">ピンを付ける</v-btn>
                      <v-btn v-if="userItem.pin !== null" flat small @click="removePin(userItem)">ピンを外す</v-btn>
                      |
                      <v-btn flat small :href="userItem.item.url" target="_blank">記事詳細</v-btn>
                      |
                      <v-btn v-if="!userItem.unread" flat small @click="unreadItem(userItem, $event)">未読にする</v-btn>
                      <v-btn v-if="userItem.unread"  flat small @click="readItem(userItem, $event)">既読にする</v-btn>
                      |
                      共有
                    </p>
                  </v-flex>
                  <v-flex xs3  text-xs-right>
                    <v-chip class="grey" text-color="white" style="font-size:12px; height: 24px">
                      <v-icon small>clear</v-icon>&nbsp;<strong>Unsubscribe</strong>
                    </v-chip>
                  </v-flex>
                </v-layout>
              </v-container>
          <v-card>
            <v-card-text><div class="caption" v-html="userItem.item.description"></div></v-card-text>
          </v-card>
        </v-expansion-panel-content>
      </v-expansion-panel>

    </div>
  </div>
</template>

<script>
import ApiClient from '../ApiClient'
import Subscription from './Subscription'
import PinToolbar from './PinToolbar'

const api = new ApiClient()

export default {
  name: 'Item',
  components: {
    'component-subscription': Subscription,
    'pin-toolbar': PinToolbar
  },
  data () {
    return {
      loading: false,
      error: null,
      userItems: null,
      activeItem: null
    }
  },
  created () {
    this.fetchData()

    this.$eventHub.$on('shortcutEnter', this.shortcutEnter)
  },
  watch: {
    '$route': 'fetchData'
  },
  computed: {
    activeSubscriptionId () {
      return Number(this.$route.params.subscriptionId)
    },
    activeSubscription () {
      return this.$store.getters.getSubscriptionById(this.activeSubscriptionId)
    },
    unreadItemCount () {
      return this.userItems.filter((userItem) => {
        return userItem.unread
      }).length
    }
  },
  methods: {
    fetchData () {
      this.userItems = null
      this.error = null
      this.loading = true

      api.getItems(this.activeSubscriptionId)
        .then((response) => {
          this.userItems = response.data
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
          this.error = 'データの取得に失敗しました'
        })
    },
    readItem (userItem, event) {
      this.activeItem = userItem

      if (this.activeItem.unread) {
        api.readItem(userItem.itemId)
          .then((response) => {
            this.activeItem.unread = false

            this.$store.commit('READ_ITEM', {subscriptionId: this.activeSubscriptionId, unreadItemCount: this.unreadItemCount})
          })
      }
    },
    unreadItem (userItem, event) {
      this.activeItem = userItem

      if (!this.activeItem.unread) {
        api.unreadItem(userItem.itemId)
          .then((response) => {
            this.activeItem.unread = true
            userItem.unread = true

            this.$store.commit('READ_ITEM', {subscriptionId: this.activeSubscriptionId, unreadItemCount: this.unreadItemCount})
          })
      }
    },
    addPin (userItem) {
      this.$store.dispatch('ADD_PIN', {title: userItem.item.title, url: userItem.item.url})
        .then((addedPin) => {
          userItem.pin = addedPin
        })
    },
    removePin (userItem) {
      this.$store.dispatch('REMOVE_PIN', {pin: userItem.pin})
        .then((removedPin) => {
          userItem.pin = null
        })
    },
    shortcutEnter () {
      // activeItem の次の要素を見つける
      /* let targetItemId = null
      if (this.activeItem == null) {
        if (this.userItems.length > 0) {
          targetItemId = this.userItems[0].id
        }
      } else {
        this.userItems.forEach((userItem, index, items) => {
          if (userItem.itemId === this.activeItem.itemId) {
            targetItemId = userItems[index + 1].itemId
          }
        })
      }

      if (targetItemId != null) {
        const key = 'itemRef_' + targetItemId
        const targetElement = this.$refs[key]
        targetElement[0].click()
      } */
    }
  }
}
</script>

<style scoped>
</style>
