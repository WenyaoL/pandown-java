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

export function register(data:any) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function postCaptcha(data:any) {
  return request({
    url: '/user/postCaptcha',
    method: 'post',
    data
  })
}

export function postCaptchaByForgetPwd(data:any) {
  return request({
    url: '/user/postCaptchaByForgetPwd',
    method: 'post',
    data
  })
}

export function resetPassword(data:any){
  return request({
    url: '/user/resetPassword',
    method: 'post',
    data
  })
}
export default {
  login,
  getInfo,
  logout,
  register,
  postCaptcha,
  postCaptchaByForgetPwd,
  resetPassword
}