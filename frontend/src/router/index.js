import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'DroneList',
    component: () => import('@/views/DroneList.vue'),
  },
  {
    path: '/drone/new',
    name: 'DroneCreate',
    component: () => import('@/views/DroneEdit.vue'),
  },
  {
    path: '/drone/:id/edit',
    name: 'DroneEdit',
    component: () => import('@/views/DroneEdit.vue'),
    props: true,
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

export default router
