package com.iaiotecp.backend.device;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceRegisterRequest;
import com.iaiotecp.backend.device.model.DeviceSummary;

@WebMvcTest(DeviceController.class)
@DisplayName("设备管理控制器测试")
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Autowired
    private ObjectMapper objectMapper;

    private Device testDevice;

    @BeforeEach
    void setUp() {
        testDevice = new Device();
        testDevice.setId("device_12345678");
        testDevice.setName("测试设备");
        testDevice.setType("SENSOR");
        testDevice.setStatus("ONLINE");
        testDevice.setClassroomId("classroom_101");
        testDevice.setCreateTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("注册设备接口 - 成功路径")
    void testRegisterDevice_Success() throws Exception {
        // Given
        DeviceRegisterRequest request = new DeviceRegisterRequest();
        request.setName("温度传感器");
        request.setType("SENSOR");
        request.setClassroomId("classroom_101");

        when(deviceService.registerDevice(anyString(), anyString(), anyString(), any()))
            .thenReturn(testDevice);

        // When & Then
        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.id").value("device_12345678"));
    }

    @Test
    @DisplayName("查询设备列表接口 - 成功路径")
    void testGetDeviceList_Success() throws Exception {
        // Given
        DeviceSummary summary = new DeviceSummary(1L, Arrays.asList(testDevice));
        when(deviceService.getDeviceList(anyInt(), anyInt(), any(), any()))
            .thenReturn(summary);

        // When & Then
        mockMvc.perform(get("/api/devices")
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.total").value(1));
    }

    @Test
    @DisplayName("查询设备详情接口 - 成功路径")
    void testGetDeviceById_Success() throws Exception {
        // Given
        String deviceId = "device_12345678";
        when(deviceService.getDeviceById(deviceId)).thenReturn(testDevice);

        // When & Then
        mockMvc.perform(get("/api/devices/{id}", deviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.id").value(deviceId));
    }

    @Test
    @DisplayName("删除设备接口 - 成功路径")
    void testDeleteDevice_Success() throws Exception {
        // Given
        String deviceId = "device_12345678";
        doNothing().when(deviceService).deleteDevice(deviceId);

        // When & Then
        mockMvc.perform(delete("/api/devices/{id}", deviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }
}




