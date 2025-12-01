<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">
        <h2>智慧化考试系统</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#001529"
        text-color="#fff"
        active-text-color="#1890ff"
      >
        <el-menu-item index="/home">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <!-- 管理员菜单 -->
        <template v-if="userStore.userInfo.role === 'ADMIN'">
          <el-menu-item index="/user">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/class">
            <el-icon><School /></el-icon>
            <span>班级管理</span>
          </el-menu-item>
          <el-menu-item index="/question">
            <el-icon><EditPen /></el-icon>
            <span>题库管理</span>
          </el-menu-item>
          <el-menu-item index="/paper">
            <el-icon><Document /></el-icon>
            <span>试卷管理</span>
          </el-menu-item>
          <el-menu-item index="/exam">
            <el-icon><Calendar /></el-icon>
            <span>考试管理</span>
          </el-menu-item>
          <el-menu-item index="/grade">
            <el-icon><DataAnalysis /></el-icon>
            <span>成绩管理</span>
          </el-menu-item>
        </template>
        
        <!-- 教师菜单 -->
        <template v-if="userStore.userInfo.role === 'TEACHER'">
          <el-menu-item index="/class">
            <el-icon><School /></el-icon>
            <span>班级管理</span>
          </el-menu-item>
          <el-menu-item index="/question">
            <el-icon><EditPen /></el-icon>
            <span>题库管理</span>
          </el-menu-item>
          <el-menu-item index="/paper">
            <el-icon><Document /></el-icon>
            <span>试卷管理</span>
          </el-menu-item>
          <el-menu-item index="/exam">
            <el-icon><Calendar /></el-icon>
            <span>考试管理</span>
          </el-menu-item>
          <el-menu-item index="/grade">
            <el-icon><DataAnalysis /></el-icon>
            <span>成绩管理</span>
          </el-menu-item>
        </template>
        
        <!-- 学生菜单 -->
        <template v-if="userStore.userInfo.role === 'STUDENT'">
          <el-menu-item index="/exam/hall">
            <el-icon><Calendar /></el-icon>
            <span>考试大厅</span>
          </el-menu-item>
          <el-menu-item index="/grade/student">
            <el-icon><DataAnalysis /></el-icon>
            <span>我的成绩</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-content">
          <span class="title">{{ route.meta.title || '首页' }}</span>
          <div class="user-info">
            <el-dropdown>
              <span class="user-name">
                <el-icon><Avatar /></el-icon>
                {{ userStore.userInfo.realName }}
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    router.push('/login')
  })
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-aside {
  background-color: #001529;
  color: #fff;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo h2 {
  margin: 0;
  color: #fff;
  font-size: 20px;
}

.el-header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-name {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-name:hover {
  background-color: #f5f5f5;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
