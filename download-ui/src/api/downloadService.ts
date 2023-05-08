import request from './request'

export function getShareDir(data:any){
  return request({
    url: '/api/list_dir',
    method: 'post',
    data
  })
}

export function getSignAndTime(data:any){
  return request({
    url:'api/getSignAndTime',
    method: 'post',
    data
  })
}


export function getSvipDlink(data:any){
  return request({
    url:'api/getSvipDlink',
    method: 'post',
    data
  })
}

export default {
  getShareDir,
  getSignAndTime,
  getSvipDlink
}