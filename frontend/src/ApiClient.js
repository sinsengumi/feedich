'use strict'

import axios from 'axios'
axios.defaults.withCredentials = true

const API_BASE_URL = process.env.API_BASE_URL

export default class ApiClient {
  getSubscriptions (onSuccess, onFailure) {
    return axios.get(`${API_BASE_URL}/api/subscriptions`)
      .then(onSuccess)
      .catch(onFailure)
  }

  getFeed (feedId) {
    return axios.get(`${API_BASE_URL}/api/feeds/${feedId}`)
  }

  getItems (feedId) {
    return axios.get(`${API_BASE_URL}/api/feeds/${feedId}/items`)
  }
}
