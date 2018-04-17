<template>
  <div>

    <v-container fill-height v-if="loading">
      <v-progress-circular class="mx-auto" indeterminate :size="70" :width="7" color="blue"></v-progress-circular>
    </v-container>

    <v-alert v-if="error" type="error" :value="true">
      {{ error }}
    </v-alert>

    <div v-if="items">
      <v-btn flat small  class="ml-0 mr-0 mt-0 pt-0 ">
            <v-icon small class="mr-1" color="light-blue" data-fa-transform="rotate-45">fas fa-thumbtack</v-icon>123
          </v-btn>
          <v-btn flat small  class="ml-0 mr-0 mt-0 pt-0 ">
            All Pins
          </v-btn>
      <!-- <v-btn-toggle>

        <v-btn>
          <v-icon color="red">star</v-icon>
        </v-btn>
        <v-btn>
          <span>Open pins</span>
        </v-btn>
      </v-btn-toggle> -->

      <component-feed :subscription-id="$route.params.subscriptionId" :unread-item-count="unreadItemCount"></component-feed>

      <br />

      <v-expansion-panel focusable>
        <v-expansion-panel-content v-for="i in items" :key="'itemId_' + i.id">
          <div slot="header" :class="{title: i == activeItem}" @click="readItem(i, $event)" :ref="'itemRef_' + i.id">{{ i.title }}</div>

          <v-container grid-list-md>
                <v-layout row wrap>
                  <v-flex xs9>
                    <p>
                      Author: {{ i.author }} | {{ i.publishedAt }} |  ピン | 記事詳細 |
                      <v-btn v-if="! i.unread" flat small @click="unreadItem(i, $event)">未読にする</v-btn>
                      <v-btn v-if="i.unread" flat small @click="readItem(i, $event)">既読にする</v-btn>
                      | 共有</p>
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
import Feed from './Feed.vue'

export default {
  name: 'Item',
  components: {
    'component-feed': Feed
  },
  data () {
    return {
      loading: false,
      error: null,
      items: null,
      activeItem: null
    }
  },
  created () {
    this.fetchData()

    this.$eventHub.$on('shortcutEnter', this.shortcutEnter)
  },
  watch: {
    '$route': 'fetchData'
  },
  computed: {
    unreadItemCount () {
      return this.items.filter((item) => {
        return item.unread
      }).length
    }
  },
  methods: {
    fetchData () {
      this.items = null
      this.error = null
      this.loading = true

      const api = new ApiClient()
      const subscriptionId = this.$route.params.subscriptionId

      api.getItems(subscriptionId)
        .then((response) => {
          this.items = response.data
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
          console.log(error)
          this.error = 'データの取得に失敗しました'
        })
    },
    readItem (item, event) {
      this.activeItem = item

      if (this.activeItem.unread) {
        const api = new ApiClient()
        api.readItem(item.id)
          .then((response) => {
            console.log('read')
            this.activeItem.unread = false
            item.unread = false

            this.$eventHub.$emit('readItem', item.feedId, this.unreadItemCount)
          })
          .catch((error) => {
            console.log(error)
          })
      }
    },
    unreadItem (item, event) {
      if (!this.activeItem.unread) {
        const api = new ApiClient()
        api.unreadItem(item.id)
          .then((response) => {
            console.log('unread')
            this.activeItem.unread = true
            item.unread = true

            this.$eventHub.$emit('unreadItem', item.feedId, this.unreadItemCount)
          })
          .catch((error) => {
            console.log(error)
          })
      }
    },
    shortcutEnter () {
      // activeItem の次の要素を見つける
      let targetItemId = null
      if (this.activeItem == null) {
        if (this.items.length > 0) {
          targetItemId = this.items[0].id
        }
      } else {
        this.items.forEach((item, index, items) => {
          if (item.id === this.activeItem.id) {
            targetItemId = items[index + 1].id
          }
        })
      }

      if (targetItemId != null) {
        const key = 'itemRef_' + targetItemId
        const targetElement = this.$refs[key]
        targetElement[0].click()
      }
    }
  }
}
</script>

<style scoped>
</style>
