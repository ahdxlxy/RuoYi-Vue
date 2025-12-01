<template>
  <div class="marking-container">
    <el-card>
      <h2>人工阅卷</h2>
      <div class="student-info">
        <p>学生：{{ gradeInfo?.studentName }}</p>
        <p>考试：{{ gradeInfo?.examName }}</p>
        <p>得分：<span class="score">{{ gradeInfo?.totalScore }}</span> / {{ gradeInfo?.paperTotalScore }}</p>
      </div>
      
      <div v-for="(answer, index) in answers" :key="answer.id" class="answer-item">
        <div class="question-header">
          <span class="question-number">第{{ index + 1 }}题</span>
          <el-tag>{{ getTypeName(answer.question.type) }}</el-tag>
        </div>
        <div class="question-content">{{ answer.question.content }}</div>
        <div class="answer-section">
          <p><strong>学生答案：</strong>{{ answer.answer || '未作答' }}</p>
          <p><strong>标准答案：</strong>{{ answer.question.answer }}</p>
          <div class="score-input" v-if="answer.question.type === 'ESSAY'">
            <span>评分：</span>
            <el-input-number
              v-model="answer.score"
              :min="0"
              :max="100"
              @change="handleScoreChange(answer)"
            />
          </div>
          <div v-else>
            <el-tag v-if="answer.score !== null" :type="answer.score > 0 ? 'success' : 'danger'">
              {{ answer.score > 0 ? '正确' : '错误' }} ({{ answer.score }}分)
            </el-tag>
          </div>
        </div>
      </div>
      
      <div class="actions">
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="handleSave">保存评分</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getGradeDetail, manualGrade } from '@/api/grade'
import { getAnswersByExam } from '@/api/answer'

const router = useRouter()
const route = useRoute()
const gradeInfo = ref(null)
const answers = ref([])

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

const handleScoreChange = async (answer) => {
  // 实时保存分数
}

const handleSave = async () => {
  try {
    for (const answer of answers.value) {
      if (answer.question.type === 'ESSAY' && answer.score !== null) {
        await manualGrade({
          examId: route.query.examId,
          studentId: route.query.studentId,
          questionId: answer.question.id,
          score: answer.score
        })
      }
    }
    ElMessage.success('保存成功')
    router.back()
  } catch (error) {
    console.error(error)
  }
}

onMounted(async () => {
  try {
    const gradeRes = await getGradeDetail(route.query.examId, route.query.studentId)
    gradeInfo.value = gradeRes.data
    
    const answerRes = await getAnswersByExam(route.query.examId, route.query.studentId)
    answers.value = answerRes.data
  } catch (error) {
    console.error(error)
  }
})
</script>

<style scoped>
.marking-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.student-info {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 20px;
}

.student-info p {
  margin: 10px 0;
  font-size: 16px;
}

.score {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.answer-item {
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
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

.question-content {
  font-size: 16px;
  margin-bottom: 15px;
  line-height: 1.6;
}

.answer-section {
  padding: 15px;
  background: #f5f7fa;
  border-radius: 5px;
}

.answer-section p {
  margin: 10px 0;
}

.score-input {
  margin-top: 15px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.actions {
  margin-top: 30px;
  text-align: center;
}
</style>
