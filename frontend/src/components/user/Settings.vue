<template>
  <div class="setting">
    <div class="card">
      <h5 class="card-header">設定</h5>
      <div class="card-body">
        <div class="row">
          <div class="col-3">
            <div class="nav flex-column nav-pills" role="tablist" aria-orientation="vertical">
              <a class="nav-link active" data-toggle="pill" href="#settings-view" @click="clickView">表示の設定</a>
              <a class="nav-link" data-toggle="pill" href="#settings-pin" @click="clickPin">ピンの設定</a>
              <a class="nav-link" data-toggle="pill" href="#settings-import-export">インポート・エクスポート</a>
              <a class="nav-link" data-toggle="pill" href="#settings-withdraw">退会</a>
            </div>
          </div>
          <div class="col-9">
            <div class="tab-content">
              <div class="tab-pane fade show active" id="settings-view" role="tabpanel">
                <div class="card">
                  <div class="card-body">
                    <h4 class="mb-4">表示の設定</h4>
                    <div class="d-flex flex-row align-items-center">
                      <strong class="text-right pr-3" style="width:220px;">記事のコンパクト表示</strong>
                      <div>
                        <div class="form-check">
                          <input class="form-check-input align-text-top" name="itemCompactView" type="radio" id="itemCompactViewOn" value="true" v-model="itemCompactView" />
                          <label class="form-check-label align-text-top" for="itemCompactViewOn">有効</label>
                        </div>
                        <div class="form-check">
                          <input class="form-check-input align-text-top" name="itemCompactView" type="radio" id="itemCompactViewOff" value="false" v-model="itemCompactView" />
                          <label class="form-check-label align-text-top" for="itemCompactViewOff">無効</label>
                        </div>
                      </div>
                    </div>
                    <hr />
                    <!-- <p>
                      時刻の相対表示
                    </p> -->
                    <!-- <div class="d-flex flex-row align-items-center">
                      <strong class="text-right pr-3" style="width:220px;">記事を既読にするタイミング</strong>
                      <div>
                        <div class="form-check">
                          <input class="form-check-input" type="radio" name="exampleRadios" id="exampleRadios1" value="option1" />
                          <label class="form-check-label" for="exampleRadios1">有効</label>
                        </div>
                        <div class="form-check">
                          <input class="form-check-input" type="radio" name="exampleRadios" id="exampleRadios2" value="option2" />
                          <label class="form-check-label" for="exampleRadios2">無効</label>
                        </div>
                      </div>
                    </div>
                    <hr /> -->
                    <div class="text-center">
                      <button type="button" class="btn btn-outline-primary btn-sm" @click="saveViewSettings">設定を保存</button>
                    </div>
                  </div>
                </div>
              </div>

              <div class="tab-pane fade" id="settings-pin" role="tabpanel">
                <div class="card">
                  <div class="card-body">
                    <h4 class="mb-4">ピンの設定</h4>
                    <div class="d-flex flex-row align-items-center">
                      <strong class="text-right pr-3" style="width:220px;">一気に読む際に開くタブの数</strong>
                      <div>
                        一度に <input type="text" class="form-control form-control-sm text-center" size="2" style="width: auto; display:inline" v-model="pinOpenCount" /> 件まで開く
                        <span class="text-danger ml-3" v-if="errorPin !== null">{{ errorPin }}</span>
                      </div>
                    </div>
                    <hr />
                    <div class="d-flex flex-row align-items-center">
                      <strong class="text-right pr-3" style="width:220px;">ポップアップ表示のテスト</strong>
                      <div>
                        <p class="mb-2">ピンを一気に読むためにブラウザのポップアップブロック機能を無効にする必要があります。<br /><u>https://feedich.com</u> のサイトに対してポップアップ表示を許可してください。</p>
                        <p class="mb-0"><a href="javascript:void(0)" @click="testPopup"><i class="fas fa-external-link-alt"></i> ポップアップ表示のテスト（新規タブが3つ開きます）</a></p>
                      </div>
                    </div>
                    <hr />
                    <div class="text-center">
                      <button type="button" class="btn btn-outline-primary btn-sm" @click="savePinSettings">設定を保存</button>
                    </div>
                  </div>
                </div>
              </div>

              <div class="tab-pane fade" id="settings-import-export" role="tabpanel">
                <div class="card">
                  <div class="card-body">
                    <h4 class="mb-4">インポート・エクスポート</h4>
                    <p>フィード情報のインポート・エクスポートは OPML 形式のみサポートしています。</p>
                    <div class="d-flex flex-row align-items-center">
                      <strong class="text-right pr-3" style="width:220px;">インポート</strong>
                      <div>
                        <div class="custom-file">
                          <input type="file" class="custom-file-input" />
                          <label class="custom-file-label" for="customFile"></label>
                        </div>
                      </div>
                    </div>
                    <hr />
                    <div class="d-flex flex-row align-items-center">
                      <strong class="text-right pr-3" style="width:220px;">エクスポート</strong>
                      <div>
                        <a :href="opmlExportUrl" class="btn btn-outline-primary btn-sm">エクスポート</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="tab-pane fade" id="settings-withdraw" role="tabpanel">
                <div class="card">
                  <div class="card-body">
                    <h4 class="mb-4">退会</h4>
                    <p class="mb-0">退会します。よろしいですか？<br />（購読中のフィード、ピンの情報などすべて削除されます）</p>
                    <hr />
                    <div class="text-center">
                      <button type="button" class="btn btn-outline-danger btn-sm" @click="withdraw">退会</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LocalStorage from '../../LocalStorage'
import ApiClient from '../../ApiClient'

const ls = new LocalStorage()
const api = new ApiClient()

export default {
  data () {
    return {
      itemCompactView: null,
      pinOpenCount: null,
      errorPin: null,
      opmlExportUrl: process.env.API_BASE_URL + '/api/subscriptions/export'
    }
  },
  created () {
    this.clickView()
    this.clickPin()
  },
  computed: {
  },
  methods: {
    clickView () {
      this.itemCompactView = String(ls.getUseItemCompactView())
    },
    clickPin () {
      this.pinOpenCount = ls.getPinOpenCount()
      this.errorPin = null
    },
    saveViewSettings () {
      ls.setUseItemCompactView(this.itemCompactView)
      this.$store.dispatch('SET_NOTIFY_MESSAGE', {message: '表示の設定を保存しました'})
    },
    savePinSettings () {
      if (isNaN(this.pinOpenCount)) {
        this.errorPin = '数値を入力してください'
        return
      }
      const pinOpenCountNum = Number(this.pinOpenCount)
      if (!Number.isInteger(pinOpenCountNum) || pinOpenCountNum < 1 || pinOpenCountNum > 30) {
        this.errorPin = '1 〜 30 の値を入力してください'
        return
      }
      ls.setPinOpenCount(this.pinOpenCount)
      this.$store.dispatch('SET_NOTIFY_MESSAGE', {message: 'ピンの設定を保存しました'})
    },
    testPopup () {
      for (let i = 0; i < 3; i++) {
        window.open('https://feedich.com', '_blank')
      }
    },
    withdraw () {
      if (window.confirm('退会します。よろしいですか？')) {
        api.withdraw()
        ls.clear()
        this.$router.push('/login')
      }
    }
  }
}
</script>

<style scoped>
</style>
