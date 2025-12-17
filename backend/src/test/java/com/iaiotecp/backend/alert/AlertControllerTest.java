package com.iaiotecp.backend.alert;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.iaiotecp.backend.alert.model.ActiveAlert;
import com.iaiotecp.backend.alert.model.AlertRule;

@WebMvcTest(AlertController.class)
@DisplayName("告警管理控制器测试")
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertService alertService;

    @Autowired
    private ObjectMapper objectMapper;

    private AlertRule testRule;

    @BeforeEach
    void setUp() {
        testRule = new AlertRule();
        testRule.setId("rule_001");
        testRule.setName("高温告警");
        testRule.setCondition("GREATER_THAN");
        testRule.setThreshold(30.0);
        testRule.setDeviceIds(List.of("device_001"));
        testRule.setEnabled(true);
    }

    @Test
    @DisplayName("创建告警规则接口 - 成功路径")
    void testCreateRule_Success() throws Exception {
        // Given
        when(alertService.createRule(any(AlertRule.class))).thenReturn(testRule);

        // When & Then
        mockMvc.perform(post("/api/alerts/rules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.id").value("rule_001"));
    }

    @Test
    @DisplayName("查询告警规则列表接口 - 成功路径")
    void testListRules_Success() throws Exception {
        // Given
        when(alertService.listRules()).thenReturn(List.of(testRule));

        // When & Then
        mockMvc.perform(get("/api/alerts/rules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("查询活跃告警接口 - 成功路径")
    void testListActiveAlerts_Success() throws Exception {
        // Given
        ActiveAlert alert = new ActiveAlert();
        alert.setId("alert_001");
        alert.setRuleName("高温告警");
        alert.setDeviceName("温度传感器");
        alert.setValue(31.5);
        alert.setThreshold(30.0);

        when(alertService.listActiveAlerts()).thenReturn(List.of(alert));

        // When & Then
        mockMvc.perform(get("/api/alerts/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("处理告警接口 - 成功路径")
    void testResolveAlert_Success() throws Exception {
        // Given
        String alertId = "alert_001";
        doNothing().when(alertService).resolveAlert(alertId);

        // When & Then
        mockMvc.perform(patch("/api/alerts/{alertId}/resolve", alertId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }
}




