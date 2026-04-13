<template>
  <div class="dashboard-container">
    <div class="stats-grid">
      <el-card class="stat-card">
        <template #header>账户余额</template>
        <div class="stat-value">{{ balance }} <span class="unit">Credits</span></div>
      </el-card>
      <el-card class="stat-card">
        <template #header>累计请求次数</template>
        <div class="stat-value">{{ requestCount }}</div>
      </el-card>
      <el-card class="stat-card">
        <template #header>总消耗 Token</template>
        <div class="stat-value">{{ totalTokensUsed }}</div>
      </el-card>
    </div>
    <div class="usage-chart">
      <!-- Usage chart placeholder -->
      <el-card>
        <template #header>最近 7 天使用趋势</template>
        <div class="chart-placeholder">
          <p>图表正在加载中...</p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/http'
import { ElMessage } from 'element-plus'

const balance = ref(0)
const requestCount = ref(0)
const totalTokensUsed = ref(0)
const loading = ref(false)

const fetchStats = async () => {
  loading.value = true
  try {
    const res = await http.get('/api/user/stats')
    balance.value = res.data.balance
    requestCount.value = res.data.requestCount
    totalTokensUsed.value = res.data.totalTokensUsed
  } catch (error) {
    ElMessage.error('获取统计数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  text-align: center;
}

.stat-value {
  font-size: 2rem;
  font-weight: bold;
  color: #409eff;
}

.unit {
  font-size: 0.9rem;
  color: #909399;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
  color: #909399;
}
</style>
