<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <el-menu
        :default-active="activeRoute"
        class="el-menu-vertical"
        router
      >
        <div class="logo" @click="$router.push('/')">
          API Transit Admin
        </div>
        <el-menu-item index="/admin/channels">
          <el-icon><Connection /></el-icon>
          <span>渠道管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/tokens">
          <el-icon><Key /></el-icon>
          <span>令牌管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/mappings">
          <el-icon><Switch /></el-icon>
          <span>模型映射</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-content">
          <h2>系统管理后台</h2>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeRoute = computed(() => route.path)
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-menu-vertical {
  height: 100%;
  border-right: none;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 1.2rem;
  font-weight: bold;
  color: #409eff;
  border-bottom: 1px solid #dcdfe6;
  cursor: pointer;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
  padding: 0 20px;
}

.header-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.el-main {
  background-color: #f5f7fa;
}
</style>
