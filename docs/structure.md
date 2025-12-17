# 🗂️ 智慧教室物联网边缘计算平台 - 项目结构

## 顶层目录结构

```text
Intelligent-Agriculture-IoT-Edge-Computing-Platform/
├── backend/              # Spring Boot 后端服务
├── frontend/             # Vue 3 前端应用
├── docs/                 # 文档：需求、设计、标准等
├── README.md             # 项目说明
├── README_DATABASE.md    # 数据库说明
└── .gitignore
```

## 后端项目结构（Spring Boot）

后端采用按业务域划分包结构，以 `com.iaiotecp.backend` 为根包。

```text
backend/
├── pom.xml                                    # Maven 配置文件
├── database/
│   └── schema.sql                            # 数据库表结构定义
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/iaiotecp/backend/
    │   │       ├── BackendApplication.java   # Spring Boot 启动类
    │   │       ├── device/                   # 设备管理模块
    │   │       │   ├── DeviceController.java
    │   │       │   ├── DeviceService.java
    │   │       │   ├── DeviceServiceImpl.java
    │   │       │   ├── mapper/
    │   │       │   │   └── DeviceMapper.java
    │   │       │   └── model/
    │   │       │       ├── Device.java
    │   │       │       ├── DeviceRegisterRequest.java
    │   │       │       ├── DeviceSummary.java
    │   │       │       └── Result.java
    │   │       ├── data/                     # 数据采集与管理模块
    │   │       │   ├── DataController.java
    │   │       │   ├── DataService.java
    │   │       │   ├── DataServiceImpl.java
    │   │       │   ├── mapper/
    │   │       │   │   └── MetricMapper.java
    │   │       │   └── model/
    │   │       │       ├── MetricPage.java
    │   │       │       └── MetricRecord.java
    │   │       ├── alert/                    # 告警管理模块
    │   │       │   ├── AlertController.java
    │   │       │   ├── AlertRule.java
    │   │       │   ├── AlertService.java
    │   │       │   ├── AlertServiceImpl.java
    │   │       │   └── model/
    │   │       │       ├── ActiveAlert.java
    │   │       │       ├── Alert.java
    │   │       │       └── AlertRule.java
    │   │       ├── control/                  # 自动化控制模块
    │   │       │   ├── AutomationController.java
    │   │       │   ├── ControlController.java
    │   │       │   ├── ControlService.java
    │   │       │   ├── ControlServiceImpl.java
    │   │       │   └── model/
    │   │       │       ├── AutomationRule.java
    │   │       │       └── ControlCommand.java
    │   │       └── maintenance/              # 系统维护/运维模块
    │   │           ├── ClassroomController.java
    │   │           ├── MaintenanceController.java
    │   │           ├── MaintenanceService.java
    │   │           ├── MaintenanceServiceImpl.java
    │   │           └── model/
    │   │               └── Classroom.java
    │   └── resources/
    │       └── application.yml               # Spring Boot 配置文件
    └── test/
        └── java/
            └── com/iaiotecp/backend/
                └── BackendApplicationTests.java
```

### 各模块职责说明

- **device（设备管理模块）**：
  - `DeviceController``: RESTful API 控制器，处理设备相关的 HTTP 请求
  - `DeviceService/DeviceServiceImpl``: 业务逻辑层，实现设备注册、查询、更新、删除等功能
  - `DeviceMapper``: MyBatis 数据访问层，执行数据库操作
  - `model/`: 数据模型，包括设备实体、请求对象、响应对象等

- **data（数据管理模块）**：
  - `DataController``: 接收设备上报的传感器数据
  - `DataService/DataServiceImpl``: 数据处理业务逻辑，包括数据保存和查询
  - `MetricMapper``: 数据访问层，操作 metrics 表
  - `model/`: 数据记录模型和分页模型

- **alert（告警管理模块）**：
  - `AlertController``: 告警规则和活跃告警的 API 接口
  - `AlertService/AlertServiceImpl``: 告警检测逻辑，自动生成告警
  - `model/`: 告警规则和活跃告警的数据模型

- **control（自动化控制模块）**：
  - `AutomationController``: 自动化规则管理接口
  - `ControlController``: 设备控制指令接口
  - `ControlService/ControlServiceImpl``: 控制逻辑实现
  - `model/`: 自动化规则和控制命令模型

- **maintenance（系统维护模块）**：
  - `MaintenanceController``: 系统状态检查接口
  - `ClassroomController``: 教室信息管理接口
  - `MaintenanceService/MaintenanceServiceImpl``: 维护相关业务逻辑
  - `model/`: 教室实体模型

## 前端项目结构（Vue 3 + TypeScript）

前端采用 Vue 3 + TypeScript + Vite 构建，使用 Element Plus 组件库。

