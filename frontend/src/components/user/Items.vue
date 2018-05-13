<template>
  <div>
    <div class="title-area d-flex justify-content-between align-items-center">
      <span class="mr-auto"><img class="align-text-top" :src="activeSubscription.feed.favicon" width="16" height="16" /> {{ activeSubscription.feed.title }} ({{ activeSubscription.unreadCount }})</span>
      <a href="javascript:void(0)" class="mr-3" v-b-modal.subscriptionModal><i class="fas fa-info-circle"></i> フィード情報</a>
      <a href="javascript:void(0)"><i class="far fa-trash-alt"></i> 購読停止</a>
    </div>
    <subscription-dialog :subscription="activeSubscription"></subscription-dialog>

    <div v-if="loading" class="d-flex justify-content-center align-items-center" style="height: 200px">
      <icon name="spinner" class="text-muted" width="56" height="56" pulse></icon>
    </div>

    <div class="user-items" v-if="userItems">
      <button style="display: none" v-shortkey="['enter']" @shortkey="nextItem"></button>
      <button style="display: none" v-shortkey="['shift', 'enter']" @shortkey="prevItem"></button>
      <button style="display: none" v-shortkey="['p']" @shortkey="togglePin"></button>

      <div class="item-box" v-for="(userItem, index) in userItems" :key="'item_' + userItem.item.id" :ref="'item-box-' + index" :class="itemBoxBackgroundColor(userItem, index)">
        <div class="d-flex justify-content-between align-items-center">
          <span class="mr-auto" :class="userItem.unread ? 'item-title-unread' : 'item-title-read'">{{ userItem.item.title }}</span>

          <a class=" mr-3" href="javascript:void(0)" v-if="userItem.pin === null" @click="addPin(userItem, index)" title="スターを付ける"><icon name="regular/star" :height="18" :width="18" class="align-text-top"></icon></a>
          <a class=" mr-3" href="javascript:void(0)" v-if="userItem.pin !== null" @click="removePin(userItem, index)" title="スターを外す"><icon name="star" :height="18" :width="18" class="align-text-top text-warning"></icon></a>

          <a class="" href="javascript:void(0)" v-if="userItem.unread"  @click="readItem(userItem, index)" title="既読にする"><icon name="eye" :height="18" :width="18" class="align-text-top"></icon></a>
          <a class="" href="javascript:void(0)" v-if="!userItem.unread" @click="unreadItem(userItem, index)" title="未読にする"><icon name="eye-slash" :height="18" :width="18" class="align-text-top"></icon></a>
        </div>

        <div class="footnote-area">
          <a :href="userItem.item.url" target="_blank">元記事</a> &nbsp;|&nbsp;
          <span :title="userItem.item.publishedAt">{{ userItem.item.publishedAt | fromNow }}</span>
          <span v-if="userItem.item.author">by {{ userItem.item.author }}</span>
        </div>

        <div class="description-area" v-html="userItem.item.description">
        </div>

        <div class="shared-area">
          <i class="fab fa-twitter"></i> &nbsp;
          <i class="fab fa-facebook"></i> &nbsp;
          <i class="fab fa-line"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ApiClient from '../../ApiClient'
import Subscription from './Subscription'
import SubscriptionDialog from './SubscriptionDialog'

const api = new ApiClient()

export default {
  name: 'Item',
  components: {
    'component-subscription': Subscription,
    'subscription-dialog': SubscriptionDialog
  },
  data () {
    return {
      loading: false,
      userItems: null,
      activeItemIndex: null
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
          this.$toasted.global.info({message: 'スターを付けました'})
          userItem.pin = addedPin
        })
    },
    removePin (userItem, index) {
      this.activeItemIndex = index
      this.$store.dispatch('REMOVE_PIN', {pin: userItem.pin})
        .then((removedPin) => {
          this.$toasted.global.info({message: 'スターを外しました'})
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
