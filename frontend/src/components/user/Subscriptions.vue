<template>
  <div class="subscriptions">
    <subscription-modal v-if="targetSubscription !== null" :subscription="targetSubscription" :modal-visible="subscriptionModal" @close="subscriptionModal = false"></subscription-modal>
    <unsubscribe-modal v-if="targetSubscription !== null" :subscription="targetSubscription" :modal-visible="unsubscribeModal" @close="unsubscribeModal = false"></unsubscribe-modal>

    <div class="card">
      <h5 class="card-header">購読フィード ({{ subscriptions.length }})</h5>
      <div class="card-body">
        <div class="form-row mb-2">
          <div class="offset-9 col-3">
            <input type="text" class="form-control form-control-sm" v-model="searchQuery" placeholder="Search..." />
          </div>
        </div>

        <table class="subscriptions-table table table-sm table-hover mb-0">
          <thead>
            <tr>
              <th v-for="c in columns" :key="'column_' + c.key" :class="c.class + ' ' + (c.sortable ? 'sortable' : '')" :style="c.style" @click="c.sortable ? sortBy(c.key) : ''">
                <span :class="sortKey === c.key ? 'font-weight-bold' : ''">{{ c.label }}</span>
                <template v-if="c.sortable">
                  <i class="fas fa-sort-up align-bottom" v-if="sortOrders[c.key] > 0"></i>
                  <i class="fas fa-sort-down align-text-top" v-if="sortOrders[c.key] < 0"></i>
                </template>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="s in filteredSubscriptions" :key="'subscriptions_table' + s.id">
              <td>
                <img :src="s.feed.favicon" width="16" height="16" class="mr-1 align-text-top" v-if="s.feed.favicon !== null" />
                <i class="far fa-file-alt ml-1 mr-2" v-if="s.feed.favicon === null"></i>
                {{ s.feed.title }}
              </td>
              <td class="text-right">
                <span :title="s.feed.publishedAt | format('YYYY/MM/DD HH:mm:ss Z')">{{ s.feed.publishedAt | fromNow }}</span>
              </td>
              <td class="text-right">
                <span :title="s.createdAt | format('YYYY/MM/DD HH:mm:ss Z')">{{ s.createdAt | fromNow }}</span>
              </td>
              <td class="text-center">
                <span class="badge badge-success" v-if="s.feed.status === 'NORMAL'" :id="'status-normal'+ s.id"><i class="far fa-check-circle"></i> NORMAL</span>
                <b-tooltip triggers="hover" :target="'status-normal'+ s.id">フィードは正常です</b-tooltip>
                <span class="badge badge-danger" v-if="s.feed.status === 'BROKEN'" :id="'status-broken'+ s.id"><i class="far fa-times-circle"></i> BROKEN</span>
                <b-tooltip triggers="hover" :target="'status-broken'+ s.id">フィードのクロールに失敗しました<br />フィードが壊れている可能性があります</b-tooltip>
              </td>
              <td class="text-center">
                <a :href="s.feed.url" target="_blank" class="mr-1" title="サイト URL"><i class="fa fa-globe"></i></a>
                <a :href="s.feed.feedUrl" target="_blank" class="mr-1" title="フィード URL"><i class="fa fa-file-code"></i></a>
                <a href="javascript:void(0)" @click="openSubscriptionModal(s)" class="mr-1" title="フィード情報"><i class="fa fa-info-circle"></i></a>
                <a href="javascript:void(0)" @click="openUnsubscribeModal(s)" title="購読停止"><i class="fa fa-trash-alt"></i></a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import SubscriptionModal from './SubscriptionModal'
import UnsubscribeModal from './UnsubscribeModal'
import { mapState } from 'vuex'
import SubscriptionSorter from '../../SubscriptionSorter'

const sorter = new SubscriptionSorter()

export default {
  components: {
    'subscription-modal': SubscriptionModal,
    'unsubscribe-modal': UnsubscribeModal
  },
  data: function () {
    const columns = [
      { key: 'title', label: 'タイトル', class: '', style: {}, sortable: true },
      { key: 'publishedAt', label: '最終更新日', class: 'text-right', style: {'width': '130px'}, sortable: true },
      { key: 'createdAt', label: 'フィード登録日', class: 'text-right', style: {'width': '130px'}, sortable: true },
      { key: 'status', label: 'ステータス', class: 'text-center', style: {'width': '100px'}, sortable: true },
      { key: 'operation', label: '操作', class: 'text-center', style: {'width': '85px'}, sortable: false }
    ]
    let sortOrders = {}
    columns.forEach(function (column) {
      sortOrders[column.key] = 1
    })
    return {
      searchQuery: '',
      columns: columns,
      sortKey: '',
      sortOrders: sortOrders,
      subscriptionModal: false,
      unsubscribeModal: false,
      targetSubscription: null
    }
  },
  created () {
    this.$store.dispatch('GET_SUBSCRIPTIONS')
  },
  computed: {
    ...mapState(['subscriptions']),
    filteredSubscriptions () {
      const filteredList = this.subscriptions
        .filter(s => {
          const title = s.feed.title.toLowerCase()
          const searchQuery = this.searchQuery.toLowerCase()
          return title.indexOf(searchQuery) > -1
        })

      let subscriptionSortKey = null
      if (this.sortKey === 'title') {
        if (this.sortOrders[this.sortKey] > 0) {
          subscriptionSortKey = SubscriptionSorter.sortKey.TITLE_ASC
        } else {
          subscriptionSortKey = SubscriptionSorter.sortKey.TITLE_DESC
        }
      }
      if (this.sortKey === 'publishedAt') {
        if (this.sortOrders[this.sortKey] > 0) {
          subscriptionSortKey = SubscriptionSorter.sortKey.UPDATED_AT_ASC
        } else {
          subscriptionSortKey = SubscriptionSorter.sortKey.UPDATED_AT_DESC
        }
      }
      if (this.sortKey === 'createdAt') {
        if (this.sortOrders[this.sortKey] > 0) {
          subscriptionSortKey = SubscriptionSorter.sortKey.CREATED_AT_ASC
        } else {
          subscriptionSortKey = SubscriptionSorter.sortKey.CREATED_AT_DESC
        }
      }
      if (this.sortKey === 'status') {
        if (this.sortOrders[this.sortKey] > 0) {
          subscriptionSortKey = SubscriptionSorter.sortKey.STATUS_ASC
        } else {
          subscriptionSortKey = SubscriptionSorter.sortKey.STATUS_DESC
        }
      }
      return sorter.sort(filteredList, subscriptionSortKey)
    }
  },
  methods: {
    sortBy (key) {
      this.sortKey = key
      this.sortOrders[key] = this.sortOrders[key] * -1
    },
    openSubscriptionModal (subscription) {
      this.targetSubscription = subscription
      this.subscriptionModal = true
    },
    openUnsubscribeModal (subscription) {
      this.targetSubscription = subscription
      this.unsubscribeModal = true
    }
  }
}
</script>

<style >
.subscriptions-table th {
  font-weight: normal;
}

.sortable {
  cursor: pointer;
}
</style>
