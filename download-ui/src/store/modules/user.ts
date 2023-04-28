import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state: any) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state: { token: any }, token: any) => {
    state.token = token
  },
  SET_NAME: (state: { name: any }, name: any) => {
    state.name = name
  },
  SET_AVATAR: (state: { avatar: any }, avatar: any) => {
    state.avatar = avatar
  }
}

const actions = {
  // user login
  login({ commit }: any, userInfo: { username: any; password: any }) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then((response: { data: any }) => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(null)
      }).catch((error: any) => {
        console.log(error);
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }:any) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response
        if (!data) {
          return reject('Verification failed, please Login again.')
        }
        const { name, avatar } = data
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }:any) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        console.log("asdfasdf");
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve(null)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }:any) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve(null)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

