<template>
  <div class="paper-create-container">
    <el-card>
      <h2>{{ isEdit ? '编辑试卷' : '创建试卷' }}</h2>
      <el-form :model="form" label-width="100px" style="margin-top: 20px">
        <el-form-item label="试卷名称">
          <el-input v-model="form.name" style="width: 400px" />
        </el-form-item>
        <el-form-item label="科目">
          <el-input v-model="form.subject" style="width: 400px" />
        </el-form-item>
      </el-form>
      
      <div class="question-selector">
        <h3>选择题目</h3>
        <el-button type="primary" @click="showQuestionDialog = true">添加题目</el-button>
        
        <el-table :data="selectedQuestions" style="margin-top: 20px" border>
          <el-table-column prop="sort" label="序号" width="80" />
          <el-table-column prop="question.content" label="题目内容" />
          <el-table-column prop="question.type" label="题型" width="100">
            <template #default="{ row }">
              <el-tag>{{ getTypeName(row.question.type) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="分数" width="120">
            <template #default="{ row }">
              <el-input-number v-model="row.score" :min="1" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="{ row, $index }">
              <el-button link type="danger" @click="removeQuestion($index)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="total-score">
          总分：{{ totalScore }}
        </div>
      </div>
      
      <div class="actions">
        <el-button @click="$router.back()">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </div>
    </el-card>
    
    <!-- 选择题目对话框 -->
    <el-dialog v-model="showQuestionDialog" title="选择题目" width="800px">

      <el-table :data="questions" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="content" label="题目内容" show-overflow-tooltip />
        <el-table-column prop="type" label="题型" width="100">
          <template #default="{ row }">
            <el-tag>{{ getTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 15px; display: flex; justify-content: flex-end;">
        <el-pagination
            v-model:current-page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            :total="total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="loadQuestions"    @current-change="loadQuestions" />
      </div>

      <template #footer>
        <el-button @click="showQuestionDialog = false">取消</el-button>
        <el-button type="primary" @click="addQuestions">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getPaperById, createPaper, updatePaper } from '@/api/paper'
import { getQuestionList } from '@/api/question'

const router = useRouter()
const route = useRoute()
const isEdit = ref(false)
const showQuestionDialog = ref(false)
const questions = ref([])
const selectedFromDialog = ref([])

const total = ref(0)
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  content: '' // 如果以后想加搜索功能可以用这个
})

const form = reactive({
  name: '',
  subject: ''
})

const selectedQuestions = ref([])

const totalScore = computed(() => {
  return selectedQuestions.value.reduce((sum, item) => sum + (item.score || 0), 0)
})

const getTypeName = (type) => {
  const map = {
    'SINGLE': '单选',
    'MULTIPLE': '多选',
    'JUDGE': '判断',
    'BLANK': '填空',
    'ESSAY': '简答'
  }
  return map[type] || type
}

const handleSelectionChange = (selection) => {
  selectedFromDialog.value = selection
}

const addQuestions = () => {
  selectedFromDialog.value.forEach(q => {
    if (!selectedQuestions.value.find(item => item.question.id === q.id)) {
      selectedQuestions.value.push({
        questionId: q.id,
        question: q,
        score: 10,
        sort: selectedQuestions.value.length + 1
      })
    }
  })
  showQuestionDialog.value = false
}

const removeQuestion = (index) => {
  selectedQuestions.value.splice(index, 1)
  // 重新排序
  selectedQuestions.value.forEach((item, idx) => {
    item.sort = idx + 1
  })
}

const handleSubmit = async () => {
  if (!form.name) {
    ElMessage.error('请输入试卷名称')
    return
  }
  
  if (selectedQuestions.value.length === 0) {
    ElMessage.error('请至少选择一道题目')
    return
  }
  
  const data = {
    ...form,
    questions: selectedQuestions.value.map(item => ({
      questionId: item.questionId,
      score: item.score,
      sort: item.sort
    }))
  }
  
  try {
    if (isEdit.value) {
      await updatePaper(route.query.id, data)
      ElMessage.success('更新成功')
    } else {
      await createPaper(data)
      ElMessage.success('创建成功')
    }
    router.back()
  } catch (error) {
    console.error(error)
  }
}

const loadQuestions = async () => {
  try {
    const res = await getQuestionList({
      // 左边是后端要的参数名，右边是你前端变化的变量
      current: queryParams.pageNum,
      size: queryParams.pageSize,
      // 如果还有查询条件，也可以带上
      // content: queryParams.content
    })

    // 赋值数据
    questions.value = res.data.records
    total.value = res.data.total

  } catch (error) {
    console.error(error)
  }
}

onMounted(async () => {
  loadQuestions()
  
  if (route.query.id) {
    isEdit.value = true
    try {
      const res = await getPaperById(route.query.id)
      Object.assign(form, res.data)
      selectedQuestions.value = res.data.questions || []
    } catch (error) {
      console.error(error)
    }
  }
})
</script>

<style scoped>
.paper-create-container {
  padding: 20px;
}

.question-selector {
  margin-top: 30px;
}

.total-score {
  margin-top: 20px;
  font-size: 18px;
  font-weight: bold;
  text-align: right;
  color: #409eff;
}

.actions {
  margin-top: 30px;
  text-align: center;
}
</style>
