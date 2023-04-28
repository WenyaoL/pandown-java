import request from './request'

export function login(data:any) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token:any) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
