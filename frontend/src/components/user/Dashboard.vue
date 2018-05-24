<template>
  <div class="dashboard">

    <div class="card mb-3">
      <div class="card-body">
        <h5 class="card-title">お知らせ</h5>
        <table class="table table-sm mb-0">
          <tbody>
            <tr>
              <td class="text-center" style="width:100px">2018/05/30</td>
              <td class="text-center" style="width:80px"><span class="badge badge-info">お知らせ</span></td>
              <td>Feedich を正式リリースしました。<a href="#">詳細</a></td>
            </tr>
            <tr>
              <td class="text-center" style="width:100px">2018/05/30</td>
              <td class="text-center" style="width:80px"><span class="badge badge-info">お知らせ</span></td>
              <td>Feedich を正式リリースしました。<a href="#">詳細</a></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <template v-if="recommendFeeds.length > 0">
      <h5 class="card-title">あなたにおすすめのフィード <i class="fa fa-sync-alt ml-1" style="cursor: pointer" @click="getRecommendFeeds"></i></h5>
      <div class="row">
        <div class="col-3" v-for="(feed, index) in recommendFeeds" :key="'recommend_' + feed.id">
          <div class="card mb-4 box-shadow">
            <img class="card-img-top" style="height: 250px; width: 100%; display: block; object-fit: cover;" :src="feed.ogImage" />
            <div class="card-body">
              <p class="card-text">
                <a :href="feed.url" class="text-body" target="_blank">
                  <img :src="feed.favicon" class="align-text-bottom" width="16" height="16" v-if="feed.favicon !== null" />
                  <i class="far fa-file-alt mr-1" v-if="feed.favicon === null"></i>
                  {{ feed.title }}
                </a><br />
                <small class="text-muted">{{ feed.description }}</small>
              </p>
              <div class="d-flex justify-content-between align-items-center">
                <div class="btn-group">
                  <button type="button" class="btn btn-sm btn-outline-info" @click="subscribe(feed.feedUrl, index)">購読</button>
                </div>
                <small class="text-muted">最終更新日: <span :title="feed.publishedAt | format('YYYY/MM/DD HH:mm:ss Z')">{{ feed.publishedAt | fromNow }}</span></small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>

    <div class="alert alert-info mb-0" role="alert">
      <h4 class="alert-heading"><i class="fas fa-gift"></i> よろしければ寄付をお願いします</h4>
      <p class="mb-0">
        Feedich の開発は個人で行っています。サーバー代などの運営費を個人で払ったいるため、今後のサービス継続のためにお気持ちを頂ければ幸いです。<br />
        <a href="https://www.amazon.co.jp/gp/product/B004N3APGO/" target="_blank">Amazon ギフト券で支援する</a>
      </p>
    </div>
  </div>
</template>

<script>
import ApiClient from '../../ApiClient'

const api = new ApiClient()

export default {
  name: 'Dashboard',
  data () {
    return {
      recommendFeeds: []
    }
  },
  created () {
    this.getRecommendFeeds()
  },
  computed: {
  },
  methods: {
    getRecommendFeeds () {
      api.recommendFeeds()
        .then((response) => {
          this.recommendFeeds = response.data
        })
    },
    subscribe (feedUrl, index) {
      this.$store.dispatch('SUBSCRIBE', {feedUrl: feedUrl})
      this.recommendFeeds.splice(index, 1)
    }
  }
}
</script>

<style scoped>
</style>
