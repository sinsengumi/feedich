<template>
  <div>

    <v-dialog v-model="unsubscribeDialog" max-width="500px" v-if="targetSubscription != null">
      <v-card>
        <v-card-title class="headline">Unsubscribe ?</v-card-title>
        <v-card-text class="pt-0">
          <strong>{{ targetSubscription.feed.title }}</strong><br />
          <span class="caption">{{ targetSubscription.feed.description }}</span></v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green" flat @click.native="unsubscribeDialog = false">No</v-btn>
          <v-btn color="green" flat @click.native="unsubscribe(targetSubscription)">Yes</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

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
    :custom-filter="filterTitle"
    class="elevation-0">

    <template slot="items" slot-scope="props">
      <td style="height: 35px">
        <img :src="props.item.feed.favicon" width="16" height="16" style="vertical-align: middle;" />&nbsp;
        {{ props.item.feed.title }}
      </td>

      <td class="text-xs-center" style="width: 150px; height: 35px">{{ props.item.createdAt | fromNow }}</td>

      <td class="text-xs-center px-0" style="width: 130px; height: 35px">
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
import ApiClient from '../ApiClient'

export default {
  name: 'Subscriptions',
  data () {
    return {
      search: '',
      subscriptions: null,
      loading: false,
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
      rowsPerPageItems: [15, 30, 50, {'text': 'All', 'value': -1}],
      unsubscribeDialog: false,
      targetSubscription: null
    }
  },
  created () {
    this.fetchSubscriptions()
  },
  methods: {
    fetchSubscriptions () {
      this.loading = true
      this.subscriptions = null

      const api = new ApiClient()
      api.getSubscriptions()
        .then((response) => {
          this.subscriptions = response.data
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
          console.log(error)
        })
    },
    openUnsubscribeDialog (subscription) {
      this.unsubscribeDialog = true
      this.targetSubscription = subscription
    },
    unsubscribe (subscription) {
      this.targetSubscription = null
      const index = this.subscriptions.indexOf(subscription)

      const api = new ApiClient()
      api.unsubscribe(subscription.id)
        .then((response) => {
          this.subscriptions.splice(index, 1)
          this.unsubscribeDialog = false
          this.$eventHub.$emit('unsubscribe')
        })
        .catch((error) => {
          console.log(error)
          this.unsubscribeDialog = false
        })
    },
    filterTitle (items, search) {
      const fileterWord = search.toLowerCase()
      return items.filter((item) => {
        const title = item.feed.title.toLowerCase()
        return title.indexOf(fileterWord) > -1
      })
    }
  }
}
</script>

<style scoped>
</style>