```text
frontend/
├── package.json              # 项目依赖配置
├── package-lock.json         # 依赖锁定文件
├── vite.config.ts            # Vite 构建配置
├── tsconfig.json             # TypeScript 配置
├── .eslintrc.cjs             # ESLint 代码检查配置
├── env.d.ts                  # TypeScript 环境类型定义
├── index.html                # HTML 入口文件
└── src/
    ├── main.ts               # 应用入口文件
    ├── App.vue               # 根组件
    ├── style.css             # 全局样式
    ├── api/                  # API 调用封装
    │   └── index.ts          # 统一的 API 请求封装
    ├── router/               # 路由配置
    │   └── index.ts          # Vue Router 路由定义
    ├── components/           # 公共组件
    │   └── Layout.vue        # 布局组件
    └── views/                # 页面组件
        ├── Dashboard.vue            # 仪表板页面
        ├── DeviceManagement.vue     # 设备管理页面
        ├── DataMonitoring.vue       # 数据监控页面
        ├── AlertCenter.vue          # 告警中心页面
        ├── AutomationControl.vue    # 自动化控制页面
        └── SystemManagement.vue     # 系统管理页面
```

### 前端模块说明

- **api/**: 封装所有后端 API 调用，统一处理请求和响应
- **router/**: 定义前端路由，配置页面导航
- **components/**: 可复用的 Vue 组件，如布局组件
- **views/**: 各个功能页面的 Vue 组件
  - `Dashboard.vue`: 系统概览，显示设备统计、图表等
  - `DeviceManagement.vue`: 设备列表、注册、编辑、删除
  - `DataMonitoring.vue`: 数据查询和展示
  - `AlertCenter.vue`: 告警规则管理和活跃告警展示
  - `AutomationControl.vue`: 自动化规则管理
  - `SystemManagement.vue`: 系统维护相关功能

## 文档结构

```text
docs/
├── requirements.md              # 需求规格说明书
├── structure.md                # 项目结构说明（本文件）
├── interface.md                # API 接口文档
├── implementation_report.md    # 系统原型构建与实现报告
├── test_report.md             # 测试报告
├── plan.md                    # 项目开发规划
├── standards.md               # 开发规范
├── circulation.md              # 数据流转说明
├── architecture_and_test.md   # 架构介绍与测试说明
├── architecture_qa.md         # 架构问答指南
├── architecture_quick_ref.md # 架构快速参考
├── implementation_report.html  # 实现报告（HTML格式）
└── diagrams/                   # UML 图表
    ├── puml/                  # PlantUML 源文件
    │   ├── 智慧教室系统域模型.puml
    │   ├── 设备注册流程活动图.puml
    │   ├── 数据管理模块用例图.puml
    │   ├── 告警管理模块用例图.puml
    │   ├── 告警触发流程活动图.puml
    │   ├── 自动化控制模块用例图.puml
    │   ├── 自动化控制执行活动图.puml
    │   └── 系统维护模块用例图.puml
    └── images/                # 生成的图片文件
        ├── 智慧教室系统域模型.png
        ├── 智慧教室系统域模型.svg
        └── ...（其他图表）
```

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 3.5.8
- **语言**: Java 21
- **数据库**: MySQL
- **ORM**: MyBatis 3.0.3
- **分页**: PageHelper 2.1.0
- **构建工具**: Maven
- **连接池**: HikariCP

### 前端技术栈
- **框架**: Vue 3.5.0
- **语言**: TypeScript 5.5.0
- **构建工具**: Vite 5.4.0
- **UI 组件库**: Element Plus 2.4.4
- **路由**: Vue Router 4.2.5
- **图表**: ECharts 5.4.3 + vue-echarts 6.6.1
- **代码检查**: ESLint

## 数据访问层说明

### MyBatis Mapper
- `DeviceMapper`: 设备表的 CRUD 操作
- `MetricMapper`: 传感器数据表的插入和查询操作

### 数据库表
主要数据表（定义在 `backend/database/schema.sql`）：
- `devices`: 设备信息表
- `metrics`: 传感器数据表
- `classrooms`: 教室信息表（如需要）

## 项目运行说明

### 后端启动
```bash
cd backend
mvn spring-boot:run
```
后端服务运行在 `http://localhost:8080/api`

### 前端启动
```bash
cd frontend
npm install  # 首次运行需要安装依赖
npm run dev
```
前端服务运行在 `http://localhost:5173`

### 数据库配置
数据库配置在 `backend/src/main/resources/application.yml` 中，需要：
1. 创建 MySQL 数据库（默认：`smart_classroom`）
2. 执行 `backend/database/schema.sql` 创建表结构
3. 配置数据库连接信息（用户名、密码等）
