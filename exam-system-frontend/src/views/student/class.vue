<template>
  <div class="my-class-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的班级</span>
        </div>
      </template>
      
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="name" label="班级名称" />
        <el-table-column prop="grade" label="年级" />
        <el-table-column prop="profession" label="专业" />
        <el-table-column prop="studentCount" label="班级人数" />
        <el-table-column prop="createTime" label="加入时间" />
      </el-table>
      
      <div v-if="tableData.length === 0" class="empty-text">
        暂未加入任何班级
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyClass } from '@/api/class'

const tableData = ref([])

const loadData = async () => {
  try {
    const res = await getMyClass()
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
.my-class-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty-text {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}
</style>
