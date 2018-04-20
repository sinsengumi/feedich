import SubscriptionSorter from './SubscriptionSorter'

export default class LocalStrage {
  constructor () {
    this.sortKeyName = 'subscriptionSortKey'
  }

  getSubscriptionSortKey () {
    const sortKey = localStorage.getItem(this.sortKeyName)
    return sortKey === null ? SubscriptionSorter.sortKey.UPDATED_AT_DESC : sortKey
  }

  setSubscriptionSortKey (sortKey) {
    localStorage.setItem(this.sortKeyName, sortKey)
  }
}
