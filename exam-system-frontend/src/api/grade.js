import request from '@/utils/request'

// 成绩列表
export const getGradeList = (params) => {
    return request({
        url: '/grade/list',
        method: 'get',
        params
    })
}

// 学生成绩
export const getStudentGrades = () => {
    return request({
        url: '/grade/student',
        method: 'get'
    })
}

// 成绩详情
export const getGradeDetail = (examId, studentId) => {
    return request({
        url: '/grade/detail',
        method: 'get',
        params: { examId, studentId }
    })
}

// 人工阅卷
export const manualGrade = (params) => {
    return request({
        url: '/grade/mark',
        method: 'post',
        params
    })
}
