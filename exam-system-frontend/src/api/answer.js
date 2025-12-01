import request from '@/utils/request'

// 保存答案
export const saveAnswer = (data) => {
    return request({
        url: '/answer/save',
        method: 'post',
        data
    })
}

// 提交试卷
export const submitExam = (examId) => {
    return request({
        url: `/answer/submit/${examId}`,
        method: 'post'
    })
}

// 获取答题记录
export const getAnswersByExam = (examId, studentId) => {
    return request({
        url: `/answer/exam/${examId}`,
        method: 'get',
        params: { studentId }
    })
}
