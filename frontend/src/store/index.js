import Vue from 'vue'
import Vuex from 'vuex'
import * as mutation from './mutation-types'
import * as action from './action-types'
import ApiClient from '../ApiClient'

Vue.use(Vuex)

const api = new ApiClient()

var timer = null

function ellipsedTitle (title) {
  return title.length > 40 ? title.substring(0, 40) + '...' : title
}

function undoNotifyMessage (store) {
  clearTimeout(timer)
  timer = setTimeout(() => {
    store.commit(mutation.SET_NOTIFY_MESSAGE, {message: '....'})
  }, 3000)
}

export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production',
  state: {
    subscriptions: [],
    pins: [],
    notifyMessage: '....'
  },
  getters: {
    getSubscriptionById: (state) => (id) => {
      return state.subscriptions.find(s => s.id === id)
    }
  },
  mutations: {
    [mutation.SET_SUBSCRIPTIONS] (state, payload) {
      state.subscriptions = payload.subscriptions
    },
    [mutation.SUBSCRIBE] (state, payload) {
      state.subscriptions.unshift(payload.addedSubscription)
    },
    [mutation.UNSUBSCRIBE] (state, payload) {
      const index = state.subscriptions.findIndex(s => s.id === payload.removedSubscription.id)
      if (index !== -1) {
        state.subscriptions.splice(index, 1)
      }
    },
    [mutation.READ_ITEM] (state, payload) {
      const subscription = state.subscriptions.find(s => s.id === payload.subscriptionId)
      subscription.unreadCount = payload.unreadItemCount
    },
    [mutation.UNREAD_ITEM] (state, payload) {
      const subscription = state.subscriptions.find(s => s.id === payload.subscriptionId)
      subscription.unreadCount = payload.unreadItemCount
    },

    [mutation.SET_PINS] (state, payload) {
      state.pins = payload.pins
    },
    [mutation.ADD_PIN] (state, payload) {
      state.pins.unshift(payload.addedPin)
    },
    [mutation.REMOVE_PIN] (state, payload) {
      const index = state.pins.findIndex(p => p.id === payload.removedPin.id)
      if (index !== -1) {
        state.pins.splice(index, 1)
      }
    },
    [mutation.CLEAR_PINS] (state, payload) {
      state.pins = []
    },
    [mutation.SET_NOTIFY_MESSAGE] (state, payload) {
      state.notifyMessage = payload.message
    }
  },
  actions: {
    [action.GET_SUBSCRIPTIONS] ({ commit }, payload) {
      commit(mutation.SET_SUBSCRIPTIONS, {subscriptions: []})
      return api.getSubscriptions()
        .then((response) => {
          commit(mutation.SET_SUBSCRIPTIONS, {subscriptions: response.data})
          return response.data
        })
        .catch((error) => {
          return Promise.reject(error)
        })
    },
    [action.SUBSCRIBE] ({ commit }, payload) {
      return api.subscribe(payload.feedUrl)
        .then((response) => {
          commit(mutation.SUBSCRIBE, {addedSubscription: response.data})

          commit(mutation.SET_NOTIFY_MESSAGE, {message: '<strong>「' + ellipsedTitle(response.data.feed.title) + '」</strong>' + 'を購読しました'})
          undoNotifyMessage(this)

          return response.data
        })
        .catch((error) => {
          return Promise.reject(error)
        })
    },
    [action.UNSUBSCRIBE] ({ commit }, payload) {
      return api.unsubscribe(payload.subscription.id)
        .then((response) => {
          commit(mutation.UNSUBSCRIBE, {removedSubscription: payload.subscription})

          commit(mutation.SET_NOTIFY_MESSAGE, {message: '<strong>「' + ellipsedTitle(payload.subscription.feed.title) + '」</strong>' + 'を購読停止しました'})
          undoNotifyMessage(this)

          return payload.subscription
        })
        .catch((error) => {
          return Promise.reject(error)
        })
    },

    [action.GET_PINS] ({ commit }, payload) {
      return api.getPins()
        .then((response) => {
          commit(mutation.SET_PINS, {pins: response.data})
          return response.data
        })
        .catch((error) => {
          return Promise.reject(error)
        })
    },
    [action.ADD_PIN] ({ commit }, payload) {
      return api.addPin(payload.title, payload.url)
        .then((response) => {
          commit(mutation.ADD_PIN, {addedPin: response.data})

          commit(mutation.SET_NOTIFY_MESSAGE, {message: '<strong>「' + ellipsedTitle(payload.title) + '」</strong>' + 'にピンを付けました'})
          undoNotifyMessage(this)

          return response.data
        })
        .catch((error) => {
          return Promise.reject(error)
        })
    },
    [action.REMOVE_PIN] ({ commit }, payload) {
      return api.removePin(payload.pin.id)
        .then((response) => {
          commit(mutation.REMOVE_PIN, {removedPin: payload.pin})

          commit(mutation.SET_NOTIFY_MESSAGE, {message: '<strong>「' + ellipsedTitle(payload.pin.title) + '」</strong>' + 'のピンを外しました'})
          undoNotifyMessage(this)

          return payload.pin
        })
        .catch((error) => {
          return Promise.reject(error)
        })
    },
    [action.CLEAR_PINS] ({ commit }, payload) {
      return api.clearPins()
        .then((response) => {
          commit(mutation.CLEAR_PINS)

          commit(mutation.SET_NOTIFY_MESSAGE, {message: 'ピンを全削除しました'})
          undoNotifyMessage(this)
        })
        .catch((error) => {
          return Promise.reject(error)
        })
    },
    [action.SET_NOTIFY_MESSAGE] ({ commit }, payload) {
      commit(mutation.SET_NOTIFY_MESSAGE, {message: payload.message})

      if (payload.dontUndo === undefined) {
        undoNotifyMessage(this)
      }
    }
  }
})
