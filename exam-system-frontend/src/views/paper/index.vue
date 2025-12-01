<template>
  <div class="paper-container">
    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="$router.push('/paper/create')">创建试卷</el-button>
        <el-input
          v-model="keyword"
          placeholder="搜索试卷名称"
          style="width: 200px; margin-left: auto"
          clearable
          @change="loadData"
        />
      </div>
      
      <el-table :data="tableData" style="margin-top: 20px" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="试卷名称" />
        <el-table-column prop="subject" label="科目" />
        <el-table-column prop="totalScore" label="总分" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handlePreview(row)">预览</el-button>
            <el-button link type="warning" @click="handleEdit(row)">编辑</el-button>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPaperList, deletePaper } from '@/api/paper'

const router = useRouter()
const tableData = ref([])
const keyword = ref('')

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const loadData = async () => {
  try {
    const res = await getPaperList({
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

const handlePreview = (row) => {
  // 预览试卷功能
  ElMessage.info('预览功能')
}

const handleEdit = (row) => {
  router.push(`/paper/create?id=${row.id}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该试卷吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deletePaper(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.paper-container {
  padding: 20px;
}

.toolbar {
  display: flex;
align-items: center;
}
</style>
