<template>
  <div style="padding-top: 15px">
    <subscription-dialog :dialog-visible="subscriptionDialog" :subscription="targetSubscription" @close="subscriptionDialog = false"></subscription-dialog>
    <unsubscribe-dialog :dialog-visible="unsubscribeDialog" :subscription="targetSubscription" @close="unsubscribeDialog = false"></unsubscribe-dialog>

    <v-card>
      <v-card-title class="pb-0">
        <p class="title">Subscriptions</p>
        <v-spacer></v-spacer>
        <v-text-field append-icon="search" label="Search" single-line hide-details v-model="search"></v-text-field>
      </v-card-title>

      <v-data-table v-if="subscriptions != null"
        :headers="headers"
        :items="subscriptions"
        :rows-per-page-items="rowsPerPageItems"
        disable-initial-sort
        :search="search"
        :custom-filter="filterTitle">

        <template slot="items" slot-scope="props">
          <td style="height: 35px">
            <img :src="props.item.feed.favicon" width="16" height="16" style="vertical-align: middle;" />&nbsp;
            {{ props.item.feed.title }}
          </td>

          <td class="text-xs-center" style="width: 160px; height: 35px">{{ props.item.createdAt | fromNow }}</td>

          <td class="text-xs-center px-0" style="width: 140px; height: 35px">
            <a :href="props.item.feed.url" target="_blank">
              <v-btn small icon class="mx-0 mt-0 mb-0">
                <v-icon small color="green">fas fa-globe</v-icon>
              </v-btn>
            </a>
            <a :href="props.item.feed.feedUrl" target="_blank">
              <v-btn small icon class="mx-0 mt-0 mb-0">
                <v-icon small color="teal">fas fa-file-code</v-icon>
              </v-btn>
            </a>
            <v-btn small icon class="mx-0 mt-0 mb-0" @click="openSubscriptionDialog(props.item)">
              <v-icon small color="blue">fas fa-info-circle</v-icon>
            </v-btn>
            <v-btn small icon class="mx-0 mt-0 mb-0" @click="openUnsubscribeDialog(props.item)">
              <v-icon small color="pink">fas fa-trash-alt</v-icon>
            </v-btn>
          </td>
        </template>
      </v-data-table>
    </v-card>
  </div>
</template>

<script>
import SubscriptionDialog from './SubscriptionDialog'
import UnsubscribeDialog from './UnsubscribeDialog'
import { mapState } from 'vuex'

export default {
  components: {
    'subscription-dialog': SubscriptionDialog,
    'unsubscribe-dialog': UnsubscribeDialog
  },
  data () {
    return {
      search: '',
      headers: [
        {
          text: 'Title',
          align: 'left',
          value: 'feed.title',
          sortable: true
        },
        {
          text: 'Subscribed',
          align: 'center',
          value: 'createdAt',
          sortable: true
        },
        {
          text: 'Action',
          align: 'center',
          sortable: false
        }
      ],
      rowsPerPageItems: [20, 50, 100, {'text': 'All', 'value': -1}],
      subscriptionDialog: false,
      unsubscribeDialog: false,
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
    openSubscriptionDialog (subscription) {
      this.subscriptionDialog = true
      this.targetSubscription = subscription
    },
    openUnsubscribeDialog (subscription) {
      this.unsubscribeDialog = true
      this.targetSubscription = subscription
    }
  }
}
</script>

<style scoped>
</style>
