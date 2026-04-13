<template>
  <div class="logs-container">
    <el-table :data="logs" style="width: 100%" v-loading="loading">
      <el-table-column prop="createdAt" label="时间" width="180">
        <template #default="scope">
          {{ new Date(scope.row.createdAt).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column prop="model" label="模型" width="150" />
      <el-table-column prop="promptTokens" label="提示词 Token" width="120" />
      <el-table-column prop="completion_tokens" label="回答 Token" width="120" />
      <el-table-column prop="totalTokens" label="总计 Token" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'SUCCESS' ? 'success' : 'danger'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/http'
import { ElMessage } from 'element-plus'

const logs = ref([])
const loading = ref(false)

const fetchLogs = async () => {
  loading.value = true
  try {
    const res = await http.get('/api/user/logs')
    logs.value = res.data
  } catch (error) {
    ElMessage.error('获取日志失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchLogs()
})
</script>

<style scoped>
</style>
