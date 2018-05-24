<template>
  <div class="star">
    <div class="card">
      <h5 class="card-header">ピン ({{ pins.length }})</h5>
      <div class="card-body">
        <template v-if="pins.length === 0">
          <div class="alert alert-info mb-0" role="alert">
            ピンがありません。記事にピンを付けてみましょう！
          </div>
        </template>
        <template v-if="pins.length > 0">
          <div class="mb-3 text-right">
            <button type="button" class="btn btn-sm btn-outline-info" @click="readPins"><i class="fas fa-external-link-alt"></i> 一気に読む（新規タブを開きます）</button>
            <button type="button" class="btn btn-sm btn-outline-danger" @click="clearPins"><i class="fas fa-trash-alt"></i> 全削除</button>
          </div>

          <table class="table table-sm table-hover mb-0">
            <tbody>
              <tr v-for="(pin, index) in pins" :key="'pins_' + pin.title" :class="index + 1 === ellipsedPins.length ? 'open-threshold' : ''">
                <td><a href="javascript:void(0)" @click="readPin(pin)"><i class="far fa-file-alt mr-1"></i> {{ pin.title }}</a></td>
                <td class="text-right" style="width: 140px"><span :title="pin.createdAt | format('YYYY/MM/DD HH:mm:ss Z')">{{ pin.createdAt | fromNow }}</span></td>
                <td class="text-center" style="width: 30px"><a href="javascript:void(0)" @click="removePin(pin)" title="削除"><i class="fas fa-trash-alt"></i></a></td>
              </tr>
            </tbody>
          </table>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import LocalStorage from '../../LocalStorage'

const ls = new LocalStorage()

export default {
  computed: {
    ...mapState(['pins']),
    ellipsedPins () {
      return this.pins.slice(0, ls.getPinOpenCount())
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
      if (window.confirm('ピンを全削除します。よろしいですか？')) {
        this.$store.dispatch('CLEAR_PINS')
      }
    }
  }
}
</script>

<style>
.open-threshold {
  border-bottom: 3px solid #dee2e6;
}
</style>
