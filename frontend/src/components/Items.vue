<template>
  <div>
    <v-container fill-height v-if="loading">
      <v-progress-circular class="mx-auto" indeterminate :size="70" :width="7" color="blue"></v-progress-circular>
    </v-container>

    <v-alert v-if="error" type="error" :value="true">
      {{ error }}
    </v-alert>

    <div v-if="userItems">
      <button style="display: none" v-shortkey="['enter']" @shortkey="nextItem"></button>
      <button style="display: none" v-shortkey="['shift', 'enter']" @shortkey="prevItem"></button>
      <button style="display: none" v-shortkey="['p']" @shortkey="togglePin"></button>

      <div class="sticky-header">
        <pin-toolbar></pin-toolbar>
        <component-subscription :subscription="activeSubscription" :unread-item-count="unreadItemCount"></component-subscription>
      </div>

      <div :ref="'item-box-' + index" class="item-box" v-for="(userItem, index) in userItems" :key="'item_' + userItem.item.id" :class="itemBoxBackgroundColor(userItem)">
        <v-layout row wrap>
          <v-flex xs9>
            <h3 class="item-title">{{ userItem.item.title }}</h3>
          </v-flex>
          <v-flex xs3 class="operation-area">
            <v-btn flat small class="operation-btn" v-if="userItem.pin === null" @click="addPin(userItem)">
              <v-icon small data-fa-transform="rotate-45" class="operation-icon">fas fa-thumbtack</v-icon>Add Pin
            </v-btn>
            <v-btn flat small class="operation-btn" v-if="userItem.pin !== null" @click="removePin(userItem)">
              <v-icon small data-fa-transform="rotate-45" class="operation-icon">fas fa-thumbtack</v-icon>Remove Pin
            </v-btn>

            <v-btn flat small class="operation-btn" v-if="userItem.unread" @click.native="readItem(userItem)">
              <icon name="eye" class="operation-icon"></icon> Mark as read
            </v-btn>
            <v-btn flat small class="operation-btn" v-if="!userItem.unread" @click.native="unreadItem(userItem)">
              <icon name="eye-slash" class="operation-icon"></icon> Mark as unread
            </v-btn>
          </v-flex>
        </v-layout>

        <div class="footnote-area">
          <a :href="userItem.item.url" target="_blank">元記事</a> &nbsp;|&nbsp;
          {{ userItem.item.publishedAt | fromNow }}
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
      <br v-for="index in 30" />
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
      this.error = null
      this.loading = true

      api.getItems(this.activeSubscriptionId)
        .then((response) => {
          this.userItems = response.data
          this.loading = false
        })
        .catch(() => {
          this.loading = false
          this.error = 'データの取得に失敗しました'
        })
    },
    readItem (userItem) {
      if (userItem.unread) {
        api.readItem(userItem.itemId)
          .then((response) => {
            userItem.unread = false

            this.$store.commit('READ_ITEM', {subscriptionId: this.activeSubscriptionId, unreadItemCount: this.unreadItemCount})
          })
      }
    },
    unreadItem (userItem) {
      if (!userItem.unread) {
        api.unreadItem(userItem.itemId)
          .then((response) => {
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
    itemBoxBackgroundColor (userItem) {
      if (userItem.pin !== null) {
        return 'item-box-pin'
      }
      if (!userItem.unread) {
        return 'item-box-read'
      }
      return ''
    },
    nextItem () {
      if (this.activeItemIndex === null) {
        this.activeItemIndex = 0
      } else {
        this.activeItemIndex = this.activeItemIndex === this.userItems.length - 1 ? this.activeItemIndex : this.activeItemIndex + 1
      }

      const userItem = this.userItems[this.activeItemIndex]
      this.readItem(userItem)

      this.scrollToItem(this.activeItemIndex)
    },
    prevItem () {
      if (this.activeItemIndex === null) {
        this.activeItemIndex = 0
      } else {
        this.activeItemIndex = this.activeItemIndex === 0 ? 0 : this.activeItemIndex - 1
      }

      const userItem = this.userItems[this.activeItemIndex]
      this.readItem(userItem)

      this.scrollToItem(this.activeItemIndex)
    },
    scrollToItem (itemIndex) {
      const element = this.$refs['item-box-' + itemIndex]
      window.scrollTo(0, element[0].offsetTop - 246)
    },
    togglePin () {
      if (this.activeItemIndex === null) {
        return
      }
      const userItem = this.userItems[this.activeItemIndex]
      if (userItem.pin === null) {
        this.addPin(userItem)
      } else {
        this.removePin(userItem)
      }
    }
  }
}
</script>

<style scoped>
.sticky-header {
  position: -webkit-sticky;
  position: sticky;
  top: 46px;
  padding-top: 15px;
  z-index: 100;
  background-color: #FAFAFA;
}

.item-box {
  margin-top: 15px;
  margin-bottom: 15px;
  padding: 10px 15px 5px 15px;
  background-color: white;
  border: 1px dotted #BDBDBD;
}

.item-box-pin {
  background-color: #FFF4F6;
}

.item-box-read {
  background-color: #F5F5F5;
}

h3.item-title {
  font-weight: 600;
}

.operation-area {
  text-align: right;
  font-size: 12px;
}

.operation-btn {
  margin: 0;
  color: #1976d2;
}

.operation-icon {
  margin-right:5px;
}

.footnote-area {
  padding-top: 5px;
  color: #616161;
  font-size: 12px;
}

.description-area {
  padding: 10px;
  font-size: 13px;
  border-bottom: 1px solid #BDBDBD;
}

.shared-area {
  font-size: 1.2em;
  padding-top: 5px;
  text-align: right;
}
</style>
