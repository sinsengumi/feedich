import Vue from 'vue'
import Toasted from 'vue-toasted'

Vue.use(Toasted)

Vue.toasted.register('info',
  (payload) => payload.message,
  {
    duration: 5000,
    type: 'success',
    iconPack: 'fontawesome',
    icon: 'info-circle',
    className: ['pt-3', 'pb-3']
  }
)

Vue.toasted.register('error',
  (payload) => {
    if (!payload.message) {
      return 'Oops.. Something Went Wrong...'
    }
    return payload.message
  },
  {
    duration: 5000,
    type: 'error',
    iconPack: 'fontawesome',
    icon: 'exclamation-triangle',
    className: ['pt-3', 'pb-3']
  }
)

export default Toasted
