import Vue from 'vue'
import Vuex from 'vuex'
import * as mutation from './mutation-types'
import * as action from './action-types'
import ApiClient from '../ApiClient'

Vue.use(Vuex)

const api = new ApiClient()

export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production',
  state: {
    subscriptions: [],
    pins: []
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
    }
  },
  actions: {
    [action.GET_SUBSCRIPTIONS] ({ commit }, payload) {
      return api.getSubscriptions()
        .then((response) => {
          commit(mutation.SET_SUBSCRIPTIONS, {subscriptions: response.data})
          return response.data
        })
    },
    [action.SUBSCRIBE] ({ commit }, payload) {
      return api.subscribe(payload.feedUrl)
        .then((response) => {
          commit(mutation.SUBSCRIBE, {addedSubscription: response.data})
          Vue.toasted.global.info({message: 'Subscribe "' + response.data.feed.title + '"'})
          return response.data
        })
        .catch((error) => {
          console.log(error)
        })
    },
    [action.UNSUBSCRIBE] ({ commit }, payload) {
      return api.unsubscribe(payload.subscription.id)
        .then((response) => {
          commit(mutation.UNSUBSCRIBE, {removedSubscription: payload.subscription})
          Vue.toasted.global.info({message: 'Unsubscribe "' + payload.subscription.feed.title + '"'})
          return payload.subscription
        })
        .catch((error) => {
          console.log(error)
        })
    },

    [action.GET_PINS] ({ commit }, payload) {
      return api.getPins()
        .then((response) => {
          commit(mutation.SET_PINS, {pins: response.data})
          return response.data
        })
        .catch((error) => {
          console.log(error)
        })
    },
    [action.ADD_PIN] ({ commit }, payload) {
      return api.addPin(payload.title, payload.url)
        .then((response) => {
          commit(mutation.ADD_PIN, {addedPin: response.data})
          return response.data
        })
        .catch((error) => {
          console.log(error)
        })
    },
    [action.REMOVE_PIN] ({ commit }, payload) {
      return api.removePin(payload.pin.id)
        .then((response) => {
          commit(mutation.REMOVE_PIN, {removedPin: payload.pin})
          return payload.pin
        })
        .catch((error) => {
          console.log(error)
        })
    }
  }
})
