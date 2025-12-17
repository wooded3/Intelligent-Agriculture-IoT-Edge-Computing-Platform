package com.iaiotecp.backend.alert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.iaiotecp.backend.alert.model.ActiveAlert;
import com.iaiotecp.backend.alert.model.AlertRule;

@SpringBootTest
@DisplayName("告警管理服务测试")
class AlertServiceTest {

    private AlertServiceImpl alertService;

    @BeforeEach
    void setUp() {
        alertService = new AlertServiceImpl();
        // 注意：AlertServiceImpl使用内存存储，每次创建新实例时数据是独立的
    }

    @Test
    @DisplayName("创建告警规则 - 成功路径")
    void testCreateRule_Success() {
        // Given
        AlertRule rule = new AlertRule();
        rule.setName("高温告警");
        rule.setCondition("GREATER_THAN");
        rule.setThreshold(30.0);
        rule.setDeviceIds(List.of("device_001"));
        rule.setEnabled(true);

        // When
        AlertRule result = alertService.createRule(rule);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getId().startsWith("rule_"));
        assertEquals("高温告警", result.getName());
        assertEquals(30.0, result.getThreshold());
        assertTrue(result.isEnabled());
    }

    @Test
    @DisplayName("查询告警规则列表 - 成功路径")
    void testListRules_Success() {
        // Given
        AlertRule rule = new AlertRule();
        rule.setName("测试规则");
        rule.setCondition("GREATER_THAN");
        rule.setThreshold(25.0);
        rule.setDeviceIds(List.of("device_001"));
        rule.setEnabled(true);
        alertService.createRule(rule);

        // When
        List<AlertRule> result = alertService.listRules();

        // Then
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    @DisplayName("告警检测 - 触发告警路径")
    void testCheckAndGenerateAlert_TriggerAlert() {
        // Given
        AlertRule rule = new AlertRule();
        rule.setName("温度告警");
        rule.setCondition("GREATER_THAN");
        rule.setThreshold(30.0);
        rule.setDeviceIds(List.of("device_001"));
        rule.setEnabled(true);
        alertService.createRule(rule);

        int initialAlertCount = alertService.listActiveAlerts().size();

        // When - 上报超过阈值的数据
        alertService.checkAndGenerateAlert("device_001", "温度传感器", "温度", 31.5);

        // Then
        List<ActiveAlert> alerts = alertService.listActiveAlerts();
        assertTrue(alerts.size() > initialAlertCount);
    }

    @Test
    @DisplayName("告警检测 - 不触发告警路径")
    void testCheckAndGenerateAlert_NoTrigger() {
        // Given
        AlertRule rule = new AlertRule();
        rule.setName("温度告警");
        rule.setCondition("GREATER_THAN");
        rule.setThreshold(30.0);
        rule.setDeviceIds(List.of("device_001"));
        rule.setEnabled(true);
        alertService.createRule(rule);

        int initialAlertCount = alertService.listActiveAlerts().size();

        // When - 上报未超过阈值的数据
        alertService.checkAndGenerateAlert("device_001", "温度传感器", "温度", 25.0);

        // Then
        List<ActiveAlert> alerts = alertService.listActiveAlerts();
        assertEquals(initialAlertCount, alerts.size());
    }

    @Test
    @DisplayName("处理告警 - 成功路径")
    void testResolveAlert_Success() {
        // Given
        AlertRule rule = new AlertRule();
        rule.setName("温度告警");
        rule.setCondition("GREATER_THAN");
        rule.setThreshold(30.0);
        rule.setDeviceIds(List.of("device_001"));
        rule.setEnabled(true);
        alertService.createRule(rule);
        alertService.checkAndGenerateAlert("device_001", "温度传感器", "温度", 31.5);

        List<ActiveAlert> alerts = alertService.listActiveAlerts();
        assertTrue(alerts.size() > 0);
        String alertId = alerts.get(0).getId();

        // When
        alertService.resolveAlert(alertId);

        // Then
        List<ActiveAlert> remainingAlerts = alertService.listActiveAlerts();
        assertTrue(remainingAlerts.stream().noneMatch(a -> a.getId().equals(alertId)));
    }
}




