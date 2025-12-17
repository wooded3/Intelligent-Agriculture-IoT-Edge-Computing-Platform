# 智慧教室物联网系统接口文档

## 环境配置

### 基础URL
```
http://localhost:8080/api
```

### 请求头
```
Content-Type: application/json
```

---

## 1. 设备管理模块

### 1.1 查询设备列表

**接口信息：**
- **方法**：GET
- **路径**：`/devices`
- **完整URL**：`http://localhost:8080/api/devices`

**请求参数（Query）：**
```
page: 1
pageSize: 10
classroomId: (可选) classroom_101
type: (可选) SENSOR
```

**测试用例：**
```
GET http://localhost:8080/api/devices?page=1&pageSize=10
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "total": 10,
    "rows": [
      {
        "id": "device_xxx",
        "name": "温度传感器-101",
        "type": "SENSOR",
        "status": "ONLINE",
        "classroomId": "classroom_101",
        "createTime": "2024-01-01T10:00:00"
      }
    ]
  }
}
```

---

### 1.2 获取设备统计信息

**接口信息：**
- **方法**：GET
- **路径**：`/devices/stats`
- **完整URL**：`http://localhost:8080/api/devices/stats`

**测试用例：**
```
GET http://localhost:8080/api/devices/stats
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "total": 20,
    "online": 15,
    "offline": 3,
    "error": 2
  }
}
```

---

### 1.3 根据ID查询设备

**接口信息：**
- **方法**：GET
- **路径**：`/devices/{id}`
- **完整URL**：`http://localhost:8080/api/devices/device_12345678`

**测试用例：**
```
GET http://localhost:8080/api/devices/device_12345678
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": "device_12345678",
    "name": "温度传感器-101",
    "type": "SENSOR",
    "status": "ONLINE",
    "classroomId": "classroom_101",
    "createTime": "2024-01-01T10:00:00"
  }
}
```

---

### 1.4 注册设备

**接口信息：**
- **方法**：POST
- **路径**：`/devices`
- **完整URL**：`http://localhost:8080/api/devices`

**请求体（Body - JSON）：**
```json
{
  "name": "温度传感器-102",
  "type": "SENSOR",
  "classroomId": "classroom_102",
  "config": {
    "samplingRate": 60,
    "unit": "°C"
  }
}
```

**测试用例：**
```
POST http://localhost:8080/api/devices
Content-Type: application/json

{
  "name": "温度传感器-102",
  "type": "SENSOR",
  "classroomId": "classroom_102",
  "config": {
    "samplingRate": 60,
    "unit": "°C"
  }
}
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": "device_xxxxxxxx",
    "name": "温度传感器-102",
    "type": "SENSOR",
    "status": "OFFLINE",
    "classroomId": "classroom_102",
    "createTime": "2024-01-01T10:00:00"
  }
}
```

---

### 1.5 更新设备

**接口信息：**
- **方法**：PUT
- **路径**：`/devices/{id}`
- **完整URL**：`http://localhost:8080/api/devices/device_12345678`

**请求体（Body - JSON）：**
```json
{
  "name": "温度传感器-102-更新",
  "type": "SENSOR",
  "classroomId": "classroom_102",
  "config": {
    "samplingRate": 120
  }
}
```

**测试用例：**
```
PUT http://localhost:8080/api/devices/device_12345678
Content-Type: application/json

{
  "name": "温度传感器-102-更新",
  "type": "SENSOR",
  "classroomId": "classroom_102",
  "config": {
    "samplingRate": 120
  }
}
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": "device_12345678",
    "name": "温度传感器-102-更新",
    "type": "SENSOR",
    "status": "ONLINE",
    "classroomId": "classroom_102"
  }
}
```

---

### 1.6 删除设备

**接口信息：**
- **方法**：DELETE
- **路径**：`/devices/{id}`
- **完整URL**：`http://localhost:8080/api/devices/device_12345678`

**测试用例：**
```
DELETE http://localhost:8080/api/devices/device_12345678
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

### 1.7 更新设备状态

**接口信息：**
- **方法**：PATCH
- **路径**：`/devices/{id}/status`
- **完整URL**：`http://localhost:8080/api/devices/device_12345678/status?status=ONLINE`

**请求参数（Query）：**
```
status: ONLINE
```

**测试用例：**
```
PATCH http://localhost:8080/api/devices/device_12345678/status?status=ONLINE
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

## 2. 数据管理模块

### 2.1 上报传感器数据

**接口信息：**
- **方法**：POST
- **路径**：`/sensor-data`
- **完整URL**：`http://localhost:8080/api/sensor-data`

