import request from './request'

export function getList(params:any) {
  return request({
    url: '/vue-admin-template/table/list',
    method: 'get',
    params
  })
}
