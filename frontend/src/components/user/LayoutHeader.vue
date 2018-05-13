<template>
  <header class="header fixed-top d-flex flex-row justify-content-between align-items-center">
    <div class="logo">
      <a href="/#/dashboard"><i class="fa fa-rss-square logo-icon"></i>Feedich</a>
      <span class="description">Feedich is "simple" and "snappy" feed reader.</span>
    </div>

    <ul class="header-navi d-flex flex-row">
      <li>
        <b-button variant="link" to="/settings"><i class="fa fa-cogs"></i></b-button>
      </li>
      <li>
        <form v-bind:action="logoutUrl" method="post">
          <input type="hidden" name="_csrf" :value="xsrfToken" />
          <b-button variant="link" type="submit"><i class="fa fa-sign-out-alt"></i></b-button>
        </form>
      </li>
    </ul>
  </header>
</template>

<script>
export default {
  data () {
    return {
      logoutUrl: process.env.API_BASE_URL + '/logout'
    }
  },
  computed: {
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
</style>
