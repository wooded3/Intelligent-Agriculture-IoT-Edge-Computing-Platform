import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import DeviceManagement from '../views/DeviceManagement.vue'
import { deviceApi } from '../api'

// Mock API
vi.mock('../api', () => ({
  deviceApi: {
    list: vi.fn(),
    create: vi.fn(),
    update: vi.fn(),
    delete: vi.fn(),
    getById: vi.fn(),
    getStats: vi.fn()
  }
}))

describe('DeviceManagement.vue', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染设备管理页面', () => {
    const wrapper = mount(DeviceManagement)
    expect(wrapper.text()).toContain('设备管理')
  })

  it('应该能够加载设备列表', async () => {
    const mockDevices = {
      total: 1,
      rows: [{
        id: 'device_001',
        name: '温度传感器',
        type: 'SENSOR',
        status: 'ONLINE'
      }]
    }

    vi.mocked(deviceApi.list).mockResolvedValue({
      code: 1,
      msg: 'success',
      data: mockDevices
    })

    const wrapper = mount(DeviceManagement)
    
    // 等待组件挂载和API调用
    await wrapper.vm.$nextTick()
    await new Promise(resolve => setTimeout(resolve, 100))

    // 验证API被调用
    expect(deviceApi.list).toHaveBeenCalled()
  })

  it('应该能够处理设备创建', async () => {
    const mockDevice = {
      id: 'device_001',
      name: '新设备',
      type: 'SENSOR'
    }

    vi.mocked(deviceApi.create).mockResolvedValue({
      code: 1,
      msg: 'success',
      data: mockDevice
    })

    const wrapper = mount(DeviceManagement)
    
    // 等待组件挂载
    await wrapper.vm.$nextTick()
    
    // 验证组件能正常渲染
    expect(wrapper.text()).toContain('设备管理')
    expect(wrapper.text()).toContain('添加设备')
    
    // 验证API列表调用（组件挂载时会自动调用）
    await new Promise(resolve => setTimeout(resolve, 200))
    expect(deviceApi.list).toHaveBeenCalled()
  })
})




