<template>
  <div fixed>
    <div class="loading" v-if="loading">
      <v-progress-circular indeterminate :size="70" :width="7" color="purple"></v-progress-circular>
    </div>

    <div v-if="items">
    <v-layout>
    <v-flex>
      <v-card>
        <v-card-title primary-title>
          <div>
            <h3 class="headline mb-0"><a href="#">{{ feed.title }}</a></h3>
            <div>{{ feed.description }}</div>
          </div>
        </v-card-title>
        <!-- <v-card-actions>
          <v-btn flat color="orange">Share</v-btn>
          <v-btn flat color="orange">Explore</v-btn>
        </v-card-actions> -->
      </v-card>
    </v-flex>
  </v-layout>

  <br />

  <v-expansion-panel >
  <v-expansion-panel-content v-for="i in items" :key="i.id">
      <div slot="header">{{ i.title }}</div>
    </v-expansion-panel-content>
    </v-expansion-panel>
    </div>
</div>
</template>

<script>
import ApiClient from '../ApiClient'

export default {
  name: 'Item',
  data () {
    return {
      loading: false,
      error: null,
      items: null,
      feed: null
    }
  },
  created () {
    this.fetchData()
  },
  watch: {
    '$route': 'fetchData'
  },
  methods: {
    fetchData () {
      this.items = null
      this.error = null
      this.loading = true

      const api = new ApiClient()
      const feedId = this.$route.params.feedId

      api.getFeed(feedId)
        .then(function (response) {
          this.feed = response.data
        }.bind(this))
        .catch(function (error) {
          this.error = error
        }.bind(this))

      api.getItems(feedId)
        .then(function (response) {
          this.items = response.data
          this.loading = false
        }.bind(this))
        .catch(function (error) {
          this.loading = false
          this.error = error
        }.bind(this))
    }
  }
}
</script>

<style scoped>
</style>
