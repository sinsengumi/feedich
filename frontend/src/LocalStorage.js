import SubscriptionSorter from './SubscriptionSorter'

export default class LocalStrage {
  constructor () {
    this.sortKeyName = 'subscriptionSortKey'
    this.useItemCompactView = 'useItemCompactView'
    this.pinOpenCount = 'pinOpenCount'
  }

  getSubscriptionSortKey () {
    const sortKey = localStorage.getItem(this.sortKeyName)
    return sortKey === null ? SubscriptionSorter.sortKey.UPDATED_AT_DESC : sortKey
  }

  setSubscriptionSortKey (sortKey) {
    localStorage.setItem(this.sortKeyName, sortKey)
  }

  getUseItemCompactView () {
    const useItemCompactView = localStorage.getItem(this.useItemCompactView)
    return useItemCompactView !== null && useItemCompactView.toLowerCase() === 'true'
  }

  setUseItemCompactView (useItemCompactView) {
    localStorage.setItem(this.useItemCompactView, useItemCompactView)
  }

  getPinOpenCount () {
    const pinOpenCount = localStorage.getItem(this.pinOpenCount)
    return pinOpenCount === null ? 10 : pinOpenCount
  }

  setPinOpenCount (pinOpenCount) {
    localStorage.setItem(this.pinOpenCount, pinOpenCount)
  }

  clear () {
    localStorage.clear()
  }
}
