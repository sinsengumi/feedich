<template>
  <v-dialog v-model="innerDialogVisible" max-width="500px" v-if="subscription != null">
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
            <th class="text-xs-right pb-2">Subscribed</th>
            <td class="pl-2 pb-2">{{ subscription.feed.createdAt | format('YYYY/MM/DD HH:mm.ss Z') }}</td>
          </tr>
          <tr>
            <th class="text-xs-right pb-2">Last Updated</th>
            <td class="pl-2 pb-2">{{ subscription.feed.updatedAt | format('YYYY/MM/DD HH:mm.ss Z') }}</td>
          </tr>
        </table>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue" flat @click.native="innerDialogVisible = false">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props: [
    'dialogVisible',
    'subscription'
  ],
  computed: {
    innerDialogVisible: {
      get: function () {
        return this.dialogVisible
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
