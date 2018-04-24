<template>
  <div>
    <v-tooltip v-model="floatPins" right>
      <v-btn depressed small color="light-blue lighten-5" class="ma-0 pt-0" @click="readPins" slot="activator">
        <v-icon left class="mr-1" color="light-blue lighten-2" data-fa-transform="rotate-45" style="height: 11px;">fas fa-thumbtack</v-icon>
        &nbsp;&nbsp;
        <span>{{ pins.length }}</span>
      </v-btn>
      <ul>
        <li v-for="(pin) in pins" :key="pin.title">{{ pin.title }}</li>
      </ul>
    </v-tooltip>

    <v-btn depressed small color="light-blue lighten-5" class="ma-0 pt-0" @click="toPins">
      All Pins
    </v-btn>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      loading: false,
      floatPins: false
    }
  },
  computed: mapState(['pins']),
  methods: {
    readPins () {
      alert('Pin 一気読み')
    },
    toPins () {
      this.$router.push({name: 'Pins'})
    }
  }
}
</script>

<style scoped>
</style>
