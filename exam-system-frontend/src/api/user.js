import request from '@/utils/request'

// 登录
export const login = (data) => {
    return request({
        url: '/auth/login',
        method: 'post',
        data
    })
}

// 获取用户信息
export const getUserInfo = () => {
    return request({
        url: '/user/info',
        method: 'get'
    })
}

// 用户列表
export const getUserList = (params) => {
    return request({
        url: '/user/list',
        method: 'get',
        params
    })
}

// 创建用户
export const createUser = (data) => {
    return request({
        url: '/user',
        method: 'post',
        data
    })
}

// 更新用户
export const updateUser = (id, data) => {
    return request({
        url: `/user/${id}`,
        method: 'put',
        data
    })
}

// 删除用户
export const deleteUser = (id) => {
    return request({
        url: `/user/${id}`,
        method: 'delete'
    })
}
