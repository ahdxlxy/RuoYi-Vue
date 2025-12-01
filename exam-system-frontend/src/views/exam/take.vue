<template>
  <div class="exam-take-container">
    <el-card>
      <div class="exam-header">
        <h2>{{ examInfo?.examName }}</h2>
        <div class="timer">
          <el-icon><Clock /></el-icon>
          <span>剩余时间：{{ formatTime(remainingTime) }}</span>
        </div>
      </div>
      
      <div class="exam-content">
        <el-scrollbar height="calc(100vh - 280px)">
          <div v-for="(item, index) in questions" :key="item.id" class="question-item">
            <div class="question-header">
              <span class="question-number">第{{ index + 1 }}题</span>
              <el-tag>{{ getTypeName(item.question.type) }}</el-tag>
              <span class="question-score">{{ item.score }}分</span>
            </div>
            <div class="question-content">{{ item.question.content }}</div>
            
            <!-- 单选题 -->
            <el-radio-group
              v-if="item.question.type === 'SINGLE'"
              v-model="answers[item.question.id]"
              @change="saveCurrentAnswer(item.question.id)"
            >
              <el-radio
                v-for="(option, idx) in parseOptions(item.question.options)"
                :key="idx"
                :label="option"
                style="display: block; margin: 10px 0"
              >
                {{ option }}
              </el-radio>
            </el-radio-group>
            
            <!-- 判断题 -->
            <el-radio-group
              v-if="item.question.type === 'JUDGE'"
              v-model="answers[item.question.id]"
              @change="saveCurrentAnswer(item.question.id)"
            >
              <el-radio label="正确" style="margin-right: 20px">正确</el-radio>
              <el-radio label="错误">错误</el-radio>
            </el-radio-group>
            
            <!-- 填空、简答题 -->
            <el-input
              v-if="['BLANK', 'ESSAY'].includes(item.question.type)"
              v-model="answers[item.question.id]"
              :type="item.question.type === 'ESSAY' ? 'textarea' : 'text'"
              :rows="item.question.type === 'ESSAY' ? 5 : 1"
              placeholder="请输入答案"
              @blur="saveCurrentAnswer(item.question.id)"
            />
          </div>
        </el-scrollbar>
      </div>
      
      <div class="exam-footer">
        <el-button type="danger" @click="handleSubmit">提交试卷</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getExamById } from '@/api/exam'
import { getPaperById } from '@/api/paper'
import { saveAnswer, submitExam } from '@/api/answer'

const router = useRouter()
const route = useRoute()
const examInfo = ref(null)
const questions = ref([])
const answers = reactive({})
const remainingTime = ref(0)
let timer = null

const getTypeName = (type) => {
  const map = {
    'SINGLE': '单选题',
    'MULTIPLE': '多选题',
    'JUDGE': '判断题',
    'BLANK': '填空题',
    'ESSAY': '简答题'
  }
  return map[type] || type
}

const parseOptions = (optionsStr) => {
  try {
    return JSON.parse(optionsStr)
  } catch {
    return []
  }
}

const formatTime = (seconds) => {
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60
  return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

const saveCurrentAnswer = async (questionId) => {
  try {
    await saveAnswer({
      examId: route.params.id,
      questionId,
      answer: answers[questionId] || ''
    })
  } catch (error) {
    console.error(error)
  }
}

const handleSubmit = () => {
  ElMessageBox.confirm('确定要提交试卷吗？提交后将无法修改', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await submitExam(route.params.id)
      ElMessage.success('提交成功')
      router.push('/grade/student')
    } catch (error) {
      console.error(error)
    }
  })
}

const startTimer = () => {
  timer = setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value--
    } else {
      clearInterval(timer)
      ElMessage.warning('考试时间已到，自动提交')
      submitExam(route.params.id).then(() => {
        router.push('/grade/student')
      })
    }
  }, 1000)
}

onMounted(async () => {
  try {
    const examRes = await getExamById(route.params.id)
    examInfo.value = examRes.data
    
    const paperRes = await getPaperById(examInfo.value.paperId)
    questions.value = paperRes.data.questions || []
    
   // 计算剩余时间
    remainingTime.value = examInfo.value.duration * 60
    
    startTimer()
  } catch (error) {
    console.error(error)
  }
})

onBeforeUnmount(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.exam-take-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.exam-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #409eff;
  margin-bottom: 20px;
}

.exam-header h2 {
  margin: 0;
}

.timer {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}

.question-item {
  padding: 20px;
  margin-bottom: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.question-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.question-number {
  font-weight: bold;
  font-size: 16px;
}

.question-score {
  margin-left: auto;
  color: #409eff;
  font-weight: bold;
}

.question-content {
  font-size: 16px;
  margin-bottom: 15px;
  line-height: 1.6;
}

.exam-footer {
  margin-top: 20px;
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
</style>
