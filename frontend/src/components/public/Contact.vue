<template>
  <div class="container p-4">
    <div class="row">
      <div class="col-10 offset-1">
        <h5 class="mb-3">お問い合わせ</h5>
        <p>Feedich に関するお問い合わせや不具合報告は以下のフォームからお願いします。</p>

        <form>
          <div class="form-group row">
            <label for="email" class="col-3 col-form-label text-right">返信用メールアドレス</label>
            <div class="col-7">
              <input type="email" class="form-control form-control-sm" id="email" v-model="email" />
              <small class="text-danger" v-if="emailError !== null">{{ emailError }}</small>
            </div>
          </div>
          <div class="form-group row">
            <label for="message" class="col-3 col-form-label text-right">お問い合わせ内容</label>
            <div class="col-7">
              <textarea class="form-control form-control-sm" id="message" rows="5" v-model="message"></textarea>
              <small class="text-danger" v-if="messageError !== null">{{ messageError }}</small>
            </div>
          </div>
          <div class="form-group row" v-if="error !== null">
            <div class="offset-3 col-7">
              <p class="mb-0 text-danger">{{ error }}</p>
            </div>
          </div>
          <div class="form-group row">
            <div class="offset-3 col-7">
              <button type="button" class="btn btn-outline-primary btn-sm" @click="contactPost">送信</button>
              <span class="text-success ml-2" v-if="success">送信しました。</span>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import ApiClient from '../../ApiClient'

const api = new ApiClient()

export default {
  name: 'Contact',
  data () {
    return {
      email: null,
      emailError: null,
      message: null,
      messageError: null,
      error: null,
      success: false
    }
  },
  methods: {
    contactPost () {
      this.emailError = null
      this.messageError = null
      this.error = null
      this.success = false

      if (this.email === null || this.email.length === 0) {
        this.emailError = '返信用メールアドレスが未入力です'
      }
      if (this.message === null || this.message.length === 0) {
        this.messageError = 'お問い合わせ内容が未入力です'
      }
      if (this.message !== null && this.message.length > 4000) {
        this.messageError = 'お問い合わせ内容は4000文字未満で入力してください'
      }
      console.log(this.message.length)

      if (this.emailError === null && this.messageError === null) {
        api.contact(this.email, this.message)
          .then((response) => {
            this.email = null
            this.message = null
            this.success = true
          })
          .catch(() => {
            this.error = '入力内容に不備があります'
          })
      }
    }
  }
}
</script>

<style scoped>
</style>
