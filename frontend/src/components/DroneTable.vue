<script setup>
// 无人机数据表格 — 展示列表，支持编辑/删除操作
defineProps({
  data: { type: Array, required: true },
  loading: { type: Boolean, default: false },
})

const emit = defineEmits(['edit', 'delete'])

// 状态 → el-tag 颜色类型
function statusType(status) {
  switch (status) {
    case 'IDLE': return 'success'
    case 'CHARGING': return 'warning'
    case 'IN_MAINTENANCE': return 'danger'
    default: return 'info'
  }
}

const statusLabelMap = {
  IDLE: '空闲',
  CHARGING: '充电中',
  IN_MAINTENANCE: '维护中',
}

// 电量进度条颜色
function batteryColor(percent) {
  if (percent >= 80) return '#67c23a'
  if (percent >= 30) return '#e6a23c'
  return '#f56c6c'
}

function batteryStroke(percent) {
  if (percent >= 80) return '#e1f3d8'
  if (percent >= 30) return '#faecd8'
  return '#fde2e2'
}
</script>

<template>
  <el-table :data="data" v-loading="loading" stripe empty-text="暂无数据" style="width: 100%">
    <el-table-column prop="id" label="ID" width="80" align="center" />
    <el-table-column prop="serialNumber" label="序列号" min-width="160" show-overflow-tooltip />
    <el-table-column prop="model" label="型号" min-width="140" show-overflow-tooltip />

    <!-- 电量：进度条展示 -->
    <el-table-column label="电量" width="180" align="center">
      <template #default="{ row }">
        <div class="battery-cell">
          <el-progress
            :percentage="row.batteryPercent"
            :color="batteryColor(row.batteryPercent)"
            :stroke-width="14"
            :bgcolor="batteryStroke(row.batteryPercent)"
          />
        </div>
      </template>
    </el-table-column>

    <el-table-column prop="maxFlightMinutes" label="最大飞行时长" width="130" align="center">
      <template #default="{ row }">
        {{ row.maxFlightMinutes }} 分钟
      </template>
    </el-table-column>

    <!-- 状态：彩色标签 -->
    <el-table-column label="状态" width="120" align="center">
      <template #default="{ row }">
        <el-tooltip :content="row.status" placement="top">
          <el-tag :type="statusType(row.status)" size="default">
            {{ statusLabelMap[row.status] || row.status }}
          </el-tag>
        </el-tooltip>
      </template>
    </el-table-column>

    <!-- 操作按钮 -->
    <el-table-column label="操作" width="160" align="center" fixed="right">
      <template #default="{ row }">
        <el-button type="primary" size="small" link @click="emit('edit', row)">编辑</el-button>
        <el-popconfirm
          title="确认删除该记录吗？"
          confirm-button-text="确认"
          cancel-button-text="取消"
          @confirm="emit('delete', row.id)"
        >
          <template #reference>
            <el-button type="danger" size="small" link>删除</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>
.battery-cell { padding: 4px 0; }
</style>
