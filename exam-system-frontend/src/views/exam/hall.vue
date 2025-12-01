<template>
  <div class="exam-hall-container">
    <el-card>
      <h2>考试大厅</h2>
      <p style="color: #666; margin-bottom: 20px">以下是您可以参加的考试</p>
      
      <el-row :gutter="20">
        <el-col :span="8" v-for="exam in exams" :key="exam.id">
          <el-card class="exam-card" shadow="hover">
            <div class="exam-header">
              <h3>{{ exam.examName }}</h3>
              <el-tag type="primary" style="margin-right: 10px">{{ exam.paper?.subject }}</el-tag>
              <el-tag v-if="isExamEnded(exam)" type="danger">已结束</el-tag>
              <el-tag v-else-if="!isExamStarted(exam)" type="info">未开始</el-tag>
              <el-tag v-else type="success">进行中</el-tag>
            </div>
            <div class="exam-info">
              <p><el-icon><Document /></el-icon> 试卷：{{ exam.paper?.name }}</p>
              <p><el-icon><Clock /></el-icon> 时长：{{ exam.duration }}分钟</p>
              <p><el-icon><Calendar /></el-icon> 开始：{{ exam.startTime }}</p>
              <p><el-icon><Calendar /></el-icon> 结束：{{ exam.endTime }}</p>
              <p v-if="exam.password"><el-icon><Lock /></el-icon> 需要密码</p>
            </div>
            <el-button 
              type="primary" 
              style="width: 100%; margin-top: 10px" 
              :disabled="!isExamStarted(exam) || isExamEnded(exam)"
              @click="handleStartExam(exam)"
            >
              {{ isExamEnded(exam) ? '已结束' : (isExamStarted(exam) ? '开始考试' : '尚未开始') }}
            </el-button>
          </el-card>
        </el-col>
      </el-row>
      
      <el-empty v-if="exams.length === 0" description="暂无可参加的考试" />
    </el-card>
    
    <!-- 密码输入对话框 -->
    <el-dialog v-model="passwordDialogVisible" title="请输入考试密码" width="400px">
      <el-input v-model="examPassword" placeholder="请输入密码" />
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmStartExam">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getAvailableExams, startExam } from '@/api/exam'

const router = useRouter()
const exams = ref([])
const passwordDialogVisible = ref(false)
const examPassword = ref('')
const currentExam = ref(null)

const loadExams = async () => {
  try {
    const res = await getAvailableExams()
    exams.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const isExamStarted = (exam) => {
  const now = new Date()
  const startTime = new Date(exam.startTime)
  return now >= startTime
}

const isExamEnded = (exam) => {
  const now = new Date()
  const endTime = new Date(exam.endTime)
  return now > endTime
}

const handleStartExam = (exam) => {
  currentExam.value = exam
  if (exam.password) {
    passwordDialogVisible.value = true
  } else {
    confirmStartExam()
  }
}

const confirmStartExam = async () => {
  try {
    await startExam(currentExam.value.id, examPassword.value)
    ElMessage.success('开始考试')
    router.push(`/exam/take/${currentExam.value.id}`)
    passwordDialogVisible.value = false
    examPassword.value = ''
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadExams()
})
</script>

<style scoped>
.exam-hall-container {
  padding: 20px;
}

.exam-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.exam-card:hover {
  transform: translateY(-5px);
}

.exam-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.exam-header h3 {
  margin: 0;
  font-size: 18px;
}

.exam-info p {
  margin: 8px 0;
  color: #666;
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>
