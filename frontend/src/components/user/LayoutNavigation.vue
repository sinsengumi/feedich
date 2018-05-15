<template>
  <div class="sidebar-sticky">
    <button class="d-none" v-shortkey="['r']" @shortkey="reloadSubscription"></button>
    <button class="d-none" v-shortkey="['s']" @shortkey="nextSubscription"></button>
    <button class="d-none" v-shortkey="['a']" @shortkey="prevSubscription"></button>
    <button class="d-none" v-shortkey="['o']" @shortkey="readPins"></button>

    <div class="operation-area">
      <div class="btn-group d-flex" role="group">
        <button type="button" class="btn btn-light flex-fill" @click="fetchSubscriptions"><i class="fa fa-sync-alt"></i> 更新</button>
        <div class="btn-group d-flex" role="group">
          <button id="sortMenu" type="button" class="btn btn-light flex-fill dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <i class="fas fa-sort-amount-up"></i> 並び替え
          </button>
          <div class="dropdown-menu sort-menu" aria-labelledby="sortMenu">
            <a class="dropdown-item" href="javascript:void(0)" :class="isActiveSortKey('UPDATED_AT_DESC') ? 'active' : ''" @click="sortSubscriptions('UPDATED_AT_DESC')">新着順</a>
            <a class="dropdown-item" href="javascript:void(0)" :class="isActiveSortKey('UPDATED_AT_ASC') ? 'active' : ''" @click="sortSubscriptions('UPDATED_AT_ASC')">旧着順</a>
            <a class="dropdown-item" href="javascript:void(0)" :class="isActiveSortKey('UNREAD_COUNT_DESC') ? 'active' : ''" @click="sortSubscriptions('UNREAD_COUNT_DESC')">未読が多い</a>
            <a class="dropdown-item" href="javascript:void(0)" :class="isActiveSortKey('UNREAD_COUNT_ASC') ? 'active' : ''" @click="sortSubscriptions('UNREAD_COUNT_ASC')">未読が少ない</a>
            <a class="dropdown-item" href="javascript:void(0)" :class="isActiveSortKey('TITLE_ASC') ? 'active' : ''" @click="sortSubscriptions('TITLE_ASC')">タイトル 昇順</a>
            <a class="dropdown-item" href="javascript:void(0)" :class="isActiveSortKey('TITLE_DESC') ? 'active' : ''" @click="sortSubscriptions('TITLE_DESC')">タイトル 降順</a>
          </div>
        </div>
        <button type="button" class="btn btn-light flex-fill" v-b-modal.subscribeModal><i class="fas fa-plus"></i> 購読</button>
      </div>
      <div class="btn-group d-flex" role="group">
        <b-button type="button" class="btn btn-light flex-fill" to="/subscriptions" :exact="true"><i class="far fa-clone"></i> 購読フィード</b-button>
        <b-button type="button" class="btn btn-light flex-fill" to="/pins" :exact="true"><i class="fas fa-thumbtack"></i> ピン ({{ pins.length }})</b-button>
      </div>

      <div class="search-area" v-if="subscriptions">
        <div class="input-group">
          <input type="text" v-model="fileterWord" class="form-control" placeholder="Search..." />
        </div>
      </div>
    </div>

    <div v-if="loading" class="d-flex justify-content-center align-items-center" style="height: 80%">
      <i class="fas fa-circle-notch fa-spin text-muted fa-4x"></i>
    </div>

    <div v-if="!loading && filteredSubscriptions.length === 0" class="d-flex justify-content-center align-items-center" style="height: 80%">
      <span class="no-results">No Results</span>
    </div>

    <ul class="subscription-list">
      <li v-for="s in filteredSubscriptions" class="d-flex align-items-center" :title="s.feed.title" @click="toItems(s)" :class="{'active': s.id === activeSubscriptionId}" :key="s.feed.title">
        <img :src="s.feed.favicon" width="16" height="16" />
        <span class="subscription-title text-truncate">{{ s.feed.title }}</span>
        <span class="ml-auto badge text-info">{{ s.unreadCount }}</span>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import LocalStorage from '../../LocalStorage'
import SubscriptionSorter from '../../SubscriptionSorter'

const ls = new LocalStorage()

export default {
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
</style>
