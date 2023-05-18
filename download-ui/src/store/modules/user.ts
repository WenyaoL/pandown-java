import UserService from '@/api/userService'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    roles:[]
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
  },
  SET_ROLES: (state: { roles: any }, roles: []) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }: any, userInfo: any) {
    const { username, password,email } = userInfo
    return new Promise((resolve, reject) => {
      UserService.login({ email: email.trim(), password: password }).then((response: { data: any }) => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data.token)
      }).catch((error: any) => {
        console.log(error);
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }: any) {
    return new Promise((resolve, reject) => {    
      UserService.getInfo(state.token).then(response => {
        const { data } = response
        if (!data) {
          return reject('Verification failed, please Login again.')
        }
        const { name, avatar,role_name } = data
        commit('SET_ROLES',[role_name])
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }: any,token: any) {
    return new Promise((resolve, reject) => {
      UserService.logout(token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve(null)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user register
  register({ commit, state }: any,data:any) {
    return new Promise((resolve, reject) => {
      UserService.register(data).then(() => {
        resolve(null)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user postCaptcha
  postCaptcha({ commit }: any, data: {email: string}) {
    return new Promise((resolve, reject) => {
      UserService.postCaptcha(data).then(() => {
        resolve(null)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user forgetPwd postCaptcha
  postCaptchaByForgetPwd({ commit }: any, data: {email: string}) {
    return new Promise((resolve, reject) => {
      UserService.postCaptchaByForgetPwd(data).then(() => {
        resolve(null)
      }).catch(error => {
        reject(error)
      })
    })
  },

  resetPassword({ commit }: any,data:any){
    return UserService.resetPassword(data)
  },

  // remove token
  resetToken({ commit }: any) {
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

