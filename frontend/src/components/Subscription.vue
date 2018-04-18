<template>
  <div>

    <v-container fill-height v-if="loading">
      <v-progress-circular class="mx-auto" indeterminate :size="70" :width="7" color="blue"></v-progress-circular>
    </v-container>

    <v-alert v-if="error" type="error" :value="true">
      {{ error }}
    </v-alert>

    <v-card class="light-blue lighten-5" v-if="!loading && subscription != null">
      <v-card-title primary-title class="pt-3">
        <div>
          <h3 class="headline mb-0">
            <a :href="subscription.feed.url" target="_blank"><img :src="subscription.feed.favicon" width="16" height="16" /> {{ subscription.feed.title }}</a>
          </h3>
          <div class="mt-1">{{ subscription.feed.description }}</div>
        </div>
      </v-card-title>

      <v-divider />

      <v-card-actions class="pt-0 pb-0">
        <v-container grid-list-md>
          <v-layout row wrap>
            <v-flex xs9>
              <v-chip disabled text-color="white" class="light-blue lighten-1" style="font-size:12px; height: 24px">
                <v-icon small>description</v-icon>&nbsp;<strong>{{ unreadItemCount }} unread items</strong>
              </v-chip>

              <v-chip text-color="white" class="light-blue lighten-1" style="font-size:12px; height: 24px" @click="subscriptionDialog = true">
                <v-icon small>info</v-icon>&nbsp;<strong>Feed Information</strong>
              </v-chip>
              <subscription-dialog :dialog-visible="subscriptionDialog" :subscription="subscription" @close="closeSubscriptionDialog"></subscription-dialog>
            </v-flex>
            <v-flex xs3 class="text-xs-right">
              <unsubscribe-dialog :dialog-visible="unsubscribeDialog" :subscription="subscription" @close="closeUnsubscribeDialog"></unsubscribe-dialog>

              <v-chip class="grey text-lg-right" text-color="white" style="font-size:12px; height: 24px" @click="unsubscribeDialog = true">
                <v-icon small>clear</v-icon>&nbsp;<strong>Unsubscribe</strong>
              </v-chip>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-actions>
    </v-card>

  </div>
</template>

<script>
import ApiClient from '../ApiClient'
import SubscriptionDialog from './SubscriptionDialog'
import UnsubscribeDialog from './UnsubscribeDialog'

export default {
  props: ['subscriptionId', 'unreadItemCount'],
  components: {
    'subscription-dialog': SubscriptionDialog,
    'unsubscribe-dialog': UnsubscribeDialog
  },
  data () {
    return {
      loading: false,
      error: null,
      subscription: null,
      subscriptionDialog: false,
      unsubscribeDialog: false
    }
  },
  created () {
    this.fetchFeed()
  },
  methods: {
    fetchFeed () {
      this.loading = true
      this.error = null
      this.subscription = null

      const api = new ApiClient()
      api.getSubscription(this.subscriptionId)
        .then((response) => {
          this.subscription = response.data
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
          this.error = true
          console.log(error)
        })
    },
    closeSubscriptionDialog () {
      this.subscriptionDialog = false
    },
    closeUnsubscribeDialog (subscription) {
      if (subscription != null) {
        this.$router.push({name: 'Index'})
      }
      this.unsubscribeDialog = false
    }
  }
}
</script>

<style scoped>
</style>
