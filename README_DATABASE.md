# 数据库配置说明

## 1. 创建数据库

执行 `backend/database/schema.sql` 文件创建数据库和表结构：

```sql
-- 在MySQL中执行
source backend/database/schema.sql;
```

或者直接在MySQL客户端中执行SQL文件内容。

## 2. 配置数据库连接

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smart_classroom?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root      # 修改为你的MySQL用户名
    password: root      # 修改为你的MySQL密码
```

## 3. 数据库表结构

数据库包含以下表：
- `devices` - 设备表
- `sensor_data` - 传感器数据表
- `alert_rules` - 告警规则表
- `active_alerts` - 活跃告警表
- `alert_history` - 告警历史表
- `automation_rules` - 自动化规则表
- `control_commands` - 控制指令表
- `classrooms` - 教室表

## 4. 启动项目

1. 确保MySQL已启动
2. 确保数据库已创建
3. 启动后端：`cd backend && mvn spring-boot:run`
4. 启动前端：`cd frontend && npm run dev`

## 5. 验证

访问 `http://localhost:5173/` 查看仪表板，应该能看到：
- 设备统计数据（从数据库查询）
- 设备列表（从数据库查询）
- 数据可视化图表





