<template>
  <div class="automation-control">
    <div class="page-header">
      <h2>自动化控制</h2>
      <el-button type="primary" @click="showDialog = true">
        <el-icon><Plus /></el-icon>
        创建自动化规则
      </el-button>
    </div>

    <el-card>
      <el-table :data="rules" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="规则ID" width="180" />
        <el-table-column prop="name" label="规则名称" />
        <el-table-column prop="condition" label="触发条件" />
        <el-table-column prop="action" label="执行动作" />
        <el-table-column prop="enabled" label="状态" width="100">
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
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="toggleRule(row)">
              {{ row.enabled ? '禁用' : '启用' }}
            </el-button>
            <el-button type="danger" size="small" @click="deleteRule(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 创建规则对话框 -->
    <el-dialog v-model="showDialog" title="创建自动化规则" width="500px">
      <el-form :model="ruleForm" label-width="100px">
        <el-form-item label="规则名称" required>
          <el-input v-model="ruleForm.name" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="触发条件" required>
          <el-input
            v-model="ruleForm.condition"
            type="textarea"
            :rows="2"
            placeholder="例如: 温度 > 30"
          />
        </el-form-item>
        <el-form-item label="执行动作" required>
          <el-input
            v-model="ruleForm.action"
            type="textarea"
            :rows="2"
            placeholder="例如: 打开空调"
          />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="ruleForm.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="saveRule">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { automationApi } from '../api';

const rules = ref<any[]>([]);
const loading = ref(false);
const showDialog = ref(false);

const ruleForm = reactive({
  name: '',
  condition: '',
  action: '',
  enabled: true
});

const formatTime = (time?: string) => {
  if (!time) return '-';
  return new Date(time).toLocaleString('zh-CN');
};

const loadRules = async () => {
  loading.value = true;
  try {
    const res = await automationApi.getRules();
    rules.value = res.data || [];
  } catch (err: any) {
    ElMessage.error(err.message || '加载规则失败');
  } finally {
    loading.value = false;
  }
};

const saveRule = async () => {
  if (!ruleForm.name || !ruleForm.condition || !ruleForm.action) {
    ElMessage.warning('请填写完整信息');
    return;
  }
  try {
    await automationApi.createRule(ruleForm);
    ElMessage.success('规则创建成功');
    showDialog.value = false;
    ruleForm.name = '';
    ruleForm.condition = '';
    ruleForm.action = '';
    ruleForm.enabled = true;
    loadRules();
  } catch (err: any) {
    ElMessage.error(err.message || '创建规则失败');
  }
};

const toggleRule = async (rule: any) => {
  try {
    await automationApi.toggleRule(rule.id);
    ElMessage.success(`规则已${rule.enabled ? '禁用' : '启用'}`);
    loadRules();
  } catch (err: any) {
    ElMessage.error(err.message || '操作失败');
  }
};

const deleteRule = async (id: string) => {
  try {
    await automationApi.deleteRule(id);
    ElMessage.success('规则已删除');
    loadRules();
  } catch (err: any) {
    ElMessage.error(err.message || '删除失败');
  }
};

onMounted(() => {
  loadRules();
});
</script>

<style scoped>
.automation-control {
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

