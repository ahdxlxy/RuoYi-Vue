import request from '@/utils/request'

// 考试列表
export const getExamList = (params) => {
    return request({
        url: '/exam/list',
        method: 'get',
        params
    })
}

// 可参加的考试
export const getAvailableExams = () => {
    return request({
        url: '/exam/available',
        method: 'get'
    })
}

// 考试详情
export const getExamById = (id) => {
    return request({
        url: `/exam/${id}`,
        method: 'get'
    })
}

// 创建考试
export const createExam = (data) => {
    return request({
        url: '/exam',
        method: 'post',
        data
    })
}

// 更新考试
export const updateExam = (id, data) => {
    return request({
        url: `/exam/${id}`,
        method: 'put',
        data
    })
}

// 删除考试
export const deleteExam = (id) => {
    return request({
        url: `/exam/${id}`,
        method: 'delete'
    })
}

// 开始考试
export const startExam = (id, password) => {
    return request({
        url: `/exam/${id}/start`,
        method: 'post',
        params: { password }
    })
}
