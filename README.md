## Intelligent Agriculture IoT Edge Computing Platform

本项目采用前后端分离结构：

- 后端：Java + Spring Boot（位于 `backend/`）
- 前端：Vue 3 + TypeScript + Vite（位于 `frontend/`）

---

## Backend（Spring Boot）

### 环境要求

- JDK 21（或与项目 `pom.xml` 要求一致的版本）
- Maven 3.8+

### 构建与运行

在 `backend/` 目录下：

```powershell
cd backend
mvn clean package
```

打包成功后，运行可执行 JAR（版本号以实际生成为准）：

```powershell
cd target
java -jar backend-0.0.1-SNAPSHOT.jar
```

默认后端将运行在 `http://localhost:8080/`（如有修改请以 `application.yml` 为准）。

### 开发模式运行（无需打包）

开发调试时，可以使用 Spring Boot 插件直接运行应用：

```powershell
cd backend
mvn spring-boot:run
```

---

## Frontend（Vue 3 + Vite）

### 环境要求

- Node.js（建议安装 LTS 版本，安装后自带 npm）

### 安装依赖

在 `frontend/` 目录下：

```powershell
cd frontend
npm install
```

### 开发模式启动

```powershell
cd frontend
npm run dev
```

启动后根据命令行提示打开浏览器，一般为：

- `http://localhost:5173/`

### 前端打包构建

```powershell
cd frontend
npm run build
```

构建产物将输出到 `frontend/dist/` 目录，可用于部署到静态资源服务器或与后端整合部署。
