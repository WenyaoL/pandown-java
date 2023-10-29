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
    method: 'post',
    data: { token }
  })
}

export function logout(data:any) {
  return request({
    url: '/user/logout',
    method: 'post',
    data
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

export function getUserDetail(data:any){
  return request({
    url: '/api/admin/getUserInfoByPage',
    method: 'post',
    data
  })
}

export function getUserNum(data?:any){
  return request({
    url: '/api/admin/getUserNum',
    method: 'post',
    data
  })
}

export function updateUserDetail(data:any){
  return request({
    url: '/api/admin/updateUserDetail',
    method: 'post',
    data
  })
}

export function addUserDetail(data:any){
  return request({
    url: '/api/admin/addUserDetail',
    method: 'post',
    data
  })
}

export function deleteUserDetail(data:any){
  return request({
    url: '/api/admin/deleteUserDetail',
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
  resetPassword,
  getUserDetail,
  getUserNum,
  updateUserDetail,
  deleteUserDetail
}