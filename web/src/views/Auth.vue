<template>
  <div class="auth-container">
    <el-card class="auth-card">
      <h2>{{ isLogin ? '用户登录' : '用户注册' }}</h2>
      <el-form :model="form" label-position="top" size="large">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item v-if="!isLogin" label="电子邮箱">
          <el-input v-model="form.email" placeholder="请输入电子邮箱" />
        </el-form-item>
        <el-button type="primary" :loading="loading" @click="handleAuth" block>{{ isLogin ? '登录' : '注册' }}</el-button>
        <div class="auth-toggle">
          {{ isLogin ? '还没有账号？' : '已有账号？' }}
          <el-link type="primary" @click="isLogin = !isLogin">{{ isLogin ? '立即注册' : '返回登录' }}</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const isLogin = ref(true)
const loading = ref(false)
const form = ref({
  username: '',
  password: '',
  email: ''
})

onMounted(() => {
  if (route.path === '/register') {
    isLogin.value = false
  }
})

const handleAuth = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  
  loading.value = true
  try {
    const endpoint = isLogin.value ? '/api/auth/login' : '/api/auth/register'
    const res = await axios.post(endpoint, form.value)
    const { setAuth } = await import('@/utils/auth')
    setAuth(res.data.token, { username: res.data.username, role: res.data.role })
    
    ElMessage.success(isLogin.value ? '登录成功' : '注册成功')
    router.push('/console')
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || (isLogin.value ? '登录失败' : '注册失败'))
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}

.auth-card {
  width: 400px;
  padding: 20px;
  border-radius: 12px;
}

.auth-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.auth-toggle {
  text-align: center;
  margin-top: 20px;
  font-size: 0.9rem;
  color: #606266;
}

.block {
  width: 100%;
}
</style>
