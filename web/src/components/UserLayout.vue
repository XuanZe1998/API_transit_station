<template>
  <el-container class="user-layout">
    <el-header class="user-header">
      <div class="header-left">
        <div class="logo" @click="$router.push('/')">API Transit</div>
        <el-menu mode="horizontal" :default-active="activeMenu" router class="header-menu">
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/market">模型广场</el-menu-item>
          <el-menu-item v-if="isLoggedIn" index="/console">控制台</el-menu-item>
        </el-menu>
      </div>
      <div class="header-right">
        <template v-if="!isLoggedIn">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button type="primary" @click="$router.push('/register')">注册</el-button>
        </template>
        <template v-else>
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              {{ username }} <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="console">个人中心</el-dropdown-item>
                <el-dropdown-item v-if="isAdmin" command="admin">系统管理</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </div>
    </el-header>
    <el-main class="user-main">
      <router-view></router-view>
    </el-main>
    <el-footer class="user-footer">
      <p>&copy; 2026 API Transit Station. All rights reserved.</p>
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)
const isLoggedIn = ref(false)
const username = ref('')
const isAdmin = ref(false)

const checkLogin = () => {
  const userStr = localStorage.getItem('user')
  const token = localStorage.getItem('token')
  if (!token || !userStr) {
    isLoggedIn.value = false
    username.value = ''
    isAdmin.value = false
    return
  }
  const user = JSON.parse(userStr)
  isLoggedIn.value = true
  username.value = user.username
  isAdmin.value = user.role === 'ADMIN'
}

const handleCommand = (command: string) => {
  if (command === 'logout') {
    const { clearAuth } = require('@/utils/auth')
    clearAuth()
    isLoggedIn.value = false
    router.push('/')
  } else if (command === 'console') {
    router.push('/console')
  } else if (command === 'admin') {
    router.push('/admin')
  }
}

onMounted(() => {
  checkLogin()
  window.addEventListener('auth-changed', checkLogin)
})
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.user-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 50px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #409eff;
  margin-right: 40px;
  cursor: pointer;
}

.header-menu {
  border-bottom: none;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.user-main {
  flex: 1;
  padding: 0;
}

.user-footer {
  text-align: center;
  color: #909399;
  padding: 20px 0;
  border-top: 1px solid #dcdfe6;
}
</style>
