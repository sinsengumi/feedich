<template>
  <div>
  <!-- <form class="bd-search d-flex align-items-center">
  <span class="algolia-autocomplete" style="position: relative; display: inline-block; direction: ltr;"><input type="search" class="form-control ds-input" id="search-input" placeholder="Search..." autocomplete="off" spellcheck="false" role="combobox" aria-autocomplete="list" aria-expanded="false" aria-owns="algolia-autocomplete-listbox-0" dir="auto" style="position: relative; vertical-align: top;"><pre aria-hidden="true" style="position: absolute; visibility: hidden; white-space: pre; font-family: -apple-system, system-ui, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 16px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: normal; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre><span class="ds-dropdown-menu" role="listbox" id="algolia-autocomplete-listbox-0" style="position: absolute; top: 100%; z-index: 100; display: none; left: 0px; right: auto;"><div class="ds-dataset-1"></div></span></span>
  <button class="btn btn-link bd-search-docs-toggle d-md-none p-0 ml-3" type="button" data-toggle="collapse" data-target="#bd-docs-nav" aria-controls="bd-docs-nav" aria-expanded="false" aria-label="Toggle docs navigation"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 30 30" width="30" height="30" focusable="false"><title>Menu</title><path stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-miterlimit="10" d="M4 7h22M4 15h22M4 23h22"></path></svg>
</button>
</form> -->
<nav class="collapse bd-links" id="bd-docs-nav">
<div class="bd-toc-item">
      <a class="bd-toc-link" href="/docs/4.0/getting-started/introduction/">
        Getting started
      </a>

      <b-list-group>
  <b-list-group-item class="d-flex justify-content-between align-items-center">
    Cras justo odio
    <b-badge variant="primary" pill>14</b-badge>
  </b-list-group-item>
  <b-list-group-item class="d-flex justify-content-between align-items-center">
    Dapibus ac facilisis in
    <b-badge variant="primary" pill>2</b-badge>
  </b-list-group-item>
  <b-list-group-item class="d-flex justify-content-between align-items-center">
    Morbi leo risus
    <b-badge variant="primary" pill>1</b-badge>
  </b-list-group-item>
