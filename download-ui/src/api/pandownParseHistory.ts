import request from './request'

export function getUserHistory(data?:any) {
    return request({
        url: 'api/pandownParse/userHistory',
        method: 'post',
        data
    })
}

export function deleteUserParseHistory(data?:any){
    return request({
        url: 'api/pandownParse/deleteUserParseHistory',
        method: 'post',
        data
    })
}

export default {
    getUserHistory,
    deleteUserParseHistory
}