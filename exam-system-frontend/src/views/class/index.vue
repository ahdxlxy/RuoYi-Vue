<template>
  <div class="class-container">
    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">新增班级</el-button>
        <el-input
          v-model="keyword"
          placeholder="搜索班级名称"
          style="width: 200px; margin-left: auto"
          clearable
          @change="loadData"
        />
      </div>
      
      <el-table :data="tableData" style="margin-top: 20px" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="班级名称" />
        <el-table-column prop="grade" label="年级" />
        <el-table-column prop="profession" label="专业" />
        <el-table-column prop="studentCount" label="学生人数" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="success" @click="handleManageStudents(row)">管理学生</el-button>
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
    
    <!-- 新增/编辑班级对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="年级">
          <el-input v-model="form.grade" />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="form.profession" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 学生管理对话框 -->
    <el-dialog v-model="studentDialogVisible" title="添加学生到班级" width="700px">
      <div style="margin-bottom: 20px">
        <el-input
          v-model="studentKeyword"
          placeholder="搜索学生姓名/用户名"
          style="width: 250px"
          clearable
          @change="loadStudentList"
        >
          <template #append>
            <el-button @click="loadStudentList">搜索</el-button>
          </template>
        </el-input>
      </div>
      
      <el-table 
        :data="studentList" 
        border 
        height="400"
        @selection-change="val => selectedStudents = val.map(item => item.id)"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            {{ row.gender === 'M' ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleRemoveStudent(row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <template #footer>
        <el-button @click="studentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitStudents">确定添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getClassList, createClass, updateClass, deleteClass, addStudentsToClass, removeStudentsFromClass } from '@/api/class'
import { getUserList } from '@/api/user'

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增班级')
const formRef = ref()
const keyword = ref('')

// 学生管理相关
const studentDialogVisible = ref(false)
const studentList = ref([])
const selectedStudents = ref([])
const currentClassId = ref(null)
const studentKeyword = ref('')

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive({
  id: null,
  name: '',
  grade: '',
  profession: ''
})

const rules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await getClassList({
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

const handleAdd = () => {
  dialogTitle.value = '新增班级'
  Object.assign(form, { id: null, name: '', grade: '', profession: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑班级'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (form.id) {
      await updateClass(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createClass(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该班级吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteClass(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

// 打开学生管理对话框
const handleManageStudents = async (row) => {
  currentClassId.value = row.id
  studentDialogVisible.value = true
  selectedStudents.value = []
  await loadStudentList()
}

// 加载学生列表
const loadStudentList = async () => {
  try {
    const res = await getUserList({
      current: 1,
      size: 1000, // 获取所有学生，实际项目中可能需要分页处理
      role: 'STUDENT',
      keyword: studentKeyword.value
    })
    studentList.value = res.data.records
  } catch (error) {
    console.error(error)
  }
}

// 提交选中的学生
const handleSubmitStudents = async () => {
  if (selectedStudents.value.length === 0) {
    ElMessage.warning('请选择要添加的学生')
    return
  }
  
  try {
    await addStudentsToClass(currentClassId.value, selectedStudents.value)
    ElMessage.success('添加成功')
    studentDialogVisible.value = false
    loadData() // 刷新列表以更新人数
  } catch (error) {
    console.error(error)
  }
}

// 移除学生
const handleRemoveStudent = (row) => {
  ElMessageBox.confirm(`确定要将学生 ${row.realName} 从该班级移除吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await removeStudentsFromClass(currentClassId.value, [row.id])
      ElMessage.success('移除成功')
      await loadStudentList() // 刷新学生列表
      loadData() // 刷新班级列表以更新人数
    } catch (error) {
      console.error(error)
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.class-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  align-items: center;
}
</style>
