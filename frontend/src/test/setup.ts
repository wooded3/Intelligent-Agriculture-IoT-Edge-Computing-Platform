import { vi } from 'vitest'
import { config } from '@vue/test-utils'

// Mock Element Plus
vi.mock('element-plus', () => ({
  ElMessage: {
    success: vi.fn(),
    error: vi.fn(),
    warning: vi.fn()
  },
  ElMessageBox: {
    confirm: vi.fn()
  }
}))

// Global test setup
config.global.mocks = {
  $t: (key: string) => key
}




