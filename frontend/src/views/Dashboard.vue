<template>
  <div class="dashboard">
    <h2>仪表板</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card" @click="$router.push('/devices')">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409eff;">
              <el-icon size="32"><Monitor /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.total || 0 }}</div>
              <div class="stat-label">设备总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" @click="$router.push('/devices')">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67c23a;">
              <el-icon size="32"><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.online || 0 }}</div>
              <div class="stat-label">在线设备</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" @click="$router.push('/alerts')">
          <div class="stat-content">
            <div class="stat-icon" style="background: #e6a23c;">
              <el-icon size="32"><Bell /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.alertCount || 0 }}</div>
              <div class="stat-label">活跃告警</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" @click="$router.push('/devices')">
          <div class="stat-content">
            <div class="stat-icon" style="background: #f56c6c;">
              <el-icon size="32"><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.error || 0 }}</div>
              <div class="stat-label">异常设备</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表和数据 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>设备状态分布</span>
          </template>
          <v-chart class="chart" :option="deviceStatusChart" v-loading="loading" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>设备类型分布</span>
          </template>
          <v-chart class="chart" :option="deviceTypeChart" v-loading="loading" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>设备列表</span>
              <el-button type="primary" size="small" @click="$router.push('/devices')">查看更多</el-button>
            </div>
          </template>
          <el-table :data="devices.slice(0, 5)" style="width: 100%">
            <el-table-column prop="name" label="设备名称" />
            <el-table-column prop="type" label="类型" />
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ row.status || '未知' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="classroomId" label="教室" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>当前告警</span>
              <el-button type="primary" size="small" @click="$router.push('/alerts')">查看更多</el-button>
            </div>
          </template>
          <el-table :data="alerts.slice(0, 5)" style="width: 100%">
            <el-table-column prop="ruleName" label="规则名称" />
            <el-table-column prop="deviceName" label="设备" />
            <el-table-column prop="value" label="触发值" />
            <el-table-column prop="threshold" label="阈值" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { PieChart, BarChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent
} from 'echarts/components';
import VChart from 'vue-echarts';
import { Monitor, CircleCheck, Bell, Warning } from '@element-plus/icons-vue';
import { deviceApi, alertApi } from '../api';

use([
  CanvasRenderer,
  PieChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
]);

const devices = ref<any[]>([]);
const alerts = ref<any[]>([]);
const loading = ref(false);
const stats = ref({
  total: 0,
  online: 0,
  offline: 0,
  error: 0,
  alertCount: 0
});

const deviceStatusChart = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '设备状态',
      type: 'pie',
      radius: '50%',
      data: [
        { value: stats.value.online, name: '在线' },
        { value: stats.value.offline, name: '离线' },
        { value: stats.value.error, name: '异常' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}));

const deviceTypeChart = computed(() => {
  const typeCount: Record<string, number> = {};
  devices.value.forEach(d => {
    typeCount[d.type] = (typeCount[d.type] || 0) + 1;
  });
  
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: Object.keys(typeCount)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '设备数量',
        type: 'bar',
        data: Object.values(typeCount),
        itemStyle: {
          color: '#409eff'
        }
      }
    ]
  };
});

const getStatusType = (status?: string) => {
  if (status === 'ONLINE') return 'success';
  if (status === 'OFFLINE') return 'info';
  if (status === 'ERROR') return 'danger';
  return '';
};

const loadData = async () => {
  loading.value = true;
  try {
    const [deviceRes, alertRes, statsRes] = await Promise.all([
      deviceApi.list(),
      alertApi.getCurrent(),
      deviceApi.getStats()
    ]);
    
    devices.value = deviceRes.data.rows || [];
    alerts.value = alertRes.data || [];
    
    if (statsRes.data) {
      stats.value.total = statsRes.data.total || 0;
      stats.value.online = statsRes.data.online || 0;
      stats.value.offline = statsRes.data.offline || 0;
      stats.value.error = statsRes.data.error || 0;
    }
    stats.value.alertCount = alerts.value.length;
  } catch (err) {
    console.error('加载数据失败:', err);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadData();
  // 每30秒刷新一次数据
  setInterval(loadData, 30000);
});
</script>

<style scoped>
.dashboard h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.chart {
  height: 300px;
  width: 100%;
}
</style>
