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

              <v-dialog v-model="feedInfoDialog" max-width="600">
                <v-chip text-color="white" class="light-blue lighten-1" style="font-size:12px; height: 24px" slot="activator">
                  <v-icon small>info</v-icon>&nbsp;<strong>Feed Information</strong>
                </v-chip>
                <v-card>
                  <v-card-title class="headline">Feed Information</v-card-title>
                  <v-card-text class="pt-0 pb-0">
                    <table>
                      <tr>
                        <th class="text-xs-right pb-2" style="width: 120px">Title</th>
                        <td class="pl-2 pb-2"><img :src="subscription.feed.favicon" width="16" height="16" /> {{ subscription.feed.title }}</td>
                      </tr>
                      <tr>
                        <th class="text-xs-right pb-2">Description</th>
                        <td class="pl-2 pb-2">{{ subscription.feed.description }}</td>
                      </tr>
                      <tr>
                        <th class="text-xs-right pb-2">Site URL</th>
                        <td class="pl-2 pb-2"><a :href="subscription.feed.url" target="_blank">{{ subscription.feed.url }}</a></td>
                      </tr>
                      <tr>
                        <th class="text-xs-right pb-2">Feed URL</th>
                        <td class="pl-2 pb-2"><a :href="subscription.feed.feedUrl" target="_blank">{{ subscription.feed.feedUrl }}</a></td>
                      </tr>
                      <tr>
                        <th class="text-xs-right pb-2">Feed Type</th>
                        <td class="pl-2 pb-2">{{ subscription.feed.feedType }}</td>
                      </tr>
                      <tr>
                        <th class="text-xs-right pb-2">Last Updated</th>
                        <td class="pl-2 pb-2">TODO</td>
                      </tr>
                    </table>
                  </v-card-text>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="green" flat @click.native="feedInfoDialog = false">Close</v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-flex>
            <v-flex xs3 class="text-xs-right">
              <v-dialog v-model="unsubscribeDialog" max-width="400">
                <v-chip class="grey text-lg-right" text-color="white" style="font-size:12px; height: 24px" slot="activator">
                  <v-icon small>clear</v-icon>&nbsp;<strong>Unsubscribe</strong>
                </v-chip>
                <v-card>
                  <v-card-title class="headline">Unsubscribe ?</v-card-title>
                  <v-card-text class="pt-0">
                    <strong>{{ subscription.feed.title }}</strong><br />
                    <span class="caption">{{ subscription.feed.description }}</span></v-card-text>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="green" flat @click.native="unsubscribeDialog = false">No</v-btn>
                    <v-btn color="green" flat @click.native="unsubscribe(subscription.id)">Yes</v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-actions>
    </v-card>

  </div>
</template>

<script>
import ApiClient from '../ApiClient'

export default {
  name: 'Feed',
  props: ['subscriptionId', 'unreadItemCount'],
  data () {
    return {
      loading: false,
      error: null,
      subscription: null,
      feedInfoDialog: false,
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
    unsubscribe (subscriptionId) {
      const api = new ApiClient()
      api.unsubscribe(subscriptionId)
        .then((response) => {
          this.unsubscribeDialog = false
          this.$router.push({name: 'Index'})

          this.$eventHub.$emit('unsubscribe')
        })
        .catch((error) => {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>
</style>
