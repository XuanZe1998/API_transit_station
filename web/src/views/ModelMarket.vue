<template>
  <div class="market-container">
    <div class="market-header">
      <h2>模型广场</h2>
      <p>探索各种强大的人工智能大语言模型。</p>
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索模型名称..."
          clearable
          prefix-icon="Search"
          size="large"
          @keyup.enter.native="fetchModels"
          @clear="handleSearch"
          @input="handleSearch"
        />
      </div>
      <div class="filter-bar">
        <el-select v-model="typeFilter" placeholder="厂商类型" clearable style="width: 180px" @change="onFilterChange">
          <el-option label="OpenAI" value="openai" />
          <el-option label="DeepSeek" value="deepseek" />
          <el-option label="Anthropic" value="anthropic" />
          <el-option label="Google" value="google" />
        </el-select>
        <el-select v-model="sortMode" placeholder="排序方式" style="width: 160px" @change="onFilterChange">
          <el-option label="按热度" value="hot" />
          <el-option label="最新调用" value="recent" />
          <el-option label="名称" value="name" />
        </el-select>
      </div>
    </div>
    <div class="model-grid" v-loading="loading">
      <el-card v-for="model in items" :key="model.publicName + '_' + model.type" class="model-card">
        <div class="card-header">
          <h3>{{ model.publicName }}</h3>
          <el-tag :type="getTagType(model.type)">{{ model.type }}</el-tag>
        </div>
        <p class="model-desc">兼容 OpenAI 接口，支持多种自然语言任务。</p>
        <div class="card-footer">
          <div class="price">
            <el-icon><Money /></el-icon> 免费
          </div>
          <el-button type="primary" size="small" @click="$router.push('/console')">立即调用</el-button>
        </div>
      </el-card>
      <el-empty v-if="!loading && items.length === 0" description="暂无模型" />
    </div>
    <div class="pager">
      <el-pagination
        background
        layout="prev, pager, next, sizes, total"
        :current-page="page"
        :page-size="size"
        :page-sizes="[8, 12, 16, 24, 36]"
        :total="total"
        @current-change="onPageChange"
        @size-change="onSizeChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import http from '@/utils/http'

const loading = ref(false)
const searchQuery = ref('')
const items = ref<{ publicName: string; type: string }[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(12)
const typeFilter = ref<string | undefined>(undefined)
const sortMode = ref<'hot' | 'recent' | 'name'>('name')

const onPageChange = (p: number) => {
  page.value = p
  fetchModels()
}
const onSizeChange = (s: number) => {
  size.value = s
  page.value = 1
  fetchModels()
}

const getTagType = (type: string) => {
  switch (type) {
    case 'openai': return ''
    case 'deepseek': return 'success'
    case 'anthropic': return 'warning'
    case 'google': return 'danger'
    default: return 'info'
  }
}

const fetchModels = async () => {
  loading.value = true
  try {
    const res = await http.get('/api/public/models', {
      params: { page: page.value, size: size.value, query: searchQuery.value || undefined, type: typeFilter.value, sort: sortMode.value }
    })
    items.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (e) {
    items.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchModels()
})

let searchTimer: number | undefined
const handleSearch = () => {
  if (searchTimer) window.clearTimeout(searchTimer)
  searchTimer = window.setTimeout(() => {
    page.value = 1
    fetchModels()
  }, 300)
}

const onFilterChange = () => {
  page.value = 1
  fetchModels()
}
</script>

<style scoped>
.market-container {
  padding: 50px 100px;
}

.market-header {
  text-align: center;
  margin-bottom: 50px;
}

.market-header h2 {
  font-size: 2.5rem;
  margin-bottom: 15px;
  color: #303133;
}

.market-header p {
  font-size: 1.1rem;
  color: #606266;
  margin-bottom: 30px;
}

.search-bar {
  max-width: 600px;
  margin: 0 auto;
}

.filter-bar {
  margin: 16px auto 0;
  display: flex;
  gap: 12px;
  justify-content: center;
}

.model-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
}

.model-card {
  border-radius: 12px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.model-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.card-header h3 {
  margin: 0;
  color: #303133;
}

.model-desc {
  color: #606266;
  font-size: 0.9rem;
  line-height: 1.5;
  margin-bottom: 20px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #ebeef5;
  padding-top: 15px;
}

.price {
  font-size: 0.9rem;
  color: #67c23a;
  display: flex;
  align-items: center;
  gap: 5px;
}

.pager {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
