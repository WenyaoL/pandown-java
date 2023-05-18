import { createStore } from 'vuex'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'
import system from './modules/system'
import permission from './modules/permission'

const store = createStore<any>({
  state: {
    surl: '1',
    pwd: '',
    pandownData: {}
  },
  mutations: {
    setSurl(state, value) {
      state.surl = value
    },
    setPwd(state, value) {
      state.pwd = value
    },
    setPandownData(state, value) {
      state.pandownData = value
    }
  },
  actions: {},
  modules: {
    app,
    settings,
    user,
    system,
    permission
  },
  getters: {
    sidebar: state => state.app.sidebar,
    device: state => state.app.device,
    token: state => state.user.token,
    avatar: state => state.user.avatar,
    name: state => state.user.name,
    roles: state=> state.user.roles,
    permission_routes: state => state.permission.routes,
  }
})

export default store