/// <reference types="vitest" />
import { defineConfig } from 'vitest/config'

declare module 'vitest' {
  interface TestContext {
    // 可以在这里添加自定义测试上下文类型
  }
}




