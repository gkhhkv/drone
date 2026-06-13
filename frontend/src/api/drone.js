/**
 * 无人机 CRUD API 接口层
 *
 * 本模块封装了所有无人机相关的后端接口调用，每个函数对应一个 REST API：
 * - listDrones()    → GET    /api/drones        查询全部无人机

 * - createDrone(d)  → POST   /api/drones         新增无人机
 * - updateDrone(id) → PUT    /api/drones/{id}    更新无人机
 * - deleteDrone(id) → DELETE /api/drones/{id}    删除无人机
 *
 * 所有请求共享 api/index.js 中的 http 实例，自动享受响应拦截和错误处理。
 */
import http from './index'

/**
 * 查询无人机列表
 * @returns {Promise<Array>} 无人机对象数组
 */
export function listDrones() {
  return http.get('/api/drones').then((res) => res.data)
}

/**
 * 根据 ID 查询单个无人机
 * @param {number} id - 无人机主键 ID
 * @returns {Promise<Object>} 无人机对象
 */
export function getDrone(id) {
  return http.get(`/api/drones/${id}`).then((res) => res.data)
}

/**
 * 新增无人机，未填字段由后端自动补齐
 * @param {Object} data - 无人机数据（所有字段可选）
 * @param {string} [data.serialNumber] - 序列号
 * @param {string} [data.model] - 型号
 * @param {number} [data.batteryPercent] - 电量百分比 0-100
 * @param {number} [data.maxFlightMinutes] - 最大飞行时长（分钟）
 * @param {string} [data.status] - 状态：IDLE/CHARGING/IN_MAINTENANCE
 * @returns {Promise<Object>} 创建后的无人机对象（含 ID）
 */
export function createDrone(data) {
  return http.post('/api/drones', data).then((res) => res.data)
}

/**
 * 更新无人机
 * @param {number} id - 要更新的无人机 ID
 * @param {Object} data - 需要更新的字段（仅传变更字段）
 * @returns {Promise<Object>} 更新后的无人机对象
 */
export function updateDrone(id, data) {
  return http.put(`/api/drones/${id}`, data).then((res) => res.data)
}

/**
 * 删除无人机（后端会自动重排后续 ID）
 * @param {number} id - 要删除的无人机 ID
 * @returns {Promise<void>}
 */
export function deleteDrone(id) {
  return http.delete(`/api/drones/${id}`)
}
