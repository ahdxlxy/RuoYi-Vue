import request from '@/utils/request'

// 试卷列表
export const getPaperList = (params) => {
    return request({
        url: '/paper/list',
        method: 'get',
        params
    })
}

// 试卷详情
export const getPaperById = (id) => {
    return request({
        url: `/paper/${id}`,
        method: 'get'
    })
}

// 创建试卷
export const createPaper = (data) => {
    return request({
        url: '/paper',
        method: 'post',
        data
    })
}

// 更新试卷
export const updatePaper = (id, data) => {
    return request({
        url: `/paper/${id}`,
        method: 'put',
        data
    })
}

// 删除试卷
export const deletePaper = (id) => {
    return request({
        url: `/paper/${id}`,
        method: 'delete'
    })
}