</b-list-group>

      <ul class="nav bd-sidenav"><li>
            <a href="/docs/4.0/getting-started/introduction/">
              Introduction
            </a></li><li>
            <a href="/docs/4.0/getting-started/download/">
              Download
            </a></li><li>
            <a href="/docs/4.0/getting-started/contents/">
              Contents
            </a></li><li>
            <a href="/docs/4.0/getting-started/browsers-devices/">
              Browsers &amp; devices
            </a></li><li>
            <a href="/docs/4.0/getting-started/javascript/">
              JavaScript
            </a></li><li>
            <a href="/docs/4.0/getting-started/theming/">
              Theming
            </a></li><li>
            <a href="/docs/4.0/getting-started/build-tools/">
              Build tools
            </a></li><li>
            <a href="/docs/4.0/getting-started/webpack/">
              Webpack
            </a></li><li>
            <a href="/docs/4.0/getting-started/accessibility/">
              Accessibility
            </a></li></ul>
    </div><div class="bd-toc-item">
      <a class="bd-toc-link" href="/docs/4.0/layout/overview/">
        Layout
      </a>

      <ul class="nav bd-sidenav"><li>
            <a href="/docs/4.0/layout/overview/">
              Overview
            </a></li><li>
            <a href="/docs/4.0/layout/grid/">
              Grid
            </a></li><li>
            <a href="/docs/4.0/layout/media-object/">
              Media object
            </a></li><li>
            <a href="/docs/4.0/layout/utilities-for-layout/">
              Utilities for layout
            </a></li></ul>
    </div><div class="bd-toc-item">
      <a class="bd-toc-link" href="/docs/4.0/content/reboot/">
        Content
      </a>

      <ul class="nav bd-sidenav"><li>
            <a href="/docs/4.0/content/reboot/">
              Reboot
            </a></li><li>
            <a href="/docs/4.0/content/typography/">
              Typography
            </a></li><li>
            <a href="/docs/4.0/content/code/">
              Code
            </a></li><li>
            <a href="/docs/4.0/content/images/">
              Images
            </a></li><li>
            <a href="/docs/4.0/content/tables/">
              Tables
            </a></li><li>
            <a href="/docs/4.0/content/figures/">
              Figures
            </a></li></ul>
    </div><div class="bd-toc-item">
      <a class="bd-toc-link" href="/docs/4.0/components/alerts/">
        Components
      </a>

      <ul class="nav bd-sidenav"><li>
            <a href="/docs/4.0/components/alerts/">
              Alerts
            </a></li><li>
            <a href="/docs/4.0/components/badge/">
              Badge
            </a></li><li>
            <a href="/docs/4.0/components/breadcrumb/">
              Breadcrumb
            </a></li><li>
            <a href="/docs/4.0/components/buttons/">
              Buttons
            </a></li><li>
            <a href="/docs/4.0/components/button-group/">
              Button group
            </a></li><li>
            <a href="/docs/4.0/components/card/">
              Card
            </a></li><li>
            <a href="/docs/4.0/components/carousel/">
              Carousel
            </a></li><li>
            <a href="/docs/4.0/components/collapse/">
              Collapse
            </a></li><li>
            <a href="/docs/4.0/components/dropdowns/">
              Dropdowns
            </a></li><li>
            <a href="/docs/4.0/components/forms/">
              Forms
            </a></li><li>
            <a href="/docs/4.0/components/input-group/">
              Input group
            </a></li><li>
            <a href="/docs/4.0/components/jumbotron/">
              Jumbotron
            </a></li><li>
            <a href="/docs/4.0/components/list-group/">
              List group
            </a></li><li>
            <a href="/docs/4.0/components/modal/">
              Modal
            </a></li><li>
            <a href="/docs/4.0/components/navs/">
              Navs
            </a></li><li>
            <a href="/docs/4.0/components/navbar/">
              Navbar
            </a></li><li>
            <a href="/docs/4.0/components/pagination/">
              Pagination
            </a></li><li>
            <a href="/docs/4.0/components/popovers/">
              Popovers
            </a></li><li>
            <a href="/docs/4.0/components/progress/">
              Progress
            </a></li><li>
            <a href="/docs/4.0/components/scrollspy/">
              Scrollspy
            </a></li><li>
            <a href="/docs/4.0/components/tooltips/">
              Tooltips
            </a></li></ul>
    </div><div class="bd-toc-item active">
      <a class="bd-toc-link" href="/docs/4.0/utilities/borders/">
        Utilities
      </a>

      <ul class="nav bd-sidenav"><li class="active bd-sidenav-active">
            <a href="/docs/4.0/utilities/borders/">
              Borders
            </a></li><li>
            <a href="/docs/4.0/utilities/clearfix/">
              Clearfix
            </a></li><li>
            <a href="/docs/4.0/utilities/close-icon/">
              Close icon
            </a></li><li>
            <a href="/docs/4.0/utilities/colors/">
              Colors
            </a></li><li>
            <a href="/docs/4.0/utilities/display/">
              Display
            </a></li><li>
            <a href="/docs/4.0/utilities/embed/">
              Embed
            </a></li><li>
            <a href="/docs/4.0/utilities/flex/">
              Flex
            </a></li><li>
            <a href="/docs/4.0/utilities/float/">
              Float
            </a></li><li>
            <a href="/docs/4.0/utilities/image-replacement/">
              Image replacement
            </a></li><li>
            <a href="/docs/4.0/utilities/position/">
              Position
            </a></li><li>
            <a href="/docs/4.0/utilities/screenreaders/">
              Screenreaders
            </a></li><li>
            <a href="/docs/4.0/utilities/sizing/">
              Sizing
            </a></li><li>
            <a href="/docs/4.0/utilities/spacing/">
              Spacing
            </a></li><li>
            <a href="/docs/4.0/utilities/text/">
              Text
            </a></li><li>
            <a href="/docs/4.0/utilities/vertical-align/">
              Vertical align
            </a></li><li>
            <a href="/docs/4.0/utilities/visibility/">
              Visibility
            </a></li></ul>
    </div><div class="bd-toc-item">
      <a class="bd-toc-link" href="/docs/4.0/extend/approach/">
        Extend
      </a>

      <ul class="nav bd-sidenav"><li>
            <a href="/docs/4.0/extend/approach/">
              Approach
            </a></li><li>
            <a href="/docs/4.0/extend/icons/">
              Icons
            </a></li></ul>
    </div><div class="bd-toc-item">
      <a class="bd-toc-link" href="/docs/4.0/migration/">
        Migration
      </a>

      <ul class="nav bd-sidenav"></ul>
    </div><div class="bd-toc-item">
      <a class="bd-toc-link" href="/docs/4.0/about/overview/">
        About
      </a>

      <ul class="nav bd-sidenav"><li>
            <a href="/docs/4.0/about/overview/">
              Overview
            </a></li><li>
            <a href="/docs/4.0/about/brand/">
              Brand
            </a></li><li>
            <a href="/docs/4.0/about/license/">
              License
            </a></li><li>
            <a href="/docs/4.0/about/translations/">
              Translations
            </a></li></ul>
    </div></nav>
    </div>
