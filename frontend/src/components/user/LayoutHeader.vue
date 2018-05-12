<template>
  <header class="navbar navbar-expand navbar-dark flex-column flex-md-row bd-navbar pt-0 pb-0">
    <a class="navbar-brand logo" href="/#/dashboard">Feedich</a>
    <div><span class="description">Feedich is "simple" and "snappy" feed reader.</span></div>

    <ul class="navbar-nav flex-row ml-md-auto">
      <li>
        <b-button variant="link" class="nav-link" to="/settings"><v-icon>fa fa-cogs</v-icon></b-button>
      </li>
      <li>
        <form v-bind:action="logoutUrl" method="post">
          <input type="hidden" name="_csrf" :value="xsrfToken" />
          <b-button variant="link" class="nav-link pr-0" type="submit"><v-icon>fa fa-sign-out-alt</v-icon></b-button>
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
.logo {
  font-family: 'Bree Serif', serif;
}

.description {
  font-size: 12px;
  color: #cbbde2;
  font-family: 'Bree Serif', serif;
}
</style>
