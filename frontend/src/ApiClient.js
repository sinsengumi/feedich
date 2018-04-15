'use strict'

import axios from 'axios'
axios.defaults.withCredentials = true

const API_BASE_URL = process.env.API_BASE_URL

export default class ApiClient {
  discoverFeeds (url) {
    return axios.get(`${API_BASE_URL}/api/feeds/discover?url=${url}`)
  }

  subscribeFeed (feedUrl) {
    const params = new URLSearchParams()
    params.append('feedUrl', feedUrl)
    return axios.post(`${API_BASE_URL}/api/feeds/subscribe`, params)
  }

  getSubscriptions () {
    return axios.get(`${API_BASE_URL}/api/subscriptions`)
  }

  getFeed (feedId) {
    return axios.get(`${API_BASE_URL}/api/feeds/${feedId}`)
  }

  getItems (feedId) {
    return axios.get(`${API_BASE_URL}/api/feeds/${feedId}/items`)
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
}
