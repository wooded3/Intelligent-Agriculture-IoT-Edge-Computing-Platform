<template>
  <div class="system-management">
    <h2>系统管理</h2>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>教室列表</span>
          </template>
          <el-table :data="classrooms" v-loading="loading" style="width: 100%">
            <el-table-column prop="id" label="教室ID" />
            <el-table-column prop="name" label="教室名称" />
            <el-table-column prop="location" label="位置" />
            <el-table-column prop="deviceCount" label="设备数量" width="100">
              <template #default="{ row }">
                <el-tag>{{ row.deviceCount }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>系统信息</span>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="系统名称">智慧教室物联网管理系统</el-descriptions-item>
            <el-descriptions-item label="版本号">v2.0</el-descriptions-item>
            <el-descriptions-item label="后端框架">Spring Boot 3.5.8</el-descriptions-item>
            <el-descriptions-item label="前端框架">Vue 3 + TypeScript</el-descriptions-item>
            <el-descriptions-item label="数据存储">内存存储（演示模式）</el-descriptions-item>
            <el-descriptions-item label="运行状态">
              <el-tag type="success">正常运行</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { systemApi } from '../api';

const classrooms = ref<any[]>([]);
const loading = ref(false);

const loadClassrooms = async () => {
  loading.value = true;
  try {
    const res = await systemApi.getClassrooms();
    classrooms.value = res.data || [];
  } catch (err: any) {
    ElMessage.error(err.message || '加载教室列表失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadClassrooms();
});
</script>

<style scoped>
.system-management h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
}
</style>

