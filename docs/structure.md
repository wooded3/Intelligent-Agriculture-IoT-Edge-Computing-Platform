# 🗂️ 智慧农业物联网边缘计算平台 - 项目结构

## 顶层目录结构

```text
Intelligent-Agriculture-IoT-Edge-Computing-Platform/
├── backend/              # Spring Boot 后端服务
├── frontend/             # 前端应用（预留）
├── device-simulators/    # 设备模拟脚本（预留）
├── docs/                 # 文档：需求、设计、标准等
├── README.md             # 项目说明
└── .gitignore
```

## 后端项目结构（Spring Boot）

后端采用按业务域划分包结构，以 `com.iaiotecp.backend` 为根包。

```text
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/iaiotecp/backend/
    │   │       ├── BackendApplication.java   # 启动类
    │   │       ├── common/                  # 通用组件（预留）
    │   │       ├── device/                  # 设备管理模块
    │   │       │   ├── DeviceController.java
    │   │       │   ├── DeviceService.java
    │   │       │   ├── DeviceServiceImpl.java
    │   │       │   └── model/
    │   │       │       └── DeviceSummary.java
    │   │       ├── data/                    # 数据采集与管理模块
    │   │       │   ├── DataController.java
    │   │       │   ├── DataService.java
    │   │       │   ├── DataServiceImpl.java
    │   │       │   └── model/
    │   │       │       └── MetricRecord.java
    │   │       ├── alert/                   # 告警管理模块
    │   │       │   ├── AlertController.java
    │   │       │   ├── AlertService.java
    │   │       │   ├── AlertServiceImpl.java
    │   │       │   └── model/
    │   │       │       └── Alert.java
    │   │       ├── control/                 # 自动化控制模块
    │   │       │   ├── ControlController.java
    │   │       │   ├── ControlService.java
    │   │       │   ├── ControlServiceImpl.java
    │   │       │   └── model/
    │   │       │       └── ControlCommand.java
    │   │       └── maintenance/             # 系统维护/运维模块
    │   │           ├── MaintenanceController.java
    │   │           ├── MaintenanceService.java
    │   │           └── MaintenanceServiceImpl.java
    │   └── resources/
    │       └── application.yml              # 基础配置
    └── test/
        └── java/
            └── com/iaiotecp/backend/
                └── BackendApplicationTests.java
```

各模块职责简要说明：

- `device`：设备列表、设备基础信息管理，为设备注册/展示提供接口。
- `data`：接收设备上报的环境数据（如温度、湿度），负责存储和简单处理。
- `alert`：根据数据和规则生成告警，提供告警查询接口。
- `control`：对设备下发控制指令（如开关、调节参数），记录控制操作。
- `maintenance`：提供系统状态检查、运维相关接口（如 `/maintenance/status`）。

## 前端与设备模拟结构（规划）

当前前端与设备模拟脚本尚在规划中，预期结构如下：

```text
frontend/
  └── （待定：可使用 React/Vue 或简单静态页面）

device-simulators/
  ├── README.md              # 说明如何运行模拟设备
  ├── python/
  │   └── temperature_sender.py
  └── node/
      └── temperature_sender.js
```
上述模拟脚本会周期性向 `backend` 的 `/data/metrics` 接口上报数据，用于联调端到端流程。
