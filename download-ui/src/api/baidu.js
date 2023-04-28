import request from './request'

const baiduapi = {
  get(url, params, baseURL) { // 修改 GET 请求方法
    return request({
      url,
      method: 'get',
      params, // 将参数放在 URL 的查询字符串中
      baseURL // 传递 baseURL 参数
    })
  },

  post(url, data, baseURL) { // 修改 POST 请求方法
    return request({
      url,
      method: 'post',
      data, // 将 JSON 对象作为请求体参数
      baseURL, // 传递 baseURL 参数
      headers: {
        'Content-Type': 'application/json' // 修改请求头中的 Content-Type
      }
    })
  }
}

export default baiduapi
