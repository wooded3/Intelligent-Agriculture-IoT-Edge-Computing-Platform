package com.iaiotecp.backend.device;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iaiotecp.backend.device.mapper.DeviceMapper;
import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceSummary;

@ExtendWith(MockitoExtension.class)
@DisplayName("设备管理服务测试")
class DeviceServiceTest {

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private DeviceServiceImpl deviceService;

    private Device testDevice;

    @BeforeEach
    void setUp() {
        testDevice = new Device();
        testDevice.setId("device_12345678");
        testDevice.setName("测试设备");
        testDevice.setType("SENSOR");
        testDevice.setStatus("ONLINE");
        testDevice.setClassroomId("classroom_101");
        testDevice.setConfig("{}");
        testDevice.setCreateTime(LocalDateTime.now());
        testDevice.setUpdateTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("设备注册 - 成功路径")
    void testRegisterDevice_Success() {
        // Given
        String name = "温度传感器";
        String type = "SENSOR";
        String classroomId = "classroom_101";
        Map<String, Object> config = Map.of("samplingInterval", 30);

        when(deviceMapper.insert(any(Device.class))).thenAnswer(invocation -> {
            Device device = invocation.getArgument(0);
            assertNotNull(device.getId());
            assertTrue(device.getId().startsWith("device_"));
            return null;
        });

        // When
        Device result = deviceService.registerDevice(name, type, classroomId, config);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(name, result.getName());
        assertEquals("SENSOR", result.getType());
        assertEquals(classroomId, result.getClassroomId());
        assertEquals("OFFLINE", result.getStatus());
        verify(deviceMapper, times(1)).insert(any(Device.class));
    }

    @Test
    @DisplayName("设备注册 - 异常路径：名称为空")
    void testRegisterDevice_EmptyName() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> deviceService.registerDevice("", "SENSOR", "classroom_101", null)
        );
        assertEquals("设备名称不能为空", exception.getMessage());
        verify(deviceMapper, never()).insert(any(Device.class));
    }

    @Test
    @DisplayName("设备注册 - 异常路径：无效设备类型")
    void testRegisterDevice_InvalidType() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> deviceService.registerDevice("测试设备", "INVALID", "classroom_101", null)
        );
        assertTrue(exception.getMessage().contains("无效的设备类型"));
        verify(deviceMapper, never()).insert(any(Device.class));
    }

    @Test
    @DisplayName("查询设备 - 成功路径")
    void testGetDeviceById_Success() {
        // Given
        String deviceId = "device_12345678";
        when(deviceMapper.selectById(deviceId)).thenReturn(testDevice);

        // When
        Device result = deviceService.getDeviceById(deviceId);

        // Then
        assertNotNull(result);
        assertEquals(deviceId, result.getId());
        assertEquals("测试设备", result.getName());
        verify(deviceMapper, times(1)).selectById(deviceId);
    }

    @Test
    @DisplayName("查询设备 - 异常路径：设备不存在")
    void testGetDeviceById_NotFound() {
        // Given
        String deviceId = "device_not_exist";
        when(deviceMapper.selectById(deviceId)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> deviceService.getDeviceById(deviceId)
        );
        assertTrue(exception.getMessage().contains("设备不存在"));
    }

    @Test
    @DisplayName("查询设备列表 - 成功路径")
    void testGetDeviceList_Success() {
        // Given
        List<Device> devices = Arrays.asList(testDevice);
        when(deviceMapper.selectList(null, null)).thenReturn(devices);

        // When
        DeviceSummary result = deviceService.getDeviceList(1, 10, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRows().size());
        verify(deviceMapper, times(1)).selectList(null, null);
    }

    @Test
    @DisplayName("更新设备 - 成功路径")
    void testUpdateDevice_Success() {
        // Given
        String deviceId = "device_12345678";
        when(deviceMapper.selectById(deviceId)).thenReturn(testDevice);
        doNothing().when(deviceMapper).update(any(Device.class));

        // When
        Device result = deviceService.updateDevice(deviceId, "新设备名称", null, null, null);

        // Then
        assertNotNull(result);
        assertEquals("新设备名称", result.getName());
        verify(deviceMapper, times(1)).selectById(deviceId);
        verify(deviceMapper, times(1)).update(any(Device.class));
    }

    @Test
    @DisplayName("删除设备 - 成功路径")
    void testDeleteDevice_Success() {
        // Given
        String deviceId = "device_12345678";
        when(deviceMapper.selectById(deviceId)).thenReturn(testDevice);
        doNothing().when(deviceMapper).deleteById(deviceId);

        // When
        boolean result = deviceService.deleteDevice(deviceId);

        // Then
        assertTrue(result);
        verify(deviceMapper, times(1)).selectById(deviceId);
        verify(deviceMapper, times(1)).deleteById(deviceId);
    }

    @Test
    @DisplayName("更新设备状态 - 成功路径")
    void testUpdateDeviceStatus_Success() {
        // Given
        String deviceId = "device_12345678";
        String newStatus = "ONLINE";
        when(deviceMapper.selectById(deviceId)).thenReturn(testDevice);
        doNothing().when(deviceMapper).updateStatus(deviceId, newStatus);

        // When
        boolean result = deviceService.updateDeviceStatus(deviceId, newStatus);

        // Then
        assertTrue(result);
        verify(deviceMapper, times(1)).selectById(deviceId);
        verify(deviceMapper, times(1)).updateStatus(deviceId, newStatus);
    }
}




