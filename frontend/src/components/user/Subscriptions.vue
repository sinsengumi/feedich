<template>
  <div class="subscriptions">
    <subscription-modal :subscription="targetSubscription" :modal-visible="subscriptionModal" @close="subscriptionModal = false"></subscription-modal>
    <unsubscribe-modal :subscription="targetSubscription" :modal-visible="unsubscribeModal" @close="unsubscribeModal = false"></unsubscribe-modal>

    <div class="card">
      <h5 class="card-header">購読フィード ({{ subscriptions.length }})</h5>
      <div class="card-body">
        <b-table :items="subscriptions" :fields="fields" hover small show-empty empty-text="No results" class="mb-0">
          <template slot="title" slot-scope="data">
            <img :src="data.item.feed.favicon" width="16" height="16" class="mr-1" /> {{ data.item.feed.title }}
          </template>
          <template slot="createdAt" slot-scope="data">
            {{ data.value | fromNow }}
          </template>
          <template slot="operation" slot-scope="data">
            <div class="btn-group btn-group-sm" role="group">
              <a class="btn btn-primary" :href="data.item.feed.url" target="_blank" role="button"><i class="fas fa-globe"></i></a>
              <a class="btn btn-info" :href="data.item.feed.feedUrl" target="_blank" role="button"><i class="fas fa-file-code"></i></a>
              <button type="button" class="btn btn-success" @click="openSubscriptionModal(data.item)"><i class="fas fa-info-circle"></i></button>
              <button type="button" class="btn btn-danger" @click="openUnsubscribeModal(data.item)"><i class="fas fa-trash-alt"></i></button>
            </div>
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
        { key: 'title', thClass: 'title-th', tdClass: 'title-td' },
        { key: 'createdAt', thClass: 'createdAt-th', tdClass: 'createdAt-td' },
        { key: 'operation', thClass: 'operation-th', tdClass: 'operation-td' }
      ],
      subscriptionModal: false,
      unsubscribeModal: false,
      targetSubscription: null,
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
  width: 150px;
  text-align: center;
  padding: 5px 10px!important;
}

.operation-td {
  text-align: center;
  padding: 5px 10px!important;
}
</style>
