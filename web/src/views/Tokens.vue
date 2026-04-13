<template>
  <div class="tokens-container">
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">生成令牌</el-button>
    </div>
    <el-table :data="tokens" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="名称" width="120" />
      <el-table-column prop="key" label="令牌" width="280">
        <template #default="scope">
          <el-text class="w-150px" truncated>{{ scope.row.key }}</el-text>
          <el-button size="small" link type="primary" @click="copyToClipboard(scope.row.key)">复制</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="totalQuota" label="总额度" width="120">
        <template #default="scope">
          {{ scope.row.totalQuota / 1000 }} K
        </template>
      </el-table-column>
      <el-table-column prop="usedQuota" label="已用" width="120">
        <template #default="scope">
          {{ scope.row.usedQuota / 1000 }} K
        </template>
      </el-table-column>
      <el-table-column prop="enabled" label="状态" width="80">
        <template #default="scope">
          <el-switch v-model="scope.row.enabled" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Token Form Dialog -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑令牌' : '生成令牌'">
      <el-form :model="form" label-width="100px">
        <el-form-item label="令牌名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="令牌Key" v-if="form.id">
          <el-input v-model="form.key" readonly />
        </el-form-item>
        <el-form-item label="总额度 (Token)">
          <el-input-number v-model="form.totalQuota" :min="0" :step="1000" />
          <div class="tip">1 K = 1000 tokens</div>
        </el-form-item>
        <el-form-item label="过期时间">
          <el-date-picker v-model="form.expiredAt" type="datetime" placeholder="选择日期时间" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveToken">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const tokens = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = ref({
  id: null,
  name: '',
  key: '',
  totalQuota: 1000000,
  expiredAt: null,
  enabled: true
})

const fetchTokens = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/tokens')
    tokens.value = res.data
  } catch (error) {
    ElMessage.error('获取令牌失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  form.value = {
    id: null,
    name: '',
    key: '',
    totalQuota: 1000000,
    expiredAt: null,
    enabled: true
  }
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  form.value = { ...row }
  dialogVisible.value = true
}

const saveToken = async () => {
  try {
    if (form.value.id) {
      await axios.put(`/api/tokens/${form.value.id}`, form.value)
    } else {
      await axios.post('/api/tokens', form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchTokens()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const copyToClipboard = (text: string) => {
  navigator.clipboard.writeText(text).then(() => {
    ElMessage.success('复制成功')
  })
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该令牌吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/api/tokens/${row.id}`)
      ElMessage.success('删除成功')
      fetchTokens()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  fetchTokens()
})
</script>

<style scoped>
.action-bar {
  margin-bottom: 20px;
  text-align: left;
}
.tip {
  font-size: 0.8rem;
  color: #909399;
  margin-top: 5px;
}
</style>
