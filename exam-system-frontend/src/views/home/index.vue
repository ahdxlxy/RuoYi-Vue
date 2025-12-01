<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#409eff"><User /></el-icon>
            <div class="stat-text">
              <div class="stat-title">用户总数</div>
              <div class="stat-value">{{ stats.userCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#67c23a"><Document /></el-icon>
            <div class="stat-text">
              <div class="stat-title">试卷总数</div>
              <div class="stat-value">{{ stats.paperCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#e6a23c"><Calendar /></el-icon>
            <div class="stat-text">
              <div class="stat-title">考试总数</div>
              <div class="stat-value">{{ stats.examCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#f56c6c"><EditPen /></el-icon>
            <div class="stat-text">
              <div class="stat-title">题目总数</div>
              <div class="stat-value">{{ stats.questionCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card class="welcome-card" style="margin-top: 20px">
      <h2>欢迎使用智慧化考试系统</h2>
      <p>当前用户：{{ userStore.userInfo.realName }} ({{ getRoleName(userStore.userInfo.role) }})</p>
      <div class="quick-links">
        <el-button v-if="userStore.userInfo.role === 'STUDENT'" type="primary" @click="$router.push('/exam/hall')">
          进入考试大厅
        </el-button>
        <el-button v-if="userStore.userInfo.role === 'TEACHER'" type="primary" @click="$router.push('/exam')">
          考试管理
        </el-button>
        <el-button v-if="userStore.userInfo.role === 'ADMIN'" type="primary" @click="$router.push('/user')">
          用户管理
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getDashboardStats } from '@/api/statistics'

const userStore = useUserStore()

const stats = ref({
  userCount: 0,
  paperCount: 0,
  examCount: 0,
  questionCount: 0
})

const getRoleName = (role) => {
  const roleMap = {
    'ADMIN': '管理员',
    'TEACHER': '教师',
    'STUDENT': '学生'
  }
  return roleMap[role] || role
}

onMounted(async () => {
  try {
    const res = await getDashboardStats()
    stats.value = res.data
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
})
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.stat-card {
  border-radius: 8px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  font-size: 48px;
}

.stat-text {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #999;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.welcome-card {
  padding: 30px;
  border-radius: 8px;
}

.welcome-card h2 {
  margin: 0 0 15px 0;
  color: #333;
}

.welcome-card p {
  color: #666;
  font-size: 16px;
  margin-bottom: 20px;
}

.quick-links {
  margin-top: 30px;
}
</style>
