import request from './request'

export function getSvipAccountNum(data?:any){
  return request({
    url: 'api/dbtableSvip/getAccountNum',
    method: 'post',
    data
  })
}

export function getSvipAccountDetail(){
  return request({
    url: 'api/dbtableSvip/getAccountDetail',
    method: 'post',
  })
}

export function addSvipAccount(data:any){
  return request({
    url: 'api/dbtableSvip/addAccount',
    method: 'post',
    data
  })
}

export function deleteSvipAccount(data:any){
  return request({
    url: 'api/dbtableSvip/deleteAccount',
    method: 'post',
    data
  })
}

export function updateSvipAccount(data:any){
  return request({
    url: 'api/dbtableSvip/updateAccount',
    method: 'post',
    data
  })
}

export function getCommonAccountDetail(){
  return request({
    url: 'api/dbtable-common-account/getAccountDetail',
    method: 'post'
  })
}

export function addCommonAccount(data:any){
  return request({
    url: 'api/dbtable-common-account/addAccount',
    method: 'post',
    data
  })
}

export function updateCommonAccount(data:any){
  return request({
    url: 'api/dbtable-common-account/updateAccount',
    method: 'post',
    data
  })
}

export function deleteCommonAccount(data:any){
  return request({
    url: 'api/dbtable-common-account/deleteAccount',
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
  getCommonAccountDetail,
  addCommonAccount,
  updateCommonAccount,
  deleteCommonAccount
}