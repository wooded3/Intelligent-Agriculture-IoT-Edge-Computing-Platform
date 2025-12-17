package com.iaiotecp.backend.data;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaiotecp.backend.data.model.MetricPage;
import com.iaiotecp.backend.data.model.MetricRecord;

@WebMvcTest(DataController.class)
@DisplayName("数据管理控制器测试")
class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataService dataService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("保存数据接口 - 成功路径")
    void testSaveMetric_Success() throws Exception {
        // Given
        String requestBody = """
            {
                "deviceId": "device_001",
                "value": 25.6,
                "unit": "°C",
                "timestamp": "2024-01-01T10:00:00Z"
            }
            """;

        doNothing().when(dataService).saveMetric(anyString(), anyDouble(), anyString(), anyString());

        // When & Then
        mockMvc.perform(post("/api/data/metrics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    @DisplayName("查询数据接口 - 成功路径")
    void testQueryMetrics_Success() throws Exception {
        // Given
        MetricRecord record = new MetricRecord();
        record.setId("metric_001");
        record.setDeviceId("device_001");
        record.setValue(25.6);
        record.setUnit("°C");

        MetricPage page = new MetricPage(1, java.util.Arrays.asList(record));
        when(dataService.queryMetrics(anyString(), anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/data/metrics")
                .param("deviceId", "device_001")
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.total").value(1));
    }
}




