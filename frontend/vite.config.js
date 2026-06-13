/**
 * Vite 构建配置文件
 *
 * 配置项说明：
 * - plugins: 启用 Vue 3 SFC 编译插件
 * - resolve.alias: 设置 @ 路径别名指向 src 目录
 * - server: 开发服务器配置
 *   - host: 0.0.0.0 允许局域网访问
 *   - port: 5173（Vite 默认端口）
 *   - proxy: 将 /api 请求代理到后端 Spring Boot 8080 端口，解决跨域问题
 * - build: 构建输出到 dist 目录
 *
 * 开发时前端(5173)和后端(8080)同时运行，/api 请求自动转发到后端。
 * 生产构建后，dist 目录内容放入后端 static 目录，由 Spring Boot 直接 serve。
 */
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  // 注册 Vue SFC 编译插件
  plugins: [vue(), vueDevTools()],

  // 路径解析配置
  resolve: {
    alias: {
      // @ 别名 → src 目录，方便导入时写 import X from '@/components/X.vue'
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },

  // 开发服务器配置
  server: {
    host: '0.0.0.0', // 监听所有网络接口
    port: 5173, // Vite 开发服务器端口
    proxy: {
      // API 代理：前端 /api/* 请求转发到后端 Spring Boot
      '/api': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true, // 修改请求头中的 Origin 为目标地址
      },
    },
  },

  // 生产构建配置
  build: {
    outDir: 'dist', // 构建输出目录
    assetsDir: 'assets', // 静态资源子目录
  },
})
