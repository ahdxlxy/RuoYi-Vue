<template>
  <div class="grade-container">
    <el-card>
      <div class="toolbar">
        <el-select v-model="searchForm.examId" placeholder="选择考试" clearable style="width: 200px; margin-right: 10px">
          <el-option
            v-for="exam in exams"
            :key="exam.id"
            :label="exam.examName"
            :value="exam.id"
          />
        </el-select>
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      
      <el-table :data="tableData" style="margin-top: 20px" border>
        <el-table-column prop="examName" label="考试名称" />
        <el-table-column prop="studentName" label="学生姓名" />
        <el-table-column prop="totalScore" label="得分" width="100">
          <template #default="{ row }">
            <span style="font-weight: bold; color: #409eff">{{ row.totalScore }}</span> / {{ row.paperTotalScore }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'GRADING'" type="warning">阅卷中</el-tag>
            <el-tag v-else type="success">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleGrade(row)">阅卷</el-button>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getGradeList } from '@/api/grade'
import { getExamList } from '@/api/exam'

const router = useRouter()
const tableData = ref([])
const exams = ref([])

const searchForm = reactive({
  examId: null
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const loadData = async () => {
  try {
    const res = await getGradeList({
      current: pagination.current,
      size: pagination.size,
      examId: searchForm.examId
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const loadExams = async () => {
  try {
    const res = await getExamList({ current: 1, size: 100 })
    exams.value = res.data.records
  } catch (error) {
    console.error(error)
  }
}

const handleGrade = (row) => {
  router.push(`/grade/marking?examId=${row.examId}&studentId=${row.studentId}`)
}

onMounted(() => {
  loadData()
  loadExams()
})
</script>

<style scoped>
.grade-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  align-items: center;
}
</style>
