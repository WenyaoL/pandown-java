import request from './request'

export function getUserFlowInfo(data?: any) {
    return request({
        url: 'api/user-flow/getUserFlowInfo',
        method: 'post',
        data
    })
}

export default {
    getUserFlowInfo
}