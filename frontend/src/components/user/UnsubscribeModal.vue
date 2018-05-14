<template>
  <b-modal id="unsubscribeModal" title="購読停止" @ok="unsubscribe" no-fade>
    <p><strong>{{ subscription.feed.title }}</strong></p>
    <p>を購読停止します。よろしいですか？</p>
  </b-modal>
</template>

<script>
export default {
  props: [
    'subscription'
  ],
  methods: {
    unsubscribe (event) {
      event.preventDefault()
      this.$store.dispatch('UNSUBSCRIBE', {subscription: this.subscription})
      this.$router.push({name: 'Index'})
    }
  }
}
</script>

<style scoped>
</style>
