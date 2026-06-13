<script setup>
// 无人机数据面板 — 列表、筛选、统计、CRUD 入口
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { listDrones, deleteDrone } from '@/api/drone'
import SearchBar from '@/components/SearchBar.vue'
import DroneTable from '@/components/DroneTable.vue'

const router = useRouter()

// 防止组件卸载后更新状态
let isMounted = true
onBeforeUnmount(() => {
  isMounted = false
})

const drones = ref([])
const loading = ref(false)
const filteredDrones = ref([])

const filters = reactive({
  serialNumber: '',
  model: '',
  status: '',
})

// 统计卡片：总数、空闲、充电、维护
const stats = computed(() => {
  const total = drones.value.length
  const idle = drones.value.filter((d) => d.status === 'IDLE').length
  const charging = drones.value.filter((d) => d.status === 'CHARGING').length
  const maintenance = drones.value.filter((d) => d.status === 'IN_MAINTENANCE').length
  return { total, idle, charging, maintenance }
})

// 前端筛选：序列号/型号模糊匹配，状态精确匹配
function applyFilters() {
  let result = drones.value
  const sn = filters.serialNumber.trim().toLowerCase()
  const model = filters.model.trim().toLowerCase()
  const status = filters.status

  if (sn) {
    result = result.filter((d) => (d.serialNumber || '').toLowerCase().includes(sn))
  }
  if (model) {
    result = result.filter((d) => d.model.toLowerCase().includes(model))
  }
  if (status) {
    result = result.filter((d) => d.status === status)
  }
  filteredDrones.value = result
}

function resetFilters() {
  filters.serialNumber = ''
  filters.model = ''
  filters.status = ''
  filteredDrones.value = [...drones.value]
}

// 加载无人机列表
async function fetchDrones() {
  loading.value = true
  try {
    const data = await listDrones()
    if (!isMounted) return
    drones.value = data
    applyFilters()
    ElMessage.success(`加载完成，共 ${data.length} 条`)
  } catch (e) {
    if (!isMounted) return
    ElMessage.error('加载失败：' + e.message)
  } finally {
    if (isMounted) loading.value = false
  }
}

function openCreate() {
  router.push('/drone/new')
}

function openEdit(drone) {
  router.push(`/drone/${drone.id}/edit`)
}

async function handleDelete(id) {
  try {
    await deleteDrone(id)
    ElMessage.success('删除成功')
    await fetchDrones()
  } catch (e) {
    ElMessage.error('删除失败：' + e.message)
  }
}

onMounted(() => {
  fetchDrones()
})
</script>

<template>
  <el-card class="header-card">
    <h1 style="text-align: center;">无人机数据面板</h1>
  </el-card>

  <!-- 统计卡片 -->
  <div class="stats-row">
    <el-card class="stat-card stat-total">
      <div class="stat-label">无人机总数</div>
      <div class="stat-value">{{ stats.total }}</div>
    </el-card>
    <el-card class="stat-card stat-idle">
      <div class="stat-label">空闲</div>
      <div class="stat-value">{{ stats.idle }}</div>
    </el-card>
    <el-card class="stat-card stat-charging">
      <div class="stat-label">充电中</div>
      <div class="stat-value">{{ stats.charging }}</div>
    </el-card>
    <el-card class="stat-card stat-maintenance">
      <div class="stat-label">维护中</div>
      <div class="stat-value">{{ stats.maintenance }}</div>
    </el-card>
  </div>

  <!-- 搜索筛选栏 -->
  <el-card class="section-card">
    <SearchBar
      :filters="filters"
      @update:filters="Object.assign(filters, $event)"
      @search="applyFilters"
      @reset="resetFilters"
      @create="openCreate"
    />
  </el-card>

  <!-- 数据表格 -->
  <el-card class="section-card">
    <DroneTable
      :data="filteredDrones"
      :loading="loading"
      @edit="openEdit"
      @delete="handleDelete"
    />
  </el-card>
</template>

<style scoped>
.header-card { margin-bottom: 16px; }
.header-card h1 { font-size: 20px; font-weight: 600; }
.section-card { margin-bottom: 16px; }

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card {
  text-align: center;
  padding: 8px 0;
  border-left: 4px solid;
}

.stat-total { border-left-color: #409eff; }
.stat-idle { border-left-color: #67c23a; }
.stat-charging { border-left-color: #e6a23c; }
.stat-maintenance { border-left-color: #f56c6c; }

.stat-label { font-size: 14px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 28px; font-weight: 700; color: #303133; }
</style>
