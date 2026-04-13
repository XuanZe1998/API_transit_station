<template>
  <div class="wallet-container">
    <el-card class="balance-card">
      <template #header>我的钱包</template>
      <div class="balance-info">
        <div class="balance-label">当前余额</div>
        <div class="balance-value">{{ balance }} <span class="unit">Credits</span></div>
      </div>
      <div class="recharge-section">
        <h3>在线充值</h3>
        <el-radio-group v-model="rechargeAmount" class="recharge-options">
          <el-radio-button label="10">10 元 (1,000,000 Credits)</el-radio-button>
          <el-radio-button label="50">50 元 (5,000,000 Credits)</el-radio-button>
          <el-radio-button label="100">100 元 (10,000,000 Credits)</el-radio-button>
        </el-radio-group>
        <div class="recharge-btn">
          <el-button type="primary" size="large" @click="handleRecharge">立即充值</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/http'
import { ElMessage } from 'element-plus'

const balance = ref(0)
const rechargeAmount = ref('10')

const fetchBalance = async () => {
  try {
    const res = await http.get('/api/user/profile')
    balance.value = res.data.balance
  } catch (error) {
    ElMessage.error('获取余额失败')
  }
}

const handleRecharge = async () => {
  try {
    const res = await http.post('/api/user/wallet/recharge', null, {
      params: { amount: Number(rechargeAmount.value) }
    })
    balance.value = res.data.balance
    ElMessage.success('充值成功')
  } catch (e) {
    ElMessage.error('充值失败')
  }
}

onMounted(() => {
  fetchBalance()
})
</script>

<style scoped>
.balance-card {
  max-width: 600px;
  margin: 0 auto;
}

.balance-info {
  text-align: center;
  padding: 30px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 30px;
}

.balance-label {
  color: #909399;
  font-size: 1rem;
  margin-bottom: 10px;
}

.balance-value {
  font-size: 2.5rem;
  font-weight: bold;
  color: #67c23a;
}

.unit {
  font-size: 1rem;
  color: #909399;
}

.recharge-section h3 {
  margin-bottom: 20px;
  color: #303133;
}

.recharge-options {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 30px;
}

.recharge-btn {
  text-align: center;
}
</style>
