/**
 * Axios HTTP 客户端封装
 *
 * 本模块基于 axios 创建统一的 HTTP 请求实例，提供：
 * 1. 统一的 baseURL 和超时配置
 * 2. 响应拦截器：自动解包后端返回的 ApiResponse 结构
 *    - 后端返回 { success, message, data } 格式
 *    - 拦截器在 success=true 时自动提取 data 字段
 *    - success=false 时统一抛出错误
 * 3. 统一的错误处理：网络异常、业务异常都转化为 Error 对象
 *
 * 使用示例：
 *   import http from '@/api/index'
 *   const data = await http.get('/api/drones').then(res => res.data)
 */
import axios from 'axios'

// 创建 axios 实例，配置基础参数
const http = axios.create({
  baseURL: '/', // 开发环境下 Vite proxy 会转发 /api 请求到后端 localhost:8080
  timeout: 10000, // 请求超时时间 10 秒
  headers: { 'Content-Type': 'application/json' },
})

/**
 * 响应拦截器 — 成功回调
 * 从后端统一响应体 { success, message, data } 中提取实际数据
 */
http.interceptors.response.use(
  (response) => {
    const body = response.data
    // 业务成功：自动解包，只返回 data 部分
    if (body.success) {
      response.data = body.data
      return response
    }
    // 业务失败：抛出错误供调用方 catch 处理
    return Promise.reject(new Error(body.message || '请求失败'))
  },

  /**
   * 响应拦截器 — 失败回调
   * 处理网络异常、HTTP 错误（4xx/5xx）等
   */
  (error) => {
    const message = error.response?.data?.message || error.message || '网络错误'
    return Promise.reject(new Error(message))
  },
)

export default http
