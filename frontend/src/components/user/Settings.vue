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
              <a class="nav-link" data-toggle="pill" href="#settings-import" @click="clickImport">インポート</a>
              <a class="nav-link" data-toggle="pill" href="#settings-export">エクスポート</a>
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

              <div class="tab-pane fade" id="settings-import" role="tabpanel">
                <div class="card">

                  <div class="card-body">
                    <h4 class="mb-4">インポート</h4>

                    <div v-if="latestImport === null || latestImport.status === 'FINISHED'">
                      <p>フィード情報のインポートは OPML 形式のみサポートしています。</p>
                      <div class="d-flex flex-row align-items-center">
                        <strong class="text-right pr-3" style="width:120px;">インポート</strong>
                        <b-form-file style="width:350px;" v-model="importOpmlFile" ref="importOpmlFile" placeholder="OPML 形式のファイルを選択してください"></b-form-file>
                        <button type="button" class="btn btn-outline-primary btn-sm ml-3" @click="importOpml">インポート</button>
                      </div>
                      <p class="text-danger mt-1" style="margin-left:120px;" v-if="errorImport !== null">{{ errorImport }}</p>
                    </div>

                    <!-- Import が終了している時 -->
                    <div class="card mt-4" v-if="latestImport !== null && latestImport.status === 'FINISHED'">
                      <div class="card-header d-flex align-items-center">
                        <span class="mr-2" style="cursor: pointer" @click="importedFeedsArea = !importedFeedsArea">
                          <i class="far fa-plus-square fa-lg" v-if="!importedFeedsArea"></i>
                          <i class="far fa-minus-square fa-lg" v-if="importedFeedsArea"></i>
                        </span>
                        <h5 class="mb-0">インポート完了 ({{ latestImport.importFeeds.length }} feeds)</h5>
                        <small class="ml-auto">完了日時 : {{ latestImport.updatedAt | format('YYYY/MM/DD HH:mm:ss')}}</small>
                      </div>

                      <div class="m-2 ml-3">
                        <span class="badge badge-success" v-if="importSuccessCount > 0">購読完了 {{ importSuccessCount }}件</span>
                        <span class="badge badge-primary" v-if="importAlreadySubscribedCount > 0">既に購読済 {{ importAlreadySubscribedCount }}件</span>
                        <span class="badge badge-danger" v-if="importFailedCount > 0">エラー {{ importFailedCount }}件</span>
                      </div>

                      <ul class="list-group list-group-flush" v-if="importedFeedsArea">
                        <li class="list-group-item list-group-item-action d-flex align-items-center" :key="'importFeed1_' + index" v-for="(feed, index) in latestImport.importFeeds">
                          <div><strong>{{ feed.title }}</strong><br /><small class="text-muted">{{ feed.xmlUrl }}</small></div>
                          <span class="ml-auto badge badge-success" v-if="feed.status === 'SUCCESS'">購読完了</span>
                          <span class="ml-auto badge badge-danger" v-if="feed.status === 'FAILED'">エラー</span>
                          <span class="ml-auto badge badge-primary" v-if="feed.status === 'ALREADY_SUBSCRIBED'">既に購読済</span>
                        </li>
                      </ul>
                    </div>

                    <!-- Import 中の時 -->
                    <div class="card" v-if="latestImport !== null && latestImport.status !== 'FINISHED'">
                      <div class="card-header d-flex align-items-center">
                        <span class="mr-2" style="cursor: pointer" @click="importingFeedsArea = !importingFeedsArea">
                          <i class="far fa-plus-square fa-lg" v-if="!importingFeedsArea"></i>
                          <i class="far fa-minus-square fa-lg" v-if="importingFeedsArea"></i>
                        </span>
                        <h5 class="mb-0">インポート中 ({{ latestImport.importFeeds.length }} feeds)</h5>
                        <small class="ml-auto">開始日時 : {{ latestImport.createdAt | format('YYYY/MM/DD HH:mm:ss')}}</small>
                      </div>

                      <div class="progress m-2">
                        <div class="progress-bar bg-info progress-bar-striped progress-bar-animated" role="progressbar" :style="'width:' + importPercentage + '%;'">{{ importPercentage }}%</div>
                      </div>

                      <ul class="list-group list-group-flush" v-if="importingFeedsArea">
                        <li class="list-group-item list-group-item-action d-flex align-items-center" :key="'importFeed2_' + index" v-for="(feed, index) in latestImport.importFeeds">
                          <div><strong>{{ feed.title }}</strong><br /><small class="text-muted">{{ feed.xmlUrl }}</small></div>
                          <span class="ml-auto badge badge-info" v-if="feed.status === 'QUEUED'">処理中</span>
                          <span class="ml-auto badge badge-success" v-if="feed.status === 'SUCCESS'">購読完了</span>
                          <span class="ml-auto badge badge-danger" v-if="feed.status === 'FAILED'">エラー</span>
                          <span class="ml-auto badge badge-primary" v-if="feed.status === 'ALREADY_SUBSCRIBED'">既に購読済</span>
                        </li>
                      </ul>
                    </div>

                  </div>

                </div>
              </div>

              <div class="tab-pane fade" id="settings-export" role="tabpanel">
                <div class="card">
                  <div class="card-body">
                    <h4 class="mb-4">エクスポート</h4>
                    <p>フィード情報のエクスポートは OPML 形式のみサポートしています。</p>
                    <div class="d-flex flex-row align-items-center">
                      <strong class="text-right pr-3" style="width:120px;">エクスポート</strong>
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
      importOpmlFile: null,
      latestImport: null,
      errorImport: null,
      importPollingIntervalId: null,
      importedFeedsArea: false,
      importingFeedsArea: true,
      opmlExportUrl: process.env.API_BASE_URL + '/api/subscriptions/export'
    }
  },
  computed: {
    importPercentage () {
      if (this.latestImport === null) {
        return 0;
      }
      const finishedCount = this.latestImport.importFeeds.filter(feed => feed.status !== 'QUEUED').length
      const allCount = this.latestImport.importFeeds.length
      return Math.floor((finishedCount / allCount) * 100)
    },
    importFailedCount () {
      if (this.latestImport === null) {
        return 0;
      }
      return this.latestImport.importFeeds.filter(feed => feed.status === 'FAILED').length
    },
    importSuccessCount () {
      if (this.latestImport === null) {
        return 0;
      }
      return this.latestImport.importFeeds.filter(feed => feed.status === 'SUCCESS').length
    },
    importAlreadySubscribedCount () {
      if (this.latestImport === null) {
        return 0;
      }
      return this.latestImport.importFeeds.filter(feed => feed.status === 'ALREADY_SUBSCRIBED').length
    }
  },
  created () {
    this.clickView()
    this.clickPin()
    this.clickImport()

    this.importPollingIntervalId = setInterval(() => {
      api.latestImport()
        .then((response) => {
          this.latestImport = response.data
        })
        .catch((error) => {
          this.errorImport = error.response.data.message
        })
    }, 1000)
  },
  beforeDestroy () {
    clearInterval(this.importPollingIntervalId)
  },
  methods: {
    clickView () {
      this.itemCompactView = String(ls.getUseItemCompactView())
    },
    clickPin () {
      this.pinOpenCount = ls.getPinOpenCount()
      this.errorPin = null
    },
    clickImport () {
      this.importOpmlFile = null
      this.errorImport = null
      this.importedFeedsArea = false
      this.importingFeedsArea = true
      if (this.$refs.importOpmlFile !== undefined) {
        this.$refs.importOpmlFile.reset()
      }
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
    importOpml () {
      this.errorImport = null
      if (this.importOpmlFile === null || this.importOpmlFile.size === 0) {
        this.errorImport = 'OPML ファイルが空です'
        return
      }

      api.importOpml(this.importOpmlFile)
        .then((response) => {
          this.latestImport = response.data

          this.importOpmlFile = null
        })
        .catch((error) => {
          this.errorImport = error.response.data.message

          this.importOpmlFile = null
        })
    },
    toggleImportFeedsArea () {
      this.$refs.importOpmlFile.reset()
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

<style>
.custom-file {
  height: calc(1.82rem + 2px);
}

.custom-file-input {
  height: calc(1.82rem + 2px);
}

.custom-file-label {
  height: calc(1.82rem + 2px);
  padding: 0.3rem 0.75rem;
}

.custom-file-label::after {
  height: 1.82rem;
  padding: 0.3rem 0.75rem;
}
</style>
