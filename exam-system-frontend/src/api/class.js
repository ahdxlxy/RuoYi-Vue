import request from '@/utils/request'

// 班级列表
export const getClassList = (params) => {
    return request({
        url: '/class/list',
        method: 'get',
        params
    })
}

// 获取所有班级
export const getAllClasses = () => {
    return request({
        url: '/class/all',
        method: 'get'
    })
}

// 创建班级
export const createClass = (data) => {
    return request({
        url: '/class',
        method: 'post',
        data
    })
}

// 更新班级
export const updateClass = (id, data) => {
    return request({
        url: `/class/${id}`,
        method: 'put',
        data
    })
}

// 删除班级
export const deleteClass = (id) => {
    return request({
        url: `/class/${id}`,
        method: 'delete'
    })
}

// 添加学生到班级
export const addStudentsToClass = (classId, studentIds) => {
    return request({
        url: `/class/${classId}/students`,
        method: 'post',
        data: studentIds
    })
}

// 从班级移除学生
export const removeStudentsFromClass = (classId, studentIds) => {
    return request({
        url: `/class/${classId}/students`,
        method: 'delete',
        data: studentIds
    })
}

// 获取我的班级
export const getMyClass = () => {
    return request({
        url: '/class/my',
        method: 'get'
    })
}
