<template>
<div>
  <p>Subscriptions</p>

  <v-data-table v-if="subscriptions != null"
    :headers="headers"
    :items="subscriptions"
    class="elevation-1"
  >

    <template slot="items" slot-scope="props">
      <td style="height: 30px">
        <img :src="props.item.feed.favicon" width="16" height="16" style="vertical-align: middle;" />&nbsp;
        {{ props.item.feed.title }}
      </td>
      <td class="text-xs-right" style="width: 100px; height: 30px">2h ago</td>
      <td class="text-xs-center px-0" style="width: 130px; height: 30px">
      <v-btn icon class="mx-0 mt-0 mb-0">
            <v-icon color="green">public</v-icon>
          </v-btn>
          <v-btn icon class="mx-0 mt-0 mb-0">
            <v-icon color="teal">rss_feed</v-icon>
          </v-btn>
          <v-btn icon class="mx-0 mt-0 mb-0">
            <v-icon color="pink">delete</v-icon>
          </v-btn>
        </td>
    </template>
  </v-data-table>

  </div>
</template>

<script>
import ApiClient from '../ApiClient'

export default {
  name: 'Subscriptions',
  data () {
    return {
      subscriptions: null,
      loading: false,
      headers: [
        {
          text: 'Title',
          align: 'left',
          value: 'title'
        },
        {
          text: 'Updated',
          align: 'right',
          value: 'updated'
        },
        {
          text: 'Action',
          align: 'center',
          value: 'action'
        }
      ]
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
    }
  }
}
</script>

<style scoped>
</style>
