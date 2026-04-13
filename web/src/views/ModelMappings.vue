<template>
  <div class="mappings-container">
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">添加映射</el-button>
    </div>
    <el-table :data="mappings" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="publicModelName" label="公开模型名称" width="200" />
      <el-table-column prop="channelModelName" label="渠道模型名称" width="200" />
      <el-table-column prop="channel.name" label="渠道名称" width="150" />
      <el-table-column prop="priority" label="优先级" width="100" />
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

    <!-- Mapping Form Dialog -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑映射' : '添加映射'">
      <el-form :model="form" label-width="120px">
        <el-form-item label="公开模型名称">
          <el-input v-model="form.publicModelName" placeholder="如: gpt-4" />
        </el-form-item>
        <el-form-item label="渠道模型名称">
          <el-input v-model="form.channelModelName" placeholder="如: deepseek-reasoner" />
        </el-form-item>
        <el-form-item label="选择渠道">
          <el-select v-model="form.channelId" placeholder="选择渠道">
            <el-option
              v-for="channel in channels"
              :key="channel.id"
              :label="channel.name"
              :value="channel.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-input-number v-model="form.priority" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveMapping">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const mappings = ref([])
const channels = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = ref({
  id: null,
  publicModelName: '',
  channelModelName: '',
  channelId: null,
  priority: 10,
  enabled: true
})

const fetchMappings = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/mappings')
    mappings.value = res.data
  } catch (error) {
    ElMessage.error('获取映射失败')
  } finally {
    loading.value = false
  }
}

const fetchChannels = async () => {
  try {
    const res = await axios.get('/api/channels')
    channels.value = res.data
  } catch (error) {
    ElMessage.error('获取渠道失败')
  }
}

const handleAdd = () => {
  form.value = {
    id: null,
    publicModelName: '',
    channelModelName: '',
    channelId: null,
    priority: 10,
    enabled: true
  }
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  form.value = { ...row, channelId: row.channel.id }
  dialogVisible.value = true
}

const saveMapping = async () => {
  try {
    if (form.value.id) {
      await axios.put(`/api/mappings/${form.value.id}`, form.value)
    } else {
      await axios.post('/api/mappings', form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchMappings()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该映射吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/api/mappings/${row.id}`)
      ElMessage.success('删除成功')
      fetchMappings()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  fetchMappings()
  fetchChannels()
})
</script>

<style scoped>
.action-bar {
  margin-bottom: 20px;
  text-align: left;
}
</style>
