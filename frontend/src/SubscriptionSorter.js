import moment from 'moment'

const SORT_KEY = {
  UPDATED_AT_ASC: 'UPDATED_AT_ASC',
  UPDATED_AT_DESC: 'UPDATED_AT_DESC',
  CREATED_AT_ASC: 'CREATED_AT_ASC',
  CREATED_AT_DESC: 'CREATED_AT_DESC',
  UNREAD_COUNT_ASC: 'UNREAD_COUNT_ASC',
  UNREAD_COUNT_DESC: 'UNREAD_COUNT_DESC',
  TITLE_ASC: 'TITLE_ASC',
  TITLE_DESC: 'TITLE_DESC',
  STATUS_ASC: 'STATUS_ASC',
  STATUS_DESC: 'STATUS_DESC'
}

export default class SubscriptionSorter {
  sort (subscriptions, sortKey) {
    if (sortKey === null) {
      return subscriptions
    }

    let result = null

    switch (sortKey) {
      case SORT_KEY.UPDATED_AT_ASC:
      case SORT_KEY.UPDATED_AT_DESC:
        result = this.sortUpdatedAt(subscriptions)
        return sortKey === SORT_KEY.UPDATED_AT_DESC ? result.reverse() : result
      case SORT_KEY.CREATED_AT_ASC:
      case SORT_KEY.CREATED_AT_DESC:
        result = this.sortCreatedAt(subscriptions)
        return sortKey === SORT_KEY.CREATED_AT_DESC ? result.reverse() : result
      case SORT_KEY.UNREAD_COUNT_ASC:
      case SORT_KEY.UNREAD_COUNT_DESC:
        result = this.sortUnreadCount(subscriptions)
        return sortKey === SORT_KEY.UNREAD_COUNT_DESC ? result.reverse() : result
      case SORT_KEY.TITLE_ASC:
      case SORT_KEY.TITLE_DESC:
        result = this.sortTitle(subscriptions)
        return sortKey === SORT_KEY.TITLE_DESC ? result.reverse() : result
      case SORT_KEY.STATUS_ASC:
      case SORT_KEY.STATUS_DESC:
        result = this.sortStatus(subscriptions)
        return sortKey === SORT_KEY.STATUS_DESC ? result.reverse() : result
      default:
        return this.sortUpdatedAt(subscriptions).reverse()
    }
  }

  sortUpdatedAt (subscriptions) {
    return subscriptions.sort((a, b) => {
      const aUnix = moment(a.feed.publishedAt).unix()
      const bUnix = moment(b.feed.publishedAt).unix()

      const result = aUnix - bUnix
      if (result === 0) {
        return a.feed.id > b.feed.id
      }
      return result
    })
  }

  sortCreatedAt (subscriptions) {
    return subscriptions.sort((a, b) => {
      const aUnix = moment(a.feed.createdAt).unix()
      const bUnix = moment(b.feed.createdAt).unix()

      const result = aUnix - bUnix
      if (result === 0) {
        return a.feed.id > b.feed.id
      }
      return result
    })
  }

  sortUnreadCount (subscriptions) {
    return subscriptions.sort((a, b) => {
      const result = a.unreadCountOriginal - b.unreadCountOriginal
      if (result === 0) {
        return a.feed.id > b.feed.id
      }
      return result
    })
  }

  sortTitle (subscriptions) {
    return subscriptions.sort((a, b) => {
      const aStr = a.feed.title.toLowerCase()
      const bStr = b.feed.title.toLowerCase()
      if (aStr < bStr) {
        return -1
      } else if (aStr > bStr) {
        return 1
      }
      return a.feed.id > b.feed.id
    })
  }

  sortStatus (subscriptions) {
    return subscriptions.sort((a, b) => {
      const aStr = a.feed.status
      const bStr = b.feed.status
      if (aStr < bStr) {
        return -1
      } else if (aStr > bStr) {
        return 1
      }
      return a.feed.id > b.feed.id
    })
  }
}

SubscriptionSorter.sortKey = SORT_KEY
