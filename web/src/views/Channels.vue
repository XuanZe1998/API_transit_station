<template>
  <div class="channels-container">
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">添加渠道</el-button>
    </div>
    <el-table :data="channels" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="名称" width="120" />
      <el-table-column prop="type" label="类型" width="100">
        <template #default="scope">
          <el-tag>{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="baseUrl" label="Base URL" />
      <el-table-column prop="enabled" label="状态" width="80">
        <template #default="scope">
          <el-switch v-model="scope.row.enabled" @change="toggleChannel(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Channel Form Dialog -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑渠道' : '添加渠道'">
      <el-form :model="form" label-width="100px">
        <el-form-item label="渠道名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="渠道类型">
          <el-select v-model="form.type" placeholder="选择类型">
            <el-option label="OpenAI" value="openai" />
            <el-option label="Anthropic" value="anthropic" />
            <el-option label="Gemini" value="gemini" />
            <el-option label="DeepSeek" value="deepseek" />
          </el-select>
        </el-form-item>
        <el-form-item label="Base URL">
          <el-input v-model="form.baseUrl" placeholder="https://api.openai.com" />
        </el-form-item>
        <el-form-item label="API Key">
          <el-input v-model="form.apiKey" type="password" show-password />
        </el-form-item>
        <el-form-item label="支持模型">
          <el-input v-model="form.models" placeholder="用逗号分隔，如: gpt-4,gpt-3.5-turbo" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveChannel">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const channels = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = ref({
  id: null,
  name: '',
  type: 'openai',
  baseUrl: '',
  apiKey: '',
  models: '',
  enabled: true
})

const fetchChannels = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/channels')
    channels.value = res.data
  } catch (error) {
    ElMessage.error('获取渠道失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  form.value = {
    id: null,
    name: '',
    type: 'openai',
    baseUrl: '',
    apiKey: '',
    models: '',
    enabled: true
  }
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  form.value = { ...row }
  dialogVisible.value = true
}

const saveChannel = async () => {
  try {
    if (form.value.id) {
      await axios.put(`/api/channels/${form.value.id}`, form.value)
    } else {
      await axios.post('/api/channels', form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchChannels()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const toggleChannel = async (row: any) => {
  try {
    await axios.put(`/api/channels/${row.id}`, row)
    ElMessage.success(row.enabled ? '已启用' : '已禁用')
  } catch (error) {
    ElMessage.error('操作失败')
    row.enabled = !row.enabled
  }
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该渠道吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/api/channels/${row.id}`)
      ElMessage.success('删除成功')
      fetchChannels()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  fetchChannels()
})
</script>

<style scoped>
.action-bar {
  margin-bottom: 20px;
  text-align: left;
}
</style>
