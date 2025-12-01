import request from '@/utils/request'

// 题目列表
export const getQuestionList = (params) => {
    return request({
        url: '/question/list',
        method: 'get',
        params
    })
}

// 题目详情
export const getQuestionById = (id) => {
    return request({
        url: `/question/${id}`,
        method: 'get'
    })
}

// 创建题目
export const createQuestion = (data) => {
    return request({
        url: '/question',
        method: 'post',
        data
    })
}

// 更新题目
export const updateQuestion = (id, data) => {
    return request({
        url: `/question/${id}`,
        method: 'put',
        data
    })
}

// 删除题目
export const deleteQuestion = (id) => {
    return request({
        url: `/question/${id}`,
        method: 'delete'
    })
}

// 导入题目
export const importQuestions = (data) => {
    return request({
        url: '/question/import',
        method: 'post',
        data,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}
