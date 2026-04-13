<template>
  <div class="home-container">
    <div class="hero">
      <h1>API 大模型接口中转站</h1>
      <p>一个接口调用多种模型，为您提供高效、稳定的 AI 服务体验。</p>
      <div class="hero-actions">
        <el-button type="primary" size="large" @click="$router.push('/market')">浏览模型</el-button>
        <el-button size="large" @click="handleStart">立即开始</el-button>
      </div>
    </div>
    <div class="features">
      <div class="feature-item">
        <el-icon :size="40" color="#409eff"><Connection /></el-icon>
        <h3>统一接口</h3>
        <p>支持 OpenAI 标准协议，无缝接入各类主流大模型。</p>
      </div>
      <div class="feature-item">
        <el-icon :size="40" color="#67c23a"><Monitor /></el-icon>
        <h3>模型广场</h3>
        <p>提供 GPT-4, DeepSeek, Claude 等国内外大模型，供您按需选择。</p>
      </div>
      <div class="feature-item">
        <el-icon :size="40" color="#e6a23c"><Cpu /></el-icon>
        <h3>控制台管理</h3>
        <p>实时查看使用额度、模型消耗、日志统计，管理您的个人令牌。</p>
      </div>
    </div>
    <div class="about">
      <h2>关于我们</h2>
      <p>本平台旨在为开发者提供最便捷的大模型调用方案，通过统一的接口标准和智能路由，降低使用成本，提高开发效率。</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'
const router = useRouter()
const handleStart = () => {
  const token = getToken()
  if (!token) {
    ElMessage.info('请先登录')
    router.push('/login')
  } else {
    router.push('/market')
  }
}
</script>

<style scoped>
.home-container {
  padding: 50px 20px;
}

.hero {
  text-align: center;
  margin-bottom: 80px;
}

.hero h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  color: #303133;
}

.hero p {
  font-size: 1.2rem;
  color: #606266;
  margin-bottom: 40px;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.features {
  display: flex;
  justify-content: center;
  gap: 50px;
  flex-wrap: wrap;
  margin-bottom: 80px;
}

.feature-item {
  width: 250px;
  text-align: center;
  padding: 30px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.feature-item:hover {
  transform: translateY(-10px);
}

.feature-item h3 {
  margin: 15px 0;
  color: #303133;
}

.feature-item p {
  color: #909399;
  font-size: 0.9rem;
}

.about {
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
  padding: 50px;
  background: #f5f7fa;
  border-radius: 12px;
}

.about h2 {
  margin-bottom: 20px;
  color: #303133;
}

.about p {
  line-height: 1.6;
  color: #606266;
}
</style>
