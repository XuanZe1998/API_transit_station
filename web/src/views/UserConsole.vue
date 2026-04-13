<template>
  <el-container class="console-container">
    <el-aside width="220px">
      <el-menu :default-active="activeMenu" class="console-menu" router>
        <el-menu-item index="/console/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>账户概览</span>
        </el-menu-item>
      <el-menu-item index="/console/reference">
        <el-icon><Document /></el-icon>
        <span>统一接口</span>
      </el-menu-item>
      <el-menu-item index="/console/tokens">
          <el-icon><Key /></el-icon>
          <span>令牌管理</span>
        </el-menu-item>
        <el-menu-item index="/console/logs">
          <el-icon><Tickets /></el-icon>
          <span>日志管理</span>
        </el-menu-item>
        <el-menu-item index="/console/wallet">
          <el-icon><Wallet /></el-icon>
          <span>我的钱包</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main class="console-main">
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.console-container {
  height: 100%;
}

.console-menu {
  height: 100%;
  border-right: none;
  background-color: #f5f7fa;
}

.console-main {
  padding: 30px 50px;
  background-color: #fff;
}
</style>