</template>

<script>
import SubscribeDialog from './SubscribeDialog'
import { mapState } from 'vuex'
import LocalStorage from '../../LocalStorage'
import SubscriptionSorter from '../../SubscriptionSorter'

const ls = new LocalStorage()

export default {
  components: {
    'subscribe-dialog': SubscribeDialog
  },
  data () {
    return {
      loading: false,
      fileterWord: '',
      subscribeDialog: false
    }
  },
  computed: {
    ...mapState(['subscriptions', 'pins']),
    activeSubscriptionId () {
      return Number(this.$route.params.subscriptionId)
    },
    filteredSubscriptions () {
      const filteredList = this.subscriptions
        .filter(s => s.unreadCountOriginal > 0)
        .filter(s => {
          const title = s.feed.title.toLowerCase()
          const fileterWord = this.fileterWord.toLowerCase()
          return title.indexOf(fileterWord) > -1
        })

      const sorter = new SubscriptionSorter()
      return sorter.sort(filteredList, ls.getSubscriptionSortKey())
    },
    ellipsedPins () {
      return this.pins.slice(0, 10)
    }
  },
  methods: {
    fetchSubscriptions () {
      this.loading = true

      this.$store.dispatch('GET_SUBSCRIPTIONS')
        .then((response) => {
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    toItems (subscription) {
      this.$router.push({name: 'Items', params: { 'subscriptionId': subscription.id }})
    },
    sortSubscriptions (sortKey) {
      const tmp = this.subscriptions
      this.$store.commit('SET_SUBSCRIPTIONS', {subscriptions: null})
      ls.setSubscriptionSortKey(sortKey)
      this.$store.commit('SET_SUBSCRIPTIONS', {subscriptions: tmp})
    },
    isActiveSortKey (targetSortKey) {
      return ls.getSubscriptionSortKey() === targetSortKey
    },
    closeSubscribeDialog () {
      this.subscribeDialog = false
    },
    reloadSubscription () {
      this.fetchSubscriptions()
    },
    nextSubscription () {
      let targetSubscription = null
      if (this.activeSubscriptionId === null) {
        if (this.subscriptions.length > 0) {
          targetSubscription = this.filteredSubscriptions[0]
        }
      } else {
        const foundIndex = this.filteredSubscriptions.findIndex(s => s.id === this.activeSubscriptionId)
        if (foundIndex !== -1) {
          const targetIndex = foundIndex === this.filteredSubscriptions.length - 1 ? foundIndex : foundIndex + 1
          targetSubscription = this.filteredSubscriptions[targetIndex]
        } else {
          targetSubscription = this.filteredSubscriptions[0]
        }
      }

      if (targetSubscription !== null) {
        this.toItems(targetSubscription)
      }
    },
    prevSubscription () {
      let targetSubscription = null
      if (this.activeSubscriptionId === null) {
        if (this.subscriptions.length > 0) {
          targetSubscription = this.filteredSubscriptions[0]
        }
      } else {
        const foundIndex = this.filteredSubscriptions.findIndex(s => s.id === this.activeSubscriptionId)
        if (foundIndex !== -1) {
          const targetIndex = foundIndex === 0 ? 0 : foundIndex - 1
          targetSubscription = this.filteredSubscriptions[targetIndex]
        } else {
          targetSubscription = this.filteredSubscriptions[0]
        }
      }

      if (targetSubscription !== null) {
        this.toItems(targetSubscription)
      }
    },
    readPins () {
      this.ellipsedPins.forEach(pin => {
        window.open(pin.url, '_blank')
        this.$store.dispatch('REMOVE_PIN', {'pin': pin})
      })
    }
  }
}
</script>

<style scoped>
.sticky-menu {
  position: -webkit-sticky;
  position:sticky;
  top: 0;
  z-index: 100;
  padding: 0;
}

.sticky-search {
  position: -webkit-sticky;
  position:sticky;
  top: 78px;
  z-index: 90;
  background-color: white;
  margin-right: 1px;
  border-bottom: 1px solid #E0E0E0;
  border-top: 1px solid #E0E0E0;
}
</style>
