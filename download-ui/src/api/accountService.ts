import request from './request'

export function getSvipAccountNum(data?:any){
  return request({
    url: 'api/svip/getAccountNum',
    method: 'post',
    data
  })
}

export function getSvipAccountDetail(){
  return request({
    url: 'api/svip/getAccountDetail',
    method: 'post',
  })
}

export function addSvipAccount(data:any){
  return request({
    url: 'api/svip/addAccount',
    method: 'post',
    data
  })
}

export function deleteSvipAccount(data:any){
  return request({
    url: 'api/svip/deleteAccount',
    method: 'post',
    data
  })
}

export function updateSvipAccount(data:any){
  return request({
    url: 'api/svip/updateAccount',
    method: 'post',
    data
  })
}

export function getCommonAccountNum(data?:any){
  return request({
    url: 'api/common-account/getAccountNum',
    method: 'post',
    data
  })
}

export function getCommonAccountDetail(){
  return request({
    url: 'api/common-account/getAccountDetail',
    method: 'post'
  })
}

export function addCommonAccount(data:any){
  return request({
    url: 'api/common-account/addAccount',
    method: 'post',
    data
  })
}

export function updateCommonAccount(data:any){
  return request({
    url: 'api/common-account/updateAccount',
    method: 'post',
    data
  })
}

export function deleteCommonAccount(data:any){
  return request({
    url: 'api/common-account/deleteAccount',
    method: 'post',
    data
  })
}



export default{
  getSvipAccountNum,
  getSvipAccountDetail,
  addSvipAccount,
  deleteSvipAccount,
  updateSvipAccount,
  getCommonAccountNum,
  getCommonAccountDetail,
  addCommonAccount,
  updateCommonAccount,
  deleteCommonAccount,

}