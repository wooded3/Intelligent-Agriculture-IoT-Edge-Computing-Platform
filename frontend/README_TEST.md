# 前端测试说明

## 安装测试依赖

```bash
cd frontend
npm install
```

## 运行测试

### 运行所有测试
```bash
npm test
```

### 运行测试并查看覆盖率
```bash
npm run test:coverage
```

### 运行测试UI界面
```bash
npm run test:ui
```

## 测试文件说明

- `src/test/DeviceManagement.test.ts`: 设备管理页面测试

## 注意事项

1. 测试需要先安装依赖：`npm install`
2. 如果遇到模块导入错误，检查 `vitest.config.ts` 配置
3. 前端测试主要测试组件渲染和基本交互




