/**
 * 无人机信息管理系统 — 前端应用入口
 *
 * 本文件是 Vue 3 应用的启动入口，负责：
 * 1. 创建 Vue 应用实例
 * 2. 全局注册 ElementPlus UI 组件库及样式
 * 3. 挂载应用到 DOM 节点 #app
 *
 * 依赖关系：main.js → App.vue → DroneList.vue → (SearchBar + DroneTable + DroneForm)
 */
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css' // ElementPlus 全局样式
import router from './router'
import App from './App.vue'
import './style.css' // 全局自定义样式

// 创建 Vue 应用实例
const app = createApp(App)

// 全局安装 ElementPlus 组件库，此后所有组件可直接使用 <el-xxx> 标签
app.use(ElementPlus)
app.use(router)

// 挂载到 index.html 中的 #app 容器
app.mount('#app')
