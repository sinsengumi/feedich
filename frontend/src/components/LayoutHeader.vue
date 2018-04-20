<template>
  <v-toolbar app dark fixed color="light-blue darken-2"
      :clipped-left="$vuetify.breakpoint.lgAndUp" height="46"
      class="elevation-1">

    <v-toolbar-title>
      <a @click="toIndex" class="index-link">
        <i class="fas fa-rss-square mr-2 ml-2"></i>
        <span class="logo">Feedich</span>
      </a>
      <span class="description">The nanka kakkoii eigo wo kokoni kaku.</span>
    </v-toolbar-title>

    <v-spacer />

    <v-btn icon>
      <v-icon>settings</v-icon>
    </v-btn>
  </v-toolbar>
</template>

<script>
export default {
  methods: {
    toIndex () {
      this.$router.push({name: 'Index'})
    }
  }
}
</script>

<style scoped>
.index-link {
  color: white;
}

.logo {
  font-family: 'Bree Serif', serif;
}

.description {
  font-size: 13px;
  color: #B3E5FC;
  font-family: 'Bree Serif', serif;
  margin-left: 10px;
}
</style>
