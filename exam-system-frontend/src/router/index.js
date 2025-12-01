import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/index.vue'),
        meta: { title: '登录' }
    },
    {
        path: '/',
        redirect: '/home',
        component: () => import('@/views/layout/index.vue'),
        children: [
            {
                path: 'home',
                name: 'Home',
                component: () => import('@/views/home/index.vue'),
                meta: { title: '首页' }
            },
            {
                path: 'user',
                name: 'User',
                component: () => import('@/views/user/index.vue'),
                meta: { title: '用户管理', roles: ['ADMIN'] }
            },
            {
                path: 'class',
                name: 'Class',
                component: () => import('@/views/class/index.vue'),
                meta: { title: '班级管理', roles: ['ADMIN', 'TEACHER'] }
            },
            {
                path: 'question',
                name: 'Question',
                component: () => import('@/views/question/index.vue'),
                meta: { title: '题库管理', roles: ['ADMIN', 'TEACHER'] }
            },
            {
                path: 'paper',
                name: 'Paper',
                component: () => import('@/views/paper/index.vue'),
                meta: { title: '试卷管理', roles: ['ADMIN', 'TEACHER'] }
            },
            {
                path: 'paper/create',
                name: 'PaperCreate',
                component: () => import('@/views/paper/create.vue'),
                meta: { title: '创建试卷', roles: ['ADMIN', 'TEACHER'] }
            },
            {
                path: 'exam',
                name: 'Exam',
                component: () => import('@/views/exam/index.vue'),
                meta: { title: '考试管理', roles: ['ADMIN', 'TEACHER'] }
            },
            {
                path: 'exam/hall',
                name: 'ExamHall',
                component: () => import('@/views/exam/hall.vue'),
                meta: { title: '考试大厅', roles: ['STUDENT'] }
            },
            {
                path: 'exam/take/:id',
                name: 'ExamTake',
                component: () => import('@/views/exam/take.vue'),
                meta: { title: '在线考试', roles: ['STUDENT'] }
            },
            {
                path: 'grade',
                name: 'Grade',
                component: () => import('@/views/grade/index.vue'),
                meta: { title: '成绩管理', roles: ['ADMIN', 'TEACHER'] }
            },
            {
                path: 'grade/marking',
                name: 'GradeMarking',
                component: () => import('@/views/grade/marking.vue'),
                meta: { title: '阅卷', roles: ['ADMIN', 'TEACHER'] }
            },
            {
                path: 'grade/student',
                name: 'GradeStudent',
                component: () => import('@/views/grade/student.vue'),
                meta: { title: '我的成绩', roles: ['STUDENT'] }
            },
            {
                path: 'student/class',
                name: 'StudentClass',
                component: () => import('@/views/student/class.vue'),
                meta: { title: '我的班级', roles: ['STUDENT'] }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const userStore = useUserStore()

    if (to.path === '/login') {
        next()
    } else {
        if (!userStore.token) {
            next('/login')
        } else {
            // 检查角色权限
            if (to.meta.roles && !to.meta.roles.includes(userStore.userInfo.role)) {
                ElMessage.error('权限不足')
                next(from.path)
            } else {
                next()
            }
        }
    }
})

export default router
