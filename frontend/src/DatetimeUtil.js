'use strict'

import moment from 'moment'

export default class DatetimeUtil {
  fromNow (datetime) {
    return moment(datetime).fromNow()
  }
}
