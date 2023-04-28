import request from './request'

export function getShareDir(data:any){
  return request({
    url: '/api/list_dir',
    method: 'post',
    data
  })
}
