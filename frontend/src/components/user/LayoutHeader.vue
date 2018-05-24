<template>
  <header class="header fixed-top d-flex flex-row align-items-center">
    <div class="logo mr-auto">
      <a href="/#/dashboard" title="Feedich"><i class="fa fa-rss-square logo-icon"></i>Feedich</a>
      <span class="description">Feedich is "Simple" and "Snappy" Feed Reader.</span>
    </div>

    <div class="notify-message-area">
      <span class="balloon" v-html="notifyMessage"></span>
      <i class="fas fa-walking text-white fa-flip-horizontal"></i>
    </div>

    <ul class="header-navi d-flex flex-row">
      <li>
        <b-button id="keyboardShortcut" variant="link" title="キーボードショートカット"><i class="fas fa-keyboard"></i></b-button>

        <b-popover target="keyboardShortcut" placement="bottom" triggers="hover" title="キーボードショートカット">
          <table class="table table-borderless table-sm keyboard-shortcut">
            <tr>
              <td class="text-right">次のフィードに移動</td>
              <td><kbd>s</kbd></td>
            </tr>
            <tr>
              <td class="text-right">前のフィードに移動</td>
              <td><kbd>a</kbd></td>
            </tr>
            <tr>
              <td class="text-right">次のアイテムに移動</td>
              <td><kbd>Enter</kbd></td>
            </tr>
            <tr>
              <td class="text-right">前のアイテムに移動</td>
              <td><kbd>Shift + Enter</kbd></td>
            </tr>
            <tr>
              <td class="text-right">ピンを付ける/外す</td>
              <td><kbd>p</kbd></td>
            </tr>
            <tr>
              <td class="text-right">ピンを一気に読む</td>
              <td><kbd>o</kbd></td>
            </tr>
            <tr>
              <td class="text-right">購読停止</td>
              <td><kbd>Delete</kbd></td>
            </tr>
            <tr>
              <td class="text-right">フィード一覧の更新</td>
              <td><kbd>r</kbd></td>
            </tr>
          </table>
        </b-popover>
      </li>
      <li>
        <b-button variant="link" to="/settings" title="設定"><i class="fa fa-cogs"></i></b-button>
      </li>
      <li>
        <form v-bind:action="logoutUrl" method="post">
          <input type="hidden" name="_csrf" :value="xsrfToken" />
          <b-button variant="link" type="submit" title="ログアウト"><i class="fa fa-sign-out-alt"></i></b-button>
        </form>
      </li>
    </ul>
  </header>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      logoutUrl: process.env.API_BASE_URL + '/logout'
    }
  },
  computed: {
    ...mapState(['notifyMessage']),
    xsrfToken () {
      const cookies = document.cookie.split(';')
      for (let i = 0; i < cookies.length; i++) {
        const elem = cookies[i].split('=')
        if (elem[0].trim() === 'XSRF-TOKEN') {
          return unescape(elem[1])
        }
      }
    }
  }
}
</script>

<style scoped>
.notify-message-area {
  margin-right: 10px;
}

.notify-message-area i {
  font-size: 1.4em;
  line-height: .75em;
  vertical-align: -0.12em;
}

.balloon {
  display: inline-block;
  background-color: #fafbfc;
  position: relative;
  padding: 3px 8px;
  border-radius: 0.15rem;
  margin-right: 6px;
  max-width: 700px;
}

.balloon:after {
  border-left: 10px solid #fafbfc;
  border-top: 6px solid transparent;
  border-bottom: 6px solid transparent;
  content: '';
  margin-top: -10px;
  position: absolute;
  right: -6px;
  top: 50%;
}
</style>
