<template>
  <!-- <v-expansion-panel>
    <v-expansion-panel-content v-for="(s, i) in subscriptions" :key="i">
      <div slot="header"><img alt="N" v-bind:src="s.feed.favicon" width="16" height="16" /> {{ s.feed.title }}</div>
      <v-card>
        <v-card-text>{{ s.feed.description }}</v-card-text>
      </v-card>
    </v-expansion-panel-content>
  </v-expansion-panel> -->

  <p>Index</p>
</template>

<script>
import ApiClient from '../ApiClient'

export default {
  name: 'Index',
  data () {
    return {
      subscriptions: []
    }
  },
  created () {
    const api = new ApiClient()
    api.getSubscriptions(
      (response) => (this.subscriptions = response.data),
      (response) => console.log('=====================')
    )
  }
}
</script>

<style scoped>
</style>
