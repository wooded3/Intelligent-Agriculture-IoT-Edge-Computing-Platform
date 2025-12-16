<template>
  <div class="data-monitoring">
    <div class="page-header">
      <h2>数据监控</h2>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        模拟数据上报
      </el-button>
    </div>

    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="设备ID">
          <el-input v-model="searchForm.deviceId" placeholder="请输入设备ID" clearable />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="searchForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="searchForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="dataList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="数据ID" width="180" />
        <el-table-column prop="deviceId" label="设备ID" />
        <el-table-column prop="value" label="数值" width="120">
          <template #default="{ row }">
            {{ row.value }} {{ row.unit || '' }}
          </template>
        </el-table-column>
        <el-table-column prop="timestamp" label="采集时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.timestamp) }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 模拟数据上报对话框 -->
    <el-dialog v-model="showAddDialog" title="模拟数据上报" width="400px">
      <el-form :model="dataForm" label-width="100px">
        <el-form-item label="设备ID" required>
          <el-input v-model="dataForm.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="数值" required>
          <el-input-number v-model="dataForm.value" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="dataForm.unit" placeholder="如: °C, %" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitData">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { dataApi } from '../api';

const dataList = ref<any[]>([]);
const loading = ref(false);
const showAddDialog = ref(false);

const searchForm = reactive({
  deviceId: '',
  startTime: '',
  endTime: ''
});

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
});

const dataForm = reactive({
  deviceId: '',
  value: 0,
  unit: '°C'
});

const formatTime = (time?: string) => {
  if (!time) return '-';
  return new Date(time).toLocaleString('zh-CN');
};

const loadData = async () => {
  loading.value = true;
  try {
    const res = await dataApi.list({
      page: pagination.page,
      pageSize: pagination.pageSize,
      deviceId: searchForm.deviceId || undefined,
      startTime: searchForm.startTime || undefined,
      endTime: searchForm.endTime || undefined
    });
    dataList.value = res.data.rows || [];
    pagination.total = res.data.total || 0;
  } catch (err: any) {
    ElMessage.error(err.message || '加载数据失败');
  } finally {
    loading.value = false;
  }
};

const resetSearch = () => {
  searchForm.deviceId = '';
  searchForm.startTime = '';
  searchForm.endTime = '';
  pagination.page = 1;
  loadData();
};

const submitData = async () => {
  if (!dataForm.deviceId) {
    ElMessage.warning('请输入设备ID');
    return;
  }

  try {
    await dataApi.create({
      deviceId: dataForm.deviceId,
      value: dataForm.value,
      unit: dataForm.unit,
      timestamp: new Date().toISOString()
    });
    ElMessage.success('数据上报成功');
    showAddDialog.value = false;
    dataForm.deviceId = '';
    dataForm.value = 0;
    dataForm.unit = '°C';
    loadData();
  } catch (err: any) {
    ElMessage.error(err.message || '数据上报失败');
  }
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.data-monitoring {
  height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.search-form {
  margin-bottom: 20px;
}
</style>

