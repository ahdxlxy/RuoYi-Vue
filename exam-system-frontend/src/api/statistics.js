import request from '@/utils/request'

export function getDashboardStats() {
    return request({
        url: '/statistics/dashboard',
        method: 'get'
    })
}
