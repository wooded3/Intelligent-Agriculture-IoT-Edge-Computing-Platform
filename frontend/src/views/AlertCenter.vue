<template>
  <div class="alert-center">
    <div class="page-header">
      <h2>告警中心</h2>
      <el-button type="primary" @click="showRuleDialog = true">
        <el-icon><Plus /></el-icon>
        创建告警规则
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>当前活跃告警</span>
          </template>
          <el-table :data="activeAlerts" v-loading="loadingAlerts" style="width: 100%">
            <el-table-column prop="ruleName" label="规则名称" />
            <el-table-column prop="deviceName" label="设备" />
            <el-table-column prop="value" label="触发值" width="100" />
            <el-table-column prop="threshold" label="阈值" width="100" />
            <el-table-column prop="timestamp" label="触发时间" width="180">
              <template #default="{ row }">
                {{ formatTime(row.timestamp) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button type="danger" size="small" @click="resolveAlert(row.id)">处理</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>告警规则</span>
          </template>
          <el-table :data="rules" v-loading="loadingRules" style="width: 100%">
            <el-table-column prop="name" label="规则名称" />
            <el-table-column prop="condition" label="条件" width="120">
              <template #default="{ row }">
                <el-tag>{{ getConditionText(row.condition) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="threshold" label="阈值" width="100" />
            <el-table-column prop="enabled" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'info'">
                  {{ row.enabled ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180">
              <template #default="{ row }">
                {{ formatTime(row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 创建告警规则对话框 -->
    <el-dialog v-model="showRuleDialog" title="创建告警规则" width="500px">
      <el-form :model="ruleForm" label-width="100px">
        <el-form-item label="规则名称" required>
          <el-input v-model="ruleForm.name" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="条件" required>
          <el-select v-model="ruleForm.condition" placeholder="请选择条件" style="width: 100%">
            <el-option label="大于" value="GREATER_THAN" />
            <el-option label="小于" value="LESS_THAN" />
            <el-option label="等于" value="EQUAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="阈值" required>
          <el-input-number v-model="ruleForm.threshold" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="设备ID列表" required>
          <el-input
            v-model="ruleForm.deviceIdsText"
            type="textarea"
            :rows="3"
            placeholder="请输入设备ID，多个用逗号分隔"
          />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="ruleForm.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRuleDialog = false">取消</el-button>
        <el-button type="primary" @click="saveRule">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { alertApi } from '../api';

const activeAlerts = ref<any[]>([]);
const rules = ref<any[]>([]);
const loadingAlerts = ref(false);
const loadingRules = ref(false);
const showRuleDialog = ref(false);

const ruleForm = reactive({
  name: '',
  condition: '',
  threshold: 0,
  deviceIdsText: '',
  enabled: true
});

const formatTime = (time?: string) => {
  if (!time) return '-';
  return new Date(time).toLocaleString('zh-CN');
};

const getConditionText = (condition: string) => {
  const map: Record<string, string> = {
    GREATER_THAN: '大于',
    LESS_THAN: '小于',
    EQUAL: '等于'
  };
  return map[condition] || condition;
};

const loadAlerts = async () => {
  loadingAlerts.value = true;
  try {
    const res = await alertApi.getCurrent();
    activeAlerts.value = res.data || [];
  } catch (err: any) {
    ElMessage.error(err.message || '加载告警失败');
  } finally {
    loadingAlerts.value = false;
  }
};

const loadRules = async () => {
  loadingRules.value = true;
  try {
    const res = await alertApi.getRules();
    rules.value = res.data || [];
  } catch (err: any) {
    ElMessage.error(err.message || '加载规则失败');
  } finally {
    loadingRules.value = false;
  }
};

const saveRule = async () => {
  if (!ruleForm.name || !ruleForm.condition || !ruleForm.deviceIdsText) {
    ElMessage.warning('请填写完整信息');
    return;
  }

  try {
    await alertApi.createRule({
      name: ruleForm.name,
      condition: ruleForm.condition,
      threshold: ruleForm.threshold,
      deviceIds: ruleForm.deviceIdsText.split(',').map(id => id.trim()).filter(Boolean),
      enabled: ruleForm.enabled
    });
    ElMessage.success('规则创建成功');
    showRuleDialog.value = false;
    ruleForm.name = '';
    ruleForm.condition = '';
    ruleForm.threshold = 0;
    ruleForm.deviceIdsText = '';
    ruleForm.enabled = true;
    loadRules();
  } catch (err: any) {
    ElMessage.error(err.message || '创建规则失败');
  }
};

const resolveAlert = async (id: string) => {
  try {
    await alertApi.resolveAlert(id);
    ElMessage.success('告警已处理');
    loadAlerts();
  } catch (err: any) {
    ElMessage.error(err.message || '处理告警失败');
  }
};

onMounted(() => {
  loadAlerts();
  loadRules();
});
</script>

<style scoped>
.alert-center {
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
</style>

