<template>
  <b-modal id="unsubscribeModal" ref="unsubscribeModal" v-model="modalVisibleInner" title="購読停止" @ok="unsubscribe" no-fade>
    <p><strong>{{ subscription.feed.title }}</strong></p>
    <p class="mb-0">を購読停止します。よろしいですか？</p>
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
  },
  methods: {
    unsubscribe (event) {
      event.preventDefault()
      this.$store.dispatch('UNSUBSCRIBE', {subscription: this.subscription})

      this.modalVisibleInner = false
      this.$emit('finishUnsubscribe')
    }
  }
}
</script>

<style scoped>
</style>
