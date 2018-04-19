import Vue from 'vue'
import axios from 'axios'

const API_BASE_URL = process.env.API_BASE_URL

axios.defaults.withCredentials = true
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      Vue.toasted.global.error({message: error.response.data.message})
    } else if (error.request) {
      Vue.toasted.global.error({message: 'Network Error'})
    } else {
      Vue.toasted.global.error()
    }
    return Promise.reject(error)
  }
)

export default class ApiClient {
  discoverFeeds (url) {
    return axios.get(`${API_BASE_URL}/api/feeds/discover?url=${url}`)
  }

  subscribe (feedUrl) {
    const params = new URLSearchParams()
    params.append('feedUrl', feedUrl)
    return axios.put(`${API_BASE_URL}/api/subscriptions`, params)
  }

  unsubscribe (subscriptionId) {
    const params = new URLSearchParams()
    params.append('subscriptionId', subscriptionId)
    return axios.delete(`${API_BASE_URL}/api/subscriptions/${subscriptionId}`, params)
  }

  getSubscriptions () {
    return axios.get(`${API_BASE_URL}/api/subscriptions`)
  }

  getSubscription (subscriptionId) {
    return axios.get(`${API_BASE_URL}/api/subscriptions/${subscriptionId}`)
  }

  getItems (subscriptionId) {
    return axios.get(`${API_BASE_URL}/api/subscriptions/${subscriptionId}/items`)
  }

  readItem (itemId) {
    const params = new URLSearchParams()
    params.append('itemId', itemId)
    return axios.post(`${API_BASE_URL}/api/items/read`, params)
  }

  unreadItem (itemId) {
    const params = new URLSearchParams()
    params.append('itemId', itemId)
    return axios.post(`${API_BASE_URL}/api/items/unread`, params)
  }

  getPins () {
    return axios.get(`${API_BASE_URL}/api/pins`)
  }

  addPin (title, url) {
    const params = new URLSearchParams()
    params.append('title', title)
    params.append('url', url)
    return axios.put(`${API_BASE_URL}/api/pins`, params)
  }

  removePin (pinId) {
    const params = new URLSearchParams()
    params.append('pinId', pinId)
    return axios.delete(`${API_BASE_URL}/api/pins/${pinId}`, params)
  }
}
