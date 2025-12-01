<template>
  <div class="exam-container">
    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">创建考试</el-button>
        <el-input
          v-model="keyword"
          placeholder="搜索考试名称"
          style="width: 200px; margin-left: auto"
          clearable
          @change="loadData"
        />
      </div>
      
      <el-table :data="tableData" style="margin-top: 20px" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="examName" label="考试名称" />
        <el-table-column prop="paper.name" label="试卷名称" />
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="duration" label="时长(分钟)" width="100" />
        <el-table-column prop="className" label="班级" />
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
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="考试名称" prop="examName">
          <el-input v-model="form.examName" />
        </el-form-item>
        <el-form-item label="选择试卷" prop="paperId">
          <el-select v-model="form.paperId" style="width: 100%">
            <el-option
              v-for="paper in papers"
              :key="paper.id"
              :label="paper.name"
              :value="paper.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="考试时长" prop="duration">
          <el-input-number v-model="form.duration" :min="1" /> 分钟
        </el-form-item>
        <el-form-item label="指定班级">
          <el-select v-model="form.classId" clearable placeholder="不选择表示全体可参加" style="width: 100%">
            <el-option
              v-for="cls in classes"
              :key="cls.id"
              :label="cls.name"
              :value="cls.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="考试密码">
          <el-input v-model="form.password" placeholder="留空表示无密码" />
        </el-form-item>
        <el-form-item label="允许补考">
          <el-switch v-model="form.allowMakeup" :active-value="1" :inactive-value="0" />
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
import { getExamList, createExam, updateExam, deleteExam } from '@/api/exam'
import { getPaperList } from '@/api/paper'
import { getAllClasses } from '@/api/class'

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('创建考试')
const formRef = ref()
const keyword = ref('')
const papers = ref([])
const classes = ref([])

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  examName: '',
  paperId: null,
  startTime: '',
  endTime: '',
  duration: 120,
  classId: null,
  password: '',
  allowMakeup: 0
})

const rules = {
  examName: [{ required: true, message: '请输入考试名称', trigger: 'blur' }],
  paperId: [{ required: true, message: '请选择试卷', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  duration: [{ required: true, message: '请输入考试时长', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await getExamList({
      current: pagination.current,
      size: pagination.size,
      keyword: keyword.value
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const loadPapers = async () => {
  try {
    const res = await getPaperList({ current: 1, size: 100 })
    papers.value = res.data.records
  } catch (error) {
    console.error(error)
  }
}

const loadClasses = async () => {
  try {
    const res = await getAllClasses()
    classes.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '创建考试'
  Object.assign(form, {
    id: null,
    examName: '',
    paperId: null,
    startTime: '',
    endTime: '',
    duration: 120,
    classId: null,
    password: '',
    allowMakeup: 0
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑考试'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (form.id) {
      await updateExam(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createExam(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该考试吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteExam(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

onMounted(() => {
  loadData()
  loadPapers()
  loadClasses()
})
</script>

<style scoped>
.exam-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  align-items: center;
}
</style>
