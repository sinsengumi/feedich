<template>
  <div style="padding-top: 15px">
    <v-card>
      <v-card-title class="pb-0">
        <p class="title">Pins ({{ pins.length }})</p>
        <v-spacer></v-spacer>
        <v-btn depressed small @click="readPins"><v-icon class="mr-1" style="font-size: 11px;">fas fa-external-link-alt</v-icon>Read {{ ellipsedPins.length }} pins (Open new windows)</v-btn>
        <v-btn depressed small @click="clearPins"><v-icon class="mr-1" style="font-size: 11px;">fas fa-trash-alt</v-icon>Clear pins</v-btn>
      </v-card-title>

      <v-data-table v-if="pins != null" :items="pins" hide-actions hide-headers>

        <template slot="items" slot-scope="props">
          <td class="pin-list-td">
            <a @click="readPin(props.item)"><v-icon style="font-size: 11px; margin: 0 2px 2px 0;">far fa-file-alt</v-icon> {{ props.item.title }}</a>
          </td>
          <td class="text-xs-center pin-list-td" style="width: 160px;">{{ props.item.createdAt | fromNow }}</td>
          <td class="text-xs-center pin-list-td px-0" style="width: 40px;">
            <v-btn small icon class="ma-0" @click="removePin(props.item)">
              <v-icon small color="pink">fas fa-trash-alt</v-icon>
            </v-btn>
          </td>
        </template>
      </v-data-table>
    </v-card>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  computed: {
    ...mapState(['pins']),
    ellipsedPins () {
      return this.pins.slice(0, 10)
    }
  },
  methods: {
    readPin (pin) {
      window.open(pin.url, '_blank')
      this.$store.dispatch('REMOVE_PIN', {'pin': pin})
    },
    removePin (pin) {
      this.$store.dispatch('REMOVE_PIN', {'pin': pin})
    },
    readPins () {
      this.ellipsedPins.forEach(pin => {
        window.open(pin.url, '_blank')
        this.$store.dispatch('REMOVE_PIN', {'pin': pin})
      })
    },
    clearPins () {
      if (window.confirm('Clear Pins ?')) {
        this.$store.dispatch('CLEAR_PINS')
      }
    }
  }
}
</script>

<style scoped>
.pin-list-td {
  padding: 2px 10px 1px 10px!important;
  height: auto;
  color: #6a737d;
}

</style>
