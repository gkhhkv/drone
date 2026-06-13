<script setup>
// 搜索筛选栏 — 序列号/型号/状态筛选 + 搜索/重置/新增按钮
const props = defineProps({
  filters: { type: Object, required: true },
})

const emit = defineEmits(['update:filters', 'search', 'reset', 'create'])

function onFieldChange(field, value) {
  emit('update:filters', { ...props.filters, [field]: value })
}

const statusOptions = [
  { label: '全部', value: '' },
  { label: 'IDLE', value: 'IDLE' },
  { label: 'CHARGING', value: 'CHARGING' },
  { label: 'IN_MAINTENANCE', value: 'IN_MAINTENANCE' },
]
</script>

<template>
  <div class="search-bar">
    <el-input
      :model-value="props.filters.serialNumber"
      placeholder="序列号"
      clearable
      style="width: 180px"
      @input="onFieldChange('serialNumber', $event)"
      @clear="onFieldChange('serialNumber', '')"
      @keyup.enter="emit('search')"
    />

    <el-input
      :model-value="props.filters.model"
      placeholder="型号"
      clearable
      style="width: 180px"
      @input="onFieldChange('model', $event)"
      @clear="onFieldChange('model', '')"
      @keyup.enter="emit('search')"
    />

    <el-select
      :model-value="props.filters.status"
      placeholder="状态"
      clearable
      style="width: 160px"
      @change="onFieldChange('status', $event || '')"
    >
      <el-option
        v-for="opt in statusOptions"
        :key="opt.value"
        :label="opt.label"
        :value="opt.value"
      />
    </el-select>

    <el-button type="primary" @click="emit('search')">搜索</el-button>
    <el-button @click="emit('reset')">重置</el-button>
    <el-button type="success" @click="emit('create')" style="margin-left: auto">新增无人机</el-button>
  </div>
</template>

<style scoped>
.search-bar {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}
</style>
