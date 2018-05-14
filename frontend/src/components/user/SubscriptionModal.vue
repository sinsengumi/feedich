<template>
  <b-modal id="subscriptionModal" :title="subscription.feed.title" hide-footer no-fade>
    <table class="table table-striped">
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
          <td>{{ subscription.feed.createdAt | format('YYYY/MM/DD HH:mm.ss Z') }}</td>
        </tr>
        <tr>
          <th class="text-right" scope="row">最終更新日</th>
          <td>{{ subscription.feed.updatedAt | format('YYYY/MM/DD HH:mm.ss Z') }}</td>
        </tr>
      </tbody>
    </table>
  </b-modal>
</template>

<script>
export default {
  props: [
    'subscription'
  ]
}
</script>

<style scoped>
</style>
