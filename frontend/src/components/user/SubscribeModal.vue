<template>
  <b-modal id="subscribeModal" ref="subscribeModal" title="購読" @shown="shown" @hidden="clearData" no-fade hide-footer>
    <div class="d-flex">
      <input type="text" ref="discoverUrl" v-model="discoverUrl" class="form-control form-control-sm mr-2" placeholder="サイトの URL または フィードの URL を指定してください" @keyup.enter="discoverFeed" />
      <button type="button" class="btn btn-sm btn-info" @click="discoverFeed">検索</button>
    </div>

    <div v-if="loading" class="d-flex justify-content-center align-items-center" style="height: 100px">
      <i class="fas fa-circle-notch fa-spin text-muted fa-3x"></i>
    </div>

    <div class="card mt-3" v-if="discoveredFeeds !== null && discoveredFeeds.length === 0">
      <div class="card-body">
        <p class="card-text text-danger">フィードが見つかりませんでした。<br />URL が正しいか確認してください。</p>
      </div>
    </div>

    <div class="card mt-3" v-if="discoveredFeeds !== null && discoveredFeeds.length > 0">
      <div class="card-body">
        <p class="card-text">{{ discoveredFeeds.length }}件のフィードが見つかりました。<br />購読したいフィードを選択してください。</p>
        <div class="list-group">
          <a href="javascript:void(0)" @click="subscribe(feed)" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" :key="'discoveredFeeds_' + feed.title" v-for="feed in discoveredFeeds" :class="feed.subscribed ? 'list-group-item-secondary' : ''">
            <img :src="feed.favicon" width="16" height="16" />
            <div class="ml-2 mr-1"><span v-if="feed.subscribed">[ 購読中 ]</span> {{ feed.title }}<br /><small class="text-secondary">{{ feed.description }}</small></div>
            <span class="badge badge-secondary ml-auto">{{ feed.feedType }}</span>
          </a>
        </div>
      </div>
    </div>
  </b-modal>
</template>

<script>
import ApiClient from '../../ApiClient'

export default {
  data () {
    return {
      discoverUrl: null,
      discoveredFeeds: null,
      loading: false
    }
  },
  methods: {
    shown () {
      this.clearData()
      this.$refs.discoverUrl.focus()
    },
    clearData () {
      this.discoverUrl = null
      this.discoveredFeeds = null
      this.loading = false
    },
    discoverFeed () {
      if (this.discoverUrl === null || this.discoverUrl.length === 0) {
        return
      }

      this.loading = true
      this.error = false
      this.discoveredFeeds = null

      const api = new ApiClient()
      api.discoverFeeds(this.discoverUrl)
        .then((response) => {
          this.discoveredFeeds = response.data
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    subscribe (feed) {
      if (!feed.subscribed) {
        this.$store.dispatch('SUBSCRIBE', {feedUrl: feed.feedUrl})
      }

      this.$refs.subscribeModal.hide()
    }
  }
}
</script>

<style scoped>
</style>
