<template>
  <div class="device-management">
    <div class="page-header">
      <h2>设备管理</h2>
      <el-button type="primary" @click="showDialog = true">
        <el-icon><Plus /></el-icon>
        添加设备
      </el-button>
    </div>

    <el-card>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="教室ID">
          <el-input v-model="searchForm.classroomId" placeholder="请输入教室ID" clearable />
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="searchForm.type" placeholder="请选择" clearable>
            <el-option label="传感器" value="SENSOR" />
            <el-option label="执行器" value="ACTUATOR" />
            <el-option label="网关" value="GATEWAY" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadDevices">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="devices" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="设备ID" width="180" />
        <el-table-column prop="name" label="设备名称" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status || '未知' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="classroomId" label="教室ID" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="editDevice(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteDevice(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadDevices"
        @current-change="loadDevices"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 添加/编辑设备对话框 -->
    <el-dialog
      v-model="showDialog"
      :title="editingDevice ? '编辑设备' : '添加设备'"
      width="500px"
    >
      <el-form :model="deviceForm" label-width="100px">
        <el-form-item label="设备名称" required>
          <el-input v-model="deviceForm.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备类型" required>
          <el-select v-model="deviceForm.type" placeholder="请选择设备类型" style="width: 100%">
            <el-option label="传感器" value="SENSOR" />
            <el-option label="执行器" value="ACTUATOR" />
            <el-option label="网关" value="GATEWAY" />
          </el-select>
        </el-form-item>
        <el-form-item label="教室ID" required>
          <el-input v-model="deviceForm.classroomId" placeholder="请输入教室ID" />
        </el-form-item>
        <el-form-item label="设备状态">
          <el-select v-model="deviceForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="在线" value="ONLINE" />
            <el-option label="离线" value="OFFLINE" />
            <el-option label="错误" value="ERROR" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="saveDevice">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { deviceApi } from '../api';

const devices = ref<any[]>([]);
const loading = ref(false);
const showDialog = ref(false);
const editingDevice = ref<any>(null);

const searchForm = reactive({
  classroomId: '',
  type: ''
});

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
});

const deviceForm = reactive({
  name: '',
  type: '',
  classroomId: '',
  status: 'OFFLINE'
});

const getStatusType = (status?: string) => {
  if (status === 'ONLINE') return 'success';
  if (status === 'OFFLINE') return 'info';
  if (status === 'ERROR') return 'danger';
  return '';
};

const formatTime = (time?: string) => {
  if (!time) return '-';
  return new Date(time).toLocaleString('zh-CN');
};

const loadDevices = async () => {
  loading.value = true;
  try {
    const res = await deviceApi.list({
      page: pagination.page,
      pageSize: pagination.pageSize,
      classroomId: searchForm.classroomId || undefined,
      type: searchForm.type || undefined
    });
    devices.value = res.data.rows || [];
    pagination.total = res.data.total || 0;
  } catch (err: any) {
    ElMessage.error(err.message || '加载设备列表失败');
  } finally {
    loading.value = false;
  }
};

const resetSearch = () => {
  searchForm.classroomId = '';
  searchForm.type = '';
  pagination.page = 1;
  loadDevices();
};

const editDevice = (device: any) => {
  editingDevice.value = device;
  deviceForm.name = device.name;
  deviceForm.type = device.type;
  deviceForm.classroomId = device.classroomId;
  deviceForm.status = device.status || 'OFFLINE';
  showDialog.value = true;
};

const saveDevice = async () => {
  if (!deviceForm.name || !deviceForm.type || !deviceForm.classroomId) {
    ElMessage.warning('请填写完整信息');
    return;
  }

  try {
    if (editingDevice.value) {
      await deviceApi.update(editingDevice.value.id, deviceForm);
      ElMessage.success('设备更新成功');
    } else {
      await deviceApi.create(deviceForm);
      ElMessage.success('设备添加成功');
    }
    showDialog.value = false;
    resetForm();
    loadDevices();
  } catch (err: any) {
    ElMessage.error(err.message || '操作失败');
  }
};

const deleteDevice = async (id: string) => {
  try {
    await ElMessageBox.confirm('确定要删除该设备吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });
    await deviceApi.delete(id);
    ElMessage.success('删除成功');
    loadDevices();
  } catch (err: any) {
    if (err !== 'cancel') {
      ElMessage.error(err.message || '删除失败');
    }
  }
};

const resetForm = () => {
  editingDevice.value = null;
  deviceForm.name = '';
  deviceForm.type = '';
  deviceForm.classroomId = '';
  deviceForm.status = 'OFFLINE';
};

onMounted(() => {
  loadDevices();
});
</script>

<style scoped>
.device-management {
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

