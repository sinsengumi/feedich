<template>
  <div class="subscriptions">
    <subscription-modal v-if="targetSubscription !== null" :subscription="targetSubscription" :modal-visible="subscriptionModal" @close="subscriptionModal = false"></subscription-modal>
    <unsubscribe-modal v-if="targetSubscription !== null" :subscription="targetSubscription" :modal-visible="unsubscribeModal" @close="unsubscribeModal = false"></unsubscribe-modal>

    <div class="card">
      <h5 class="card-header">購読フィード ({{ subscriptions.length }})</h5>
      <div class="card-body">
        <b-table :items="subscriptions" :fields="fields" hover small show-empty empty-text="No Results" class="mb-0">
          <template slot="title" slot-scope="data">
            <img :src="data.item.feed.favicon" width="16" height="16" class="mr-1" /> {{ data.item.feed.title }}
          </template>
          <template slot="createdAt" slot-scope="data">
            <span :title="data.value | format('YYYY/MM/DD HH:mm.ss Z')">{{ data.value | fromNow }}</span>
          </template>
          <template slot="operation" slot-scope="data">
            <a :href="data.item.feed.url" target="_blank" class="mr-1" title="サイト URL"><i class="fa fa-globe"></i></a>
            <a :href="data.item.feed.feedUrl" target="_blank" class="mr-1" title="フィード URL"><i class="fa fa-file-code"></i></a>
            <a href="javascript:void(0)" @click="openSubscriptionModal(data.item)" class="mr-1" title="フィード情報"><i class="fa fa-info-circle"></i></a>
            <a href="javascript:void(0)" @click="openUnsubscribeModal(data.item)" title="購読停止"><i class="fa fa-trash-alt"></i></a>
          </template>
        </b-table>
      </div>
    </div>
  </div>
</template>

<script>
import SubscriptionModal from './SubscriptionModal'
import UnsubscribeModal from './UnsubscribeModal'
import { mapState } from 'vuex'

export default {
  components: {
    'subscription-modal': SubscriptionModal,
    'unsubscribe-modal': UnsubscribeModal
  },
  data () {
    return {
      fields: [
        { key: 'title', label: 'タイトル', thClass: 'title-th', tdClass: 'title-td' },
        { key: 'createdAt', label: 'フィード登録日', thClass: 'createdAt-th', tdClass: 'createdAt-td' },
        { key: 'operation', label: '操作', thClass: 'operation-th', tdClass: 'operation-td' }
      ],
      subscriptionModal: false,
      unsubscribeModal: false,
      targetSubscription: null
    }
  },
  created () {
    this.$store.dispatch('GET_SUBSCRIPTIONS')
  },
  computed: {
    ...mapState(['subscriptions'])
  },
  methods: {
    filterTitle (subscriptions, search) {
      const fileterWord = search.toLowerCase()
      return subscriptions.filter((subscription) => {
        const title = subscription.feed.title.toLowerCase()
        return title.indexOf(fileterWord) > -1
      })
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

<style>
.title-th {
  padding: 5px 10px!important;
}

.title-td {
  padding: 5px 10px!important;
}

.createdAt-th {
  width: 140px;
  text-align: right;
  padding: 5px 10px!important;
}

.createdAt-td {
  text-align: right;
  padding: 5px 10px!important;
}

.operation-th {
  width: 120px;
  text-align: center;
  padding: 5px 10px!important;
}

.operation-td {
  text-align: center;
  padding: 5px 10px!important;
}
</style>
