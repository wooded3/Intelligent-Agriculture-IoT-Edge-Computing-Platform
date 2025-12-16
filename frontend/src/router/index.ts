import { createRouter, createWebHistory } from 'vue-router';
import Layout from '../components/Layout.vue';

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '仪表板', icon: 'Odometer' }
      },
      {
        path: 'devices',
        name: 'DeviceManagement',
        component: () => import('../views/DeviceManagement.vue'),
        meta: { title: '设备管理', icon: 'Monitor' }
      },
      {
        path: 'data',
        name: 'DataMonitoring',
        component: () => import('../views/DataMonitoring.vue'),
        meta: { title: '数据监控', icon: 'DataAnalysis' }
      },
      {
        path: 'alerts',
        name: 'AlertCenter',
        component: () => import('../views/AlertCenter.vue'),
        meta: { title: '告警中心', icon: 'Bell' }
      },
      {
        path: 'automation',
        name: 'AutomationControl',
        component: () => import('../views/AutomationControl.vue'),
        meta: { title: '自动化控制', icon: 'Setting' }
      },
      {
        path: 'system',
        name: 'SystemManagement',
        component: () => import('../views/SystemManagement.vue'),
        meta: { title: '系统管理', icon: 'Tools' }
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;

