<template>
  <div>
    <div class="title-area d-flex justify-content-between align-items-center">
      <span class="mr-auto"><a :href="activeSubscription.feed.url" target="_blank"><img class="align-text-top" :src="activeSubscription.feed.favicon" width="16" height="16" /> {{ activeSubscription.feed.title }} ({{ activeSubscription.unreadCount }})</a></span>
      <a href="javascript:void(0)" v-b-modal.subscriptionModal class="mr-3"><i class="fas fa-info-circle"></i> フィード情報</a>
      <a href="javascript:void(0)" v-b-modal.unsubscribeModal><i class="far fa-trash-alt"></i> 購読停止</a>
    </div>
    <subscription-modal :subscription="activeSubscription" :modal-visible="subscriptionModal" @close="subscriptionModal = false"></subscription-modal>
    <unsubscribe-modal :subscription="activeSubscription" :modal-visible="unsubscribeModal" @close="unsubscribeModal = false"></unsubscribe-modal>

    <div v-if="loading" class="d-flex justify-content-center align-items-center" style="height: 200px">
      <i class="fas fa-circle-notch fa-spin text-muted fa-4x"></i>
    </div>

    <div class="user-items" v-if="userItems">
      <button style="display: none" v-shortkey="['enter']" @shortkey="nextItem"></button>
      <button style="display: none" v-shortkey="['shift', 'enter']" @shortkey="prevItem"></button>
      <button style="display: none" v-shortkey="['p']" @shortkey="togglePin"></button>

      <div class="item-box" v-for="(userItem, index) in userItems" :key="'item_' + userItem.item.id" :ref="'item-box-' + index" :class="itemBoxBackgroundColor(userItem, index)">
        <div class="d-flex justify-content-between align-items-center">
          <span class="mr-auto" :class="userItem.unread ? 'item-title-unread' : 'item-title-read'">{{ userItem.item.title }}</span>

          <a class="mr-3 ml-2" href="javascript:void(0)" v-if="userItem.pin === null" @click="addPin(userItem, index)" title="ピンを付ける"><i class="fas fa-thumbtack fa-lg"></i></a>
          <a class="mr-3 ml-2" href="javascript:void(0)" v-if="userItem.pin !== null" @click="removePin(userItem, index)" title="ピンを外す"><i class="fas fa-thumbtack fa-lg text-warning"></i></a>

          <a href="javascript:void(0)" v-if="userItem.unread"  @click="readItem(userItem, index)" title="既読にする"><i class="fa fa-eye fa-lg"></i></a>
          <a href="javascript:void(0)" v-if="!userItem.unread" @click="unreadItem(userItem, index)" title="未読にする"><i class="fa fa-eye-slash fa-lg"></i></a>
        </div>

        <div class="footnote-area">
          <a :href="userItem.item.url" target="_blank">元記事</a> &nbsp;|&nbsp;
          <span :title="userItem.item.publishedAt">{{ userItem.item.publishedAt | fromNow }}</span>
          <span v-if="userItem.item.author">by {{ userItem.item.author }}</span>
        </div>

        <div class="description-area" v-html="userItem.item.description">
        </div>

        <div class="shared-area">
          <i class="fab fa-twitter"></i>
          <i class="fab fa-facebook"></i>
          <i class="fab fa-line"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ApiClient from '../../ApiClient'
import SubscriptionModal from './SubscriptionModal'
import UnsubscribeModal from './UnsubscribeModal'

const api = new ApiClient()

export default {
  name: 'Item',
  components: {
    'subscription-modal': SubscriptionModal,
    'unsubscribe-modal': UnsubscribeModal
  },
  data () {
    return {
      loading: false,
      userItems: null,
      activeItemIndex: null,
      subscriptionModal: false,
      unsubscribeModal: false
    }
  },
  created () {
    this.fetchData()
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
      this.activeItemIndex = null
      this.userItems = null
      this.loading = true

      document.title = this.activeSubscription.feed.title + ' | Feedich'

      api.getItems(this.activeSubscriptionId)
        .then((response) => {
          this.userItems = response.data
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    readItem (userItem, index) {
      this.activeItemIndex = index
      if (userItem.unread) {
        api.readItem(userItem.itemId)
          .then((response) => {
            userItem.unread = false

            this.$store.commit('READ_ITEM', {subscriptionId: this.activeSubscriptionId, unreadItemCount: this.unreadItemCount})
          })
      }
    },
    unreadItem (userItem, index) {
      this.activeItemIndex = index
      if (!userItem.unread) {
        api.unreadItem(userItem.itemId)
          .then((response) => {
            userItem.unread = true

            this.$store.commit('READ_ITEM', {subscriptionId: this.activeSubscriptionId, unreadItemCount: this.unreadItemCount})
          })
      }
    },
    addPin (userItem, index) {
      this.activeItemIndex = index
      this.$store.dispatch('ADD_PIN', {title: userItem.item.title, url: userItem.item.url})
        .then((addedPin) => {
          userItem.pin = addedPin
        })
    },
    removePin (userItem, index) {
      this.activeItemIndex = index
      this.$store.dispatch('REMOVE_PIN', {pin: userItem.pin})
        .then((removedPin) => {
          userItem.pin = null
        })
    },
    itemBoxBackgroundColor (userItem, index) {
      let activeClass = ' '
      if (index === this.activeItemIndex) {
        activeClass = 'item-box-active '
      }
      if (!userItem.unread) {
        return activeClass + 'item-box-read'
      }
      return activeClass
    },
    nextItem () {
      if (this.activeItemIndex === null) {
        this.activeItemIndex = 0
      } else {
        this.activeItemIndex = this.activeItemIndex === this.userItems.length - 1 ? this.activeItemIndex : this.activeItemIndex + 1
      }

      const userItem = this.userItems[this.activeItemIndex]
      this.readItem(userItem, this.activeItemIndex)

      this.scrollToItem(this.activeItemIndex)
    },
    prevItem () {
      if (this.activeItemIndex === null) {
        this.activeItemIndex = 0
      } else {
        this.activeItemIndex = this.activeItemIndex === 0 ? 0 : this.activeItemIndex - 1
      }

      const userItem = this.userItems[this.activeItemIndex]
      this.readItem(userItem, this.activeItemIndex)

      this.scrollToItem(this.activeItemIndex)
    },
    scrollToItem (itemIndex) {
      const element = this.$refs['item-box-' + itemIndex]
      window.scrollTo(0, element[0].offsetTop - 95)
    },
    togglePin () {
      if (this.activeItemIndex === null) {
        return
      }
      const userItem = this.userItems[this.activeItemIndex]
      if (userItem.pin === null) {
        this.addPin(userItem, this.activeItemIndex)
      } else {
        this.removePin(userItem, this.activeItemIndex)
      }
    }
  }
}
</script>

<style scoped>
</style>
