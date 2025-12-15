package com.iaiotecp.backend.alert;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.iaiotecp.backend.alert.model.ActiveAlert;
import com.iaiotecp.backend.alert.model.AlertRule;

/**
 * 告警服务实现类
 * 实现告警相关的业务逻辑
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
@Service
public class AlertServiceImpl implements AlertService {

    private final List<AlertRule> rules = new CopyOnWriteArrayList<>();
    private final List<ActiveAlert> activeAlerts = new CopyOnWriteArrayList<>();
    private final AtomicLong ruleCounter = new AtomicLong(1);
    private final AtomicLong alertCounter = new AtomicLong(1);

    public AlertServiceImpl() {
        // 预置一条示例规则和告警，便于前端联调
        AlertRule sampleRule = new AlertRule(
                "rule_" + ruleCounter.getAndIncrement(),
                "高温告警",
                "GREATER_THAN",
                30.0,
                List.of("device_1", "device_2"),
                true
        );
        rules.add(sampleRule);
        activeAlerts.add(new ActiveAlert(
                "alert_" + alertCounter.getAndIncrement(),
                sampleRule.getName(),
                "温度传感器-101教室",
                31.5,
                sampleRule.getThreshold(),
                Instant.now().toString()
        ));
    }

    @Override
    public List<AlertRule> listRules() {
        return new ArrayList<>(rules);
    }

    @Override
    public AlertRule createRule(AlertRule rule) {
        rule.setId("rule_" + ruleCounter.getAndIncrement());
        rule.setCreateTime(Instant.now().toString());
        rules.add(rule);
        return rule;
    }

    @Override
    public List<ActiveAlert> listActiveAlerts() {
        return new ArrayList<>(activeAlerts);
    }

    @Override
    public void checkAndGenerateAlert(String deviceId, String deviceName, String metricName, double value) {
        // 检查每个规则是否满足告警条件
        for (AlertRule rule : rules) {
            if (rule.isEnabled() && rule.getDeviceIds() != null && rule.getDeviceIds().contains(deviceId) && 
                rule.getName() != null && rule.getName().contains(metricName) && 
                isConditionMet(value, rule.getCondition(), rule.getThreshold())) {
                
                // 生成新的告警
                ActiveAlert alert = new ActiveAlert(
                    "alert_" + alertCounter.getAndIncrement(),
                    rule.getName(),
                    deviceName,
                    value,
                    rule.getThreshold(),
                    Instant.now().toString()
                );
                activeAlerts.add(alert);
                
                // 可以在这里添加通知逻辑，如发送消息给管理员
            }
        }
    }

    @Override
    public void resolveAlert(String alertId) {
        // 从活跃告警列表中移除已解决的告警
        activeAlerts.removeIf(alert -> alertId.equals(alert.getId()));
    }

    /**
     * 检查数值是否满足告警条件
     *
     * @param value 实际值
     * @param condition 条件
     * @param threshold 阈值
     * @return 是否满足条件
     */
    private boolean isConditionMet(double value, String condition, double threshold) {
        switch (condition) {
            case "GREATER_THAN":
                return value > threshold;
            case "LESS_THAN":
                return value < threshold;
            case "EQUAL":
                return value == threshold;
            default:
                return false;
        }
    }
}