**请求体（Body - JSON）：**
```json
{
  "deviceId": "device_12345678",
  "value": 25.6,
  "unit": "°C",
  "timestamp": "2024-01-01T10:00:00Z"
}
```

**测试用例：**
```
POST http://localhost:8080/api/sensor-data
Content-Type: application/json

{
  "deviceId": "device_12345678",
  "value": 25.6,
  "unit": "°C",
  "timestamp": "2024-01-01T10:00:00Z"
}
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

### 2.2 查询传感器数据

**接口信息：**
- **方法**：GET
- **路径**：`/sensor-data`
- **完整URL**：`http://localhost:8080/api/sensor-data`

**请求参数（Query）：**
```
deviceId: (可选) device_12345678
startTime: (可选) 2024-01-01T00:00:00Z
endTime: (可选) 2024-01-01T23:59:59Z
page: 1
pageSize: 10
```

**测试用例：**
```
GET http://localhost:8080/api/sensor-data?deviceId=device_12345678&page=1&pageSize=10
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "total": 100,
    "rows": [
      {
        "id": "metric_xxxxxxxx",
        "deviceId": "device_12345678",
        "value": 25.6,
        "unit": "°C",
        "timestamp": "2024-01-01T10:00:00Z"
      }
    ]
  }
}
```

---

## 3. 告警管理模块

### 3.1 查询告警规则列表

**接口信息：**
- **方法**：GET
- **路径**：`/alerts/rules`
- **完整URL**：`http://localhost:8080/api/alerts/rules`

**测试用例：**
```
GET http://localhost:8080/api/alerts/rules
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": "rule_1",
      "name": "高温告警",
      "condition": "GREATER_THAN",
      "threshold": 30.0,
      "deviceIds": ["device_12345678"],
      "enabled": true,
      "createTime": "2024-01-01T10:00:00Z"
    }
  ]
}
```

---

### 3.2 创建告警规则

**接口信息：**
- **方法**：POST
- **路径**：`/alerts/rules`
- **完整URL**：`http://localhost:8080/api/alerts/rules`

**请求体（Body - JSON）：**
```json
{
  "name": "高温告警",
  "condition": "GREATER_THAN",
  "threshold": 30.0,
  "deviceIds": ["device_12345678", "device_87654321"],
  "enabled": true
}
```

**测试用例：**
```
POST http://localhost:8080/api/alerts/rules
Content-Type: application/json

{
  "name": "高温告警",
  "condition": "GREATER_THAN",
  "threshold": 30.0,
  "deviceIds": ["device_12345678"],
  "enabled": true
}
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "告警规则创建成功",
  "data": {
    "id": "rule_xxxxxxxx",
    "name": "高温告警",
    "condition": "GREATER_THAN",
    "threshold": 30.0,
    "deviceIds": ["device_12345678"],
    "enabled": true,
    "createTime": "2024-01-01T10:00:00Z"
  }
}
```

---

### 3.3 查询当前活跃告警

**接口信息：**
- **方法**：GET
- **路径**：`/alerts/current`
- **完整URL**：`http://localhost:8080/api/alerts/current`

**测试用例：**
```
GET http://localhost:8080/api/alerts/current
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": "alert_xxxxxxxx",
      "ruleName": "高温告警",
      "deviceName": "温度传感器-101",
      "value": 31.5,
      "threshold": 30.0,
      "timestamp": "2024-01-01T10:00:00Z"
    }
  ]
}
```

---

### 3.4 处理告警（标记为已解决）

**接口信息：**
- **方法**：PATCH
- **路径**：`/alerts/{alertId}/resolve`
- **完整URL**：`http://localhost:8080/api/alerts/alert_12345678/resolve`

**测试用例：**
```
PATCH http://localhost:8080/api/alerts/alert_12345678/resolve
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

## 4. 自动化控制模块

### 4.1 查询自动化规则列表

**接口信息：**
- **方法**：GET
- **路径**：`/automation/rules`
- **完整URL**：`http://localhost:8080/api/automation/rules`

**测试用例：**
```
GET http://localhost:8080/api/automation/rules
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": "auto_001",
      "name": "自动调光",
      "condition": "光照度 < 300",
      "action": "打开灯光",
      "enabled": true,
      "createTime": "2024-01-01T10:00:00Z"
    }
  ]
}
```

---

### 4.2 创建自动化规则

**接口信息：**
- **方法**：POST
- **路径**：`/automation/rules`
- **完整URL**：`http://localhost:8080/api/automation/rules`

**请求体（Body - JSON）：**
```json
{
  "name": "自动调温",
  "condition": "温度 > 30",
  "action": "打开空调",
  "enabled": true
}
```

**测试用例：**
```
POST http://localhost:8080/api/automation/rules
Content-Type: application/json

{
  "name": "自动调温",
  "condition": "温度 > 30",
  "action": "打开空调",
  "enabled": true
}
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": "auto_xxxxxxxx",
    "name": "自动调温",
    "condition": "温度 > 30",
    "action": "打开空调",
    "enabled": true,
    "createTime": "2024-01-01T10:00:00Z"
  }
}
```

---

### 4.3 启用/禁用自动化规则

**接口信息：**
- **方法**：PATCH
- **路径**：`/automation/rules/{id}/toggle`
- **完整URL**：`http://localhost:8080/api/automation/rules/auto_001/toggle`

**测试用例：**
```
PATCH http://localhost:8080/api/automation/rules/auto_001/toggle
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

### 4.4 删除自动化规则

**接口信息：**
- **方法**：DELETE
- **路径**：`/automation/rules/{id}`
- **完整URL**：`http://localhost:8080/api/automation/rules/auto_001`

**测试用例：**
```
DELETE http://localhost:8080/api/automation/rules/auto_001
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

## 5. 系统维护模块

### 5.1 查询系统状态

**接口信息：**
- **方法**：GET
- **路径**：`/maintenance/status`
- **完整URL**：`http://localhost:8080/api/maintenance/status`

**测试用例：**
```
GET http://localhost:8080/api/maintenance/status
```

**期望响应：**
```
OK
```
（注意：这个接口返回的是纯文本，不是JSON）

---

### 5.2 查询教室列表

**接口信息：**
- **方法**：GET
- **路径**：`/classrooms`
- **完整URL**：`http://localhost:8080/api/classrooms`

**测试用例：**
```
GET http://localhost:8080/api/classrooms
```

**期望响应：**
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": "classroom_101",
      "name": "101教室",
      "location": "教学楼A栋1层",
      "deviceCount": 5
    },
    {
      "id": "classroom_102",
      "name": "102教室",
      "location": "教学楼A栋1层",
      "deviceCount": 3
    }
  ]
}
```

---

## 6. Apifox 测试说明

### 6.1 环境配置

在Apifox中创建环境，设置变量：
- `baseUrl = http://localhost:8080/api`

### 6.2 测试流程建议

#### 完整测试流程（按顺序执行）

1. **设备管理模块**
   - 先注册一个设备（1.4）
   - 查询设备列表（1.1）
   - 获取设备统计（1.2）
   - 查询刚注册的设备详情（1.3）
   - 更新设备信息（1.5）
   - 更新设备状态（1.7）
   - 最后删除设备（1.6）

2. **数据管理模块**
   - 先确保有设备ID（从步骤1获取）
   - 上报传感器数据（2.1）
   - 查询上报的数据（2.2）

3. **告警管理模块**
   - 创建告警规则（3.2）
   - 查询告警规则列表（3.1）
   - 查询活跃告警（3.3）
   - 处理告警（3.4）

4. **自动化控制模块**
   - 查询自动化规则列表（4.1）
   - 创建自动化规则（4.2）
   - 启用/禁用规则（4.3）
   - 删除规则（4.4）

5. **系统维护模块**
   - 查询系统状态（5.1）
   - 查询教室列表（5.2）

### 6.3 Apifox 使用提示

1. **创建环境变量**
   - 在Apifox中创建环境，设置变量 `baseUrl = http://localhost:8080/api`
   - 在接口路径中使用 `{{baseUrl}}/devices` 的形式

2. **保存响应数据**
   - 在测试用例中，可以将返回的设备ID、规则ID等保存为环境变量
   - 后续测试用例可以直接使用这些变量

3. **批量测试**
   - 可以使用Apifox的测试套件功能，批量执行所有测试用例
   - 设置断言验证响应状态码和数据格式

4. **导出测试报告**
   - 测试完成后可以导出测试报告，记录测试结果

### 6.4 常见错误处理

#### 错误响应格式
```json
{
  "code": 0,
  "msg": "错误信息描述",
  "data": null
}
```

#### 常见错误场景

1. **设备不存在**
   - 使用不存在的设备ID查询或更新
   - 期望：返回错误信息"设备不存在"

2. **参数验证失败**
   - 缺少必填字段（如name、type）
   - 期望：返回错误信息"设备名称不能为空"等

3. **规则不存在**
   - 使用不存在的规则ID操作
   - 期望：返回错误信息"规则不存在"
