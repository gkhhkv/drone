<script setup>
// 无人机表单页面 — 新增/编辑，通过路由参数区分模式
import { reactive, ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createDrone, updateDrone, getDrone } from '@/api/drone'

const router = useRouter()
const route = useRoute()

const isEdit = computed(() => !!route.params.id)
const title = computed(() => (isEdit.value ? '编辑无人机' : '新增无人机'))

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  serialNumber: '',
  model: '',
  batteryPercent: undefined,
  maxFlightMinutes: undefined,
  status: '',
})

const rules = {
  batteryPercent: [
    { type: 'number', min: 0, max: 100, message: '电量范围 0-100', trigger: 'blur' },
  ],
  maxFlightMinutes: [
    { type: 'number', min: 1, max: 300, message: '飞行时长范围 1-300', trigger: 'blur' },
  ],
}

// 编辑模式下加载已有数据
onMounted(async () => {
  if (isEdit.value) {
    loading.value = true
    try {
      const drone = await getDrone(Number(route.params.id))
      form.serialNumber = drone.serialNumber
      form.model = drone.model
      form.batteryPercent = drone.batteryPercent
      form.maxFlightMinutes = drone.maxFlightMinutes
      form.status = drone.status
    } catch (e) {
      ElMessage.error('加载失败：' + e.message)
      router.push('/')
    } finally {
      loading.value = false
    }
  }
})

// 提交表单：新增或编辑
async function submit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  const payload = {
    serialNumber: form.serialNumber || undefined,
    model: form.model || undefined,
    batteryPercent: form.batteryPercent,
    maxFlightMinutes: form.maxFlightMinutes,
    status: form.status || undefined,
  }

  try {
    if (isEdit.value) {
      await updateDrone(Number(route.params.id), payload)
      ElMessage.success('更新成功')
    } else {
      await createDrone(payload)
      ElMessage.success('创建成功')
    }
    router.push('/')
  } catch (e) {
    ElMessage.error((isEdit.value ? '更新' : '创建') + '失败：' + e.message)
  }
}

function goBack() {
  router.push('/')
}
</script>

<template>
  <el-card class="form-page">
    <template #header>
      <el-button :icon="'ArrowLeft'" @click="goBack">返回</el-button>
      <span style="margin-left: 12px; font-weight: 600">{{ title }}</span>
    </template>

    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="110px"
      style="max-width: 500px"
      v-loading="loading"
    >
      <el-form-item label="序列号">
        <el-input v-model="form.serialNumber" maxlength="64" placeholder="留空将自动生成" />
      </el-form-item>

      <el-form-item label="型号">
        <el-input v-model="form.model" maxlength="64" placeholder="留空将自动生成" />
      </el-form-item>

      <el-form-item label="电量" prop="batteryPercent">
        <el-input-number
          v-model="form.batteryPercent" :min="0" :max="100"
          placeholder="留空将自动生成" style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="最大飞行时长" prop="maxFlightMinutes">
        <el-input-number
          v-model="form.maxFlightMinutes" :min="1" :max="300"
          placeholder="留空将自动生成" style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="留空将自动生成" clearable style="width: 100%">
          <el-option label="IDLE" value="IDLE" />
          <el-option label="CHARGING" value="CHARGING" />
          <el-option label="IN_MAINTENANCE" value="IN_MAINTENANCE" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-alert type="info" :closable="false" show-icon title="未填写的字段系统将自动生成默认值" />
      </el-form-item>

      <el-form-item>
        <el-button @click="goBack">取消</el-button>
        <el-button type="primary" @click="submit">确认</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<style scoped>
.form-page { max-width: 640px; margin: 24px auto; }
</style>
