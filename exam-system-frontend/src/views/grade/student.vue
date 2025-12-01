<template>
  <div class="student-grade-container">
    <el-card>
      <h2>我的成绩</h2>
      
      <el-table :data="tableData" style="margin-top: 20px" border>
        <el-table-column prop="examName" label="考试名称" />
        <el-table-column prop="totalScore" label="得分" width="150">
          <template #default="{ row }">
            <span style="font-size: 20px; font-weight: bold; color: #409eff">
              {{ row.totalScore }}
            </span> / {{ row.paperTotalScore }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'GRADING'" type="warning">阅卷中</el-tag>
            <el-tag v-else type="success">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="180" />
      </el-table>
      
      <el-empty v-if="tableData.length === 0" description="暂无成绩记录" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStudentGrades } from '@/api/grade'

const tableData = ref([])

const loadData = async () => {
  try {
    const res = await getStudentGrades()
    tableData.value = res.data
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.student-grade-container {
  padding: 20px;
}
</style>
