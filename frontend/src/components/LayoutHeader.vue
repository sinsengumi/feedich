<template>
  <v-toolbar app dark fixed color="light-blue darken-1"
      :clipped-left="$vuetify.breakpoint.lgAndUp" height="54"
      class="elevation-1">

    <v-toolbar-title>
      <v-toolbar-side-icon>
        <v-icon>rss_feed</v-icon>
      </v-toolbar-side-icon>
      <span>Feedich</span>
    </v-toolbar-title>

    <v-spacer />

    <v-btn icon>
      <v-icon>settings</v-icon>
    </v-btn>
  </v-toolbar>
</template>

<script>
</script>

<style scoped>
</style>
