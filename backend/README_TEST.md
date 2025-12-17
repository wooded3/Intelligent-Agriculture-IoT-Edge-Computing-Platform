# 后端测试说明

## 运行测试

### 运行所有测试
```bash
cd backend
mvn test
```

### 运行特定测试类
```bash
cd backend
mvn test -Dtest=DeviceServiceTest
mvn test -Dtest=DataServiceTest
mvn test -Dtest=AlertServiceTest
```

### 运行特定测试方法
```bash
cd backend
mvn test -Dtest=DeviceServiceTest#testRegisterDevice_Success
```

## 测试类说明

- `DeviceServiceTest`: 设备管理服务测试
- `DeviceControllerTest`: 设备管理控制器测试
- `DataServiceTest`: 数据管理服务测试
- `DataControllerTest`: 数据管理控制器测试
- `AlertServiceTest`: 告警管理服务测试
- `AlertControllerTest`: 告警管理控制器测试

## 测试覆盖率

运行测试后，可以使用以下命令生成测试覆盖率报告：

```bash
mvn test jacoco:report
```

报告将生成在 `target/site/jacoco/index.html`




