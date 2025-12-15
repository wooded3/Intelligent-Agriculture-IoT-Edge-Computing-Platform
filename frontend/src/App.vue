<template>
  <div class="app">
    <header class="app-header">
      <h1>IAIoTECP 智慧教室物联网平台</h1>
      <p>前后端已联通，可直接调用后端示例接口</p>
    </header>

    <main class="app-main">
      <section class="card">
        <h2>设备列表</h2>
        <p v-if="loading.devices">加载中...</p>
        <p v-else-if="error.devices" class="error">{{ error.devices }}</p>
        <ul v-else>
          <li v-if="devices.length === 0">暂无设备，可点击“初始化示例设备”快速添加</li>
          <li v-for="d in devices" :key="d.id">
            {{ d.name }} ({{ d.type }}) - {{ d.status || '未知' }} / 教室: {{ d.classroomId }}
          </li>
        </ul>
        <button @click="initSampleDevices" :disabled="loading.devices">初始化示例设备</button>
      </section>

      <section class="card">
        <h2>当前告警</h2>
        <p v-if="loading.alerts">加载中...</p>
        <p v-else-if="error.alerts" class="error">{{ error.alerts }}</p>
        <ul v-else>
          <li v-if="alerts.length === 0">暂无告警</li>
          <li v-for="a in alerts" :key="a.id">
            {{ a.ruleName }} - {{ a.deviceName }} : {{ a.value }} (阈值 {{ a.threshold }})
          </li>
        </ul>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';

interface Device {
  id: string;
  name: string;
  type: string;
  status?: string;
  classroomId: string;
}

interface ActiveAlert {
  id: string;
  ruleName: string;
  deviceName: string;
  value: number;
  threshold: number;
}

const devices = ref<Device[]>([]);
const alerts = ref<ActiveAlert[]>([]);
const loading = reactive({ devices: false, alerts: false });
const error = reactive<{ devices?: string; alerts?: string }>({});

const fetchDevices = async () => {
  loading.devices = true;
  error.devices = undefined;
  try {
    const res = await fetch('/api/devices');
    const body = await res.json();
    if (body.code === 1) {
      devices.value = body.data.rows ?? [];
    } else {
      error.devices = body.msg || '加载设备失败';
    }
  } catch (err) {
    error.devices = (err as Error).message;
  } finally {
    loading.devices = false;
  }
};

const fetchAlerts = async () => {
  loading.alerts = true;
  error.alerts = undefined;
  try {
    const res = await fetch('/api/alerts/current');
    const body = await res.json();
    if (body.code === 1) {
      alerts.value = body.data ?? [];
    } else {
      error.alerts = body.msg || '加载告警失败';
    }
  } catch (err) {
    error.alerts = (err as Error).message;
  } finally {
    loading.alerts = false;
  }
};

const initSampleDevices = async () => {
  loading.devices = true;
  error.devices = undefined;
  try {
    const payloads = [
      { name: '温度传感器-101', type: 'SENSOR', classroomId: 'classroom_101' },
      { name: '空调-101', type: 'ACTUATOR', classroomId: 'classroom_101' }
    ];
    for (const p of payloads) {
      const res = await fetch('/api/devices', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(p)
      });

      if (!res.ok) {
        throw new Error(`创建设备失败，HTTP 状态码：${res.status}`);
      }

      const body = await res.json();
      if (body.code !== 1) {
        throw new Error(body.msg || '创建设备失败（后端返回错误）');
      }
    }
    await fetchDevices();
  } catch (err) {
    error.devices = (err as Error).message;
  } finally {
    loading.devices = false;
  }
};

onMounted(() => {
  fetchDevices();
  fetchAlerts();
});
</script>
