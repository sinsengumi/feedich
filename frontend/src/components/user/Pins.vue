<template>
  <div class="star">
    <div class="card">
      <h5 class="card-header">スター ({{ pins.length }})</h5>
      <div class="card-body">
        <div class="mb-3 text-right">
          <button type="button" class="btn btn-sm btn-info" @click="readPins"><i class="fas fa-external-link-alt"></i> 記事を一気に読む（新規タブを開きます）</button>
          <button type="button" class="btn btn-sm btn-danger" @click="clearPins"><i class="fas fa-trash-alt"></i> 全削除</button>
        </div>

        <b-table :items="pins" :fields="fields" hover small show-empty empty-text="No results" thead-class="hidden-header" class="mb-0">
          <template slot="title" slot-scope="data">
            <a href="javascript:void(0)" @click="readPin(data.item)"><i class="far fa-file-alt mr-1"></i> {{ data.value }}</a>
          </template>
          <template slot="createdAt" slot-scope="data">
            {{ data.value | fromNow }}
          </template>
          <template slot="operation" slot-scope="data">
            <a href="javascript:void(0)" @click="removePin(data.item)"><i class="fas fa-trash-alt"></i></a>
          </template>
        </b-table>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      fields: [
        { key: 'title', tdClass: 'title-td' },
        { key: 'createdAt', tdClass: 'createdAt-td' },
        { key: 'operation', tdClass: 'operation-td' }
      ]
    }
  },
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
      if (window.confirm('スターを全削除します。よろしいですか？')) {
        this.$store.dispatch('CLEAR_PINS')
      }
    }
  }
}
</script>

<style>
.title-td {
  padding: 5px 10px!important;
}

.createdAt-td {
  width: 140px;
  text-align: right;
  padding: 5px 10px!important;
}

.operation-td {
  width: 30px;
  text-align: center;
  padding: 5px 10px!important;
}
</style>
