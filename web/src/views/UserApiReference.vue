<template>
  <div class="ref-container">
    <el-card class="ref-card">
      <template #header>统一接口</template>
      <div class="section">
        <div class="label">Base URL</div>
        <el-input v-model="baseUrl" readonly />
      </div>
      <div class="section">
        <div class="label">授权 Token</div>
        <el-input v-model="token" type="password" show-password readonly />
      </div>
      <div class="section">
        <div class="label">cURL 示例</div>
        <el-input type="textarea" :rows="8" v-model="curlExample" />
        <div class="actions"><el-button @click="copy(curlExample)">复制示例</el-button></div>
      </div>
      <div class="section">
        <div class="label">请求体样例</div>
        <el-input type="textarea" :rows="10" v-model="bodyExample" />
        <div class="actions"><el-button @click="copy(bodyExample)">复制示例</el-button></div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const baseUrl = ref(`${location.origin}/api/v1/chat/completions`)
const token = ref(localStorage.getItem('token') || '')

const bodyExample = ref(`{
  "model": "gpt-3.5-turbo",
  "messages": [
    { "role": "user", "content": "你好" }
  ],
  "stream": false
}`)

const curlExample = ref(`curl -X POST "${baseUrl.value}" \\
  -H "Content-Type: application/json" \\
  -H "Authorization: Bearer ${token.value}" \\
  -d '${bodyExample.value}'`)

const copy = async (text: string) => {
  await navigator.clipboard.writeText(text)
  ElMessage.success('已复制到剪贴板')
}

onMounted(() => {
  // refresh token on mount
  token.value = localStorage.getItem('token') || ''
  curlExample.value = `curl -X POST "${baseUrl.value}" \\
  -H "Content-Type: application/json" \\
  -H "Authorization: Bearer ${token.value}" \\
  -d '${bodyExample.value}'`
})
</script>

<style scoped>
.ref-container {
  max-width: 900px;
  margin: 0 auto;
}
.section {
  margin-bottom: 20px;
}
.label {
  margin-bottom: 8px;
  color: #606266;
}
.actions {
  margin-top: 8px;
  text-align: right;
}
</style>

