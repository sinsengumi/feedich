<template>
  <b-modal id="subscriptionModal" v-model="modalVisibleInner" :title="subscription.feed.title" hide-footer no-fade>
    <table class="table table-striped mb-0">
      <tbody>
        <tr>
          <th class="text-right" scope="row" style="width: 120px">概要</th>
          <td>{{ subscription.feed.description }}</td>
        </tr>
        <tr>
          <th class="text-right" scope="row">サイト URL</th>
          <td><a :href="subscription.feed.url" target="_blank">{{ subscription.feed.url }}</a></td>
        </tr>
        <tr>
          <th class="text-right" scope="row">フィード URL</th>
          <td><a :href="subscription.feed.feedUrl" target="_blank">{{ subscription.feed.feedUrl }}</a></td>
        </tr>
        <tr>
          <th class="text-right" scope="row">フィード種別</th>
          <td>{{ subscription.feed.feedType }}</td>
        </tr>
        <tr>
          <th class="text-right" scope="row">フィード登録日</th>
          <td>{{ subscription.feed.createdAt | format('YYYY/MM/DD HH:mm:ss Z') }}</td>
        </tr>
        <tr>
          <th class="text-right" scope="row">最終更新日</th>
          <td>{{ subscription.feed.updatedAt | format('YYYY/MM/DD HH:mm:ss Z') }}</td>
        </tr>
      </tbody>
    </table>
  </b-modal>
</template>

<script>
export default {
  props: [
    'modalVisible',
    'subscription'
  ],
  computed: {
    modalVisibleInner: {
      get: function () {
        return this.modalVisible
      },
      set: function (value) {
        if (!value) {
          this.$emit('close')
        }
      }
    }
  }
}
</script>

<style scoped>
</style>
