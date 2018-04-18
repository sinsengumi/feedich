<template>
  <v-dialog v-model="intDialogVisible" max-width="500px" v-if="subscription != null">
    <v-card>
      <v-card-title class="headline">Unsubscribe ?</v-card-title>
      <v-card-text class="pt-0">
        <strong>{{ subscription.feed.title }}</strong><br />
        <span class="caption">{{ subscription.feed.description }}</span></v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="green" flat @click.native="intDialogVisible = false">No</v-btn>
        <v-btn color="green" flat @click.native="unsubscribe(subscription)">Yes</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ApiClient from '../ApiClient'

export default {
  props: [
    'dialogVisible',
    'subscription'
  ],
  computed: {
    intDialogVisible: {
      get: function () {
        return this.dialogVisible
      },
      set: function (value) {
        if (!value) {
          this.$emit('close')
        }
      }
    }
  },
  methods: {
    unsubscribe (subscription) {
      const api = new ApiClient()
      api.unsubscribe(subscription.id)
        .then((response) => {
          this.intDialogVisible = false
          this.$emit('close', subscription)
          this.$eventHub.$emit('unsubscribe')
        })
        .catch((error) => {
          console.log(error)
          this.intDialogVisible = false
        })
    }
  }
}
</script>

<style scoped>
</style>
