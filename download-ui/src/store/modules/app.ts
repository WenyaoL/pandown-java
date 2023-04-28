import Cookies from 'js-cookie'


const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? Cookies.get('sidebarStatus')=='1' : true,
    withoutAnimation: false
  },
  device: 'desktop'
}

const mutations = {
  TOGGLE_SIDEBAR: (state:any) => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', '1')
    } else {
      Cookies.set('sidebarStatus', '0')
    }
  },
  CLOSE_SIDEBAR: (state: { sidebar: { opened: boolean; withoutAnimation: any } }, withoutAnimation: any) => {
    Cookies.set('sidebarStatus', '0')
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state: { device: any }, device: any) => {
    state.device = device
  }
}

const actions = {
  toggleSideBar({ commit }:any) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }:any, { withoutAnimation }:any) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }: any, device: any) {
    commit('TOGGLE_DEVICE', device)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
