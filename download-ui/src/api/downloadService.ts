import request from './request'

export function getShareDir(data:any){
  return request({
    url: 'api/download/listDir',
    method: 'post',
    data
  })
}

export function getSignAndTime(data:any){
  return request({
    url:'api/download/getSignAndTime',
    method: 'post',
    data
  })
}


export function getSvipDlink(data:any){
  return request({
    url:'api/download/getSvipDlink',
    method: 'post',
    data
  })
}

export function getAllSvipDlink(data:any){
  return request({
    url:'api/download/getAllSvipDlink',
    method: 'post',
    data
  })
}

export default {
  getShareDir,
  getSignAndTime,
  getSvipDlink,
  getAllSvipDlink
}