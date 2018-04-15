<template>
  <div>

    <v-container fill-height v-if="loading">
      <v-progress-circular class="mx-auto" indeterminate :size="70" :width="7" color="blue"></v-progress-circular>
    </v-container>

    <v-alert v-if="error" type="error" :value="true">
      {{ error }}
    </v-alert>

    <div v-if="items">
      <v-layout>
        <v-flex>
          <v-card class="light-blue lighten-5">
            <v-card-title primary-title class="pt-3">
              <div>
                <h3 class="headline mb-0"><a :href="feed.url" target="_blank"><img v-bind:src="feed.favicon" width="16" height="16" /> {{ feed.title }}</a></h3>
                <div class="mt-1">{{ feed.description }}</div>
              </div>
            </v-card-title>
            <v-divider />

            <v-card-actions class="pt-0 pb-0">
              <v-container grid-list-md>
                <v-layout row wrap>
                  <v-flex xs9>
                    <v-chip disabled text-color="white" class="light-blue lighten-1" style="font-size:12px; height: 24px">
                      <v-icon small>description</v-icon>&nbsp;<strong>{{ items.length }} unread items</strong>
                    </v-chip>
                    <v-chip disabled text-color="white" class="light-blue lighten-1" style="font-size:12px; height: 24px">
                      <v-icon small>group</v-icon>&nbsp;<strong>1034 Subscribers</strong>
                    </v-chip>
                    <v-chip text-color="white" class="light-blue lighten-1" style="font-size:12px; height: 24px">
                      <v-icon small>info</v-icon>&nbsp;<strong>Feed Information</strong>
                    </v-chip>
                  </v-flex>
                  <v-flex xs3  text-xs-right>
                    <v-chip class="grey" text-color="white" style="font-size:12px; height: 24px">
                      <v-icon small>clear</v-icon>&nbsp;<strong>Unsubscribe</strong>
                    </v-chip>
                  </v-flex>
                </v-layout>
              </v-container>
            </v-card-actions>
          </v-card>
        </v-flex>
      </v-layout>

      <br />

      <v-expansion-panel focusable>
        <v-expansion-panel-content v-for="(i, index) in items" :key="'itemId_' + i.id">
          <div slot="header" :class="{title: i == activeItem}" @click="readItem(i, $event)">{{ i.title }}</div>

          <v-container grid-list-md>
                <v-layout row wrap>
                  <v-flex xs9>
                    <p>Author: {{ i.author }} | {{ i.publishedAt }} |  ピン | 記事詳細 | <v-btn flat small @click="unreadItem(i, $event)">未読にする</v-btn> | 共有</p>
                  </v-flex>
                  <v-flex xs3  text-xs-right>
                    <v-chip class="grey" text-color="white" style="font-size:12px; height: 24px">
                      <v-icon small>clear</v-icon>&nbsp;<strong>Unsubscribe</strong>
                    </v-chip>
                  </v-flex>
                </v-layout>
              </v-container>
          <v-card>
            <v-card-text><div class="caption" v-html="i.description"></div></v-card-text>
          </v-card>
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
      activeItem: null,
      feed: null
    }
  },
  created () {
    this.fetchData()

    this.$eventHub.$on('keyup13', () => {
      console.log('enter in item')
    })
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

      Promise.all([
        api.getFeed(feedId),
        api.getItems(feedId)
      ]).then(([feedResult, itemsResult]) => {
        this.feed = feedResult.data
        this.items = itemsResult.data
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.error = 'データの取得に失敗しました'
      })
    },
    readItem (item, event) {
      this.activeItem = item
      const api = new ApiClient()
      api.readItem(item.id)
        .then((response) => {
          console.log('read')
          this.$eventHub.$emit('readItem', item.feedId)
        })
        .catch((error) => {
          console.log(error)
        })
    },
    unreadItem (item, event) {
      const api = new ApiClient()
      api.unreadItem(item.id)
        .then((response) => {
          console.log('unread')
          this.$eventHub.$emit('unreadItem', item.feedId)
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
