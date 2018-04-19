<template>
  <v-dialog v-model="innerDialogVisible" max-width="500px" v-if="subscription != null">
    <v-card>
      <v-card-title class="headline">Unsubscribe ?</v-card-title>
      <v-card-text class="pt-0">
        <strong>{{ subscription.feed.title }}</strong><br />
        <span class="caption">{{ subscription.feed.description }}</span></v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue" flat @click.native="innerDialogVisible = false">No</v-btn>
        <v-btn color="blue" flat @click.native="unsubscribe(subscription)">Yes</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from 'vuex'

export default {
  props: [
    'dialogVisible',
    'subscription'
  ],
  computed: {
    ...mapState(['subscriptions']),
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
  },
  methods: {
    unsubscribe (subscription) {
      this.$store.dispatch('UNSUBSCRIBE', {subscription: subscription})
      this.innerDialogVisible = false
    }
  }
}
</script>

<style scoped>
</style>
