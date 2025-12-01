<template>
  <div class="question-container">
    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">新增题目</el-button>
        <el-upload
          action="#"
          :http-request="handleImport"
          :show-file-list="false"
          accept=".xlsx, .xls"
          style="margin-left: 10px"
        >
          <el-button type="success">导入题目</el-button>
        </el-upload>
        <div class="search-box" style="margin-left: auto">
          <el-select v-model="searchForm.type" placeholder="题型" clearable style="width: 120px; margin-right: 10px">
            <el-option label="单选题" value="SINGLE" />
            <el-option label="多选题" value="MULTIPLE" />
            <el-option label="判断题" value="JUDGE" />
            <el-option label="填空题" value="BLANK" />
            <el-option label="简答题" value="ESSAY" />
          </el-select>
          <el-input
            v-model="searchForm.keyword"
            placeholder="搜索题目内容"
            style="width: 200px; margin-right: 10px"
            clearable
          />
          <el-button type="primary" @click="loadData">查询</el-button>
        </div>
      </div>
      
      <el-table :data="tableData" style="margin-top: 20px" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="type" label="题型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.type === 'SINGLE'" type="primary">单选</el-tag>
            <el-tag v-else-if="row.type === 'MULTIPLE'" type="success">多选</el-tag>
            <el-tag v-else-if="row.type === 'JUDGE'" type="warning">判断</el-tag>
            <el-tag v-else-if="row.type === 'BLANK'" type="info">填空</el-tag>
            <el-tag v-else>简答</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="subject" label="科目" width="120" />
        <el-table-column prop="content" label="题目内容" show-overflow-tooltip />
        <el-table-column prop="difficulty" label="难度" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.difficulty === 'EASY'" type="success">简单</el-tag>
            <el-tag v-else-if="row.difficulty === 'MEDIUM'" type="warning">中等</el-tag>
            <el-tag v-else type="danger">困难</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        layout="total, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end"
        @current-change="loadData"
      />
    </el-card>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="题型" prop="type">
          <el-select v-model="form.type" style="width: 100%">
            <el-option label="单选题" value="SINGLE" />
            <el-option label="多选题" value="MULTIPLE" />
            <el-option label="判断题" value="JUDGE" />
            <el-option label="填空题" value="BLANK" />
            <el-option label="简答题" value="ESSAY" />
          </el-select>
        </el-form-item>
        <el-form-item label="科目">
          <el-input v-model="form.subject" />
        </el-form-item>
        <el-form-item label="题目内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="选项" v-if="['SINGLE', 'MULTIPLE', 'JUDGE'].includes(form.type)">
          <el-input v-model="form.options" type="textarea" :rows="4" placeholder='JSON格式，例如：["A. 选项1", "B. 选项2"]' />
        </el-form-item>
        <el-form-item label="答案" prop="answer">
          <el-input v-model="form.answer" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="form.difficulty" style="width: 100%">
            <el-option label="简单" value="EASY" />
            <el-option label="中等" value="MEDIUM" />
            <el-option label="困难" value="HARD" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import { getQuestionList, createQuestion, updateQuestion, deleteQuestion, importQuestions } from '@/api/question'

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增题目')
const formRef = ref()

const searchForm = reactive({
  type: '',
  keyword: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  type: 'SINGLE',
  subject: '',
  content: '',
  options: '',
  answer: '',
  difficulty: 'MEDIUM'
})

const rules = {
  type: [{ required: true, message: '请选择题型', trigger: 'change' }],
  content: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
  answer: [{ required: true, message: '请输入答案', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await getQuestionList({
      current: pagination.current,
      size: pagination.size,
      type: searchForm.type,
      keyword: searchForm.keyword
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增题目'
  Object.assign(form, {
    id: null,
    type: 'SINGLE',
    subject: '',
    content: '',
    options: '',
    answer: '',
    difficulty: 'MEDIUM'
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑题目'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (form.id) {
      await updateQuestion(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createQuestion(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该题目吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteQuestion(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleImport = async (options) => {
  const formData = new FormData()
  formData.append('file', options.file)
  try {
    await importQuestions(formData)
    ElMessage.success('导入成功')
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('导入失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.question-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  align-items: center;
}

.search-box {
  display: flex;
  align-items: center;
}
</style>
