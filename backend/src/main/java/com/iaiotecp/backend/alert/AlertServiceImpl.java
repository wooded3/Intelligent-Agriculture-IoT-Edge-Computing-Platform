package com.iaiotecp.backend.alert;

import com.iaiotecp.backend.alert.model.Alert;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    // 模拟存储告警规则
    private final List<AlertRule> alertRules = new ArrayList<>();
    
    // 模拟存储告警记录
    private final ConcurrentHashMap<String, Alert> alerts = new ConcurrentHashMap<>();

    @Override
    public List<AlertRule> getAlertRules() {
        // 实际应用中应该从数据库获取
        return new ArrayList<>(alertRules);
    }

    @Override
    public void createAlertRule(AlertRule rule) {
        // 生成规则ID
        rule.setId(UUID.randomUUID().toString());
        rule.setCreateTime(LocalDateTime.now());
        alertRules.add(rule);
    }

    @Override
    public List<Alert> getCurrentActiveAlerts() {
        // 过滤出未解决的告警
        List<Alert> activeAlerts = new ArrayList<>();
        for (Alert alert : alerts.values()) {
            if (!alert.isResolved()) {
                activeAlerts.add(alert);
            }
        }
        return activeAlerts;
    }

    @Override
    public void checkAndGenerateAlert(String deviceId, String deviceName, String metricName, double value) {
        // 检查每个规则是否满足告警条件
        for (AlertRule rule : alertRules) {
            if (rule.isEnabled() && rule.getDeviceIds().contains(deviceId) && 
                rule.getName().contains(metricName) && isConditionMet(value, rule.getCondition(), rule.getThreshold())) {
                
                // 生成新的告警
                Alert alert = new Alert(
                    rule.getName(),
                    deviceId,
                    deviceName,
                    value,
                    rule.getThreshold(),
                    LocalDateTime.now()
                );
                alert.setId(UUID.randomUUID().toString());
                alerts.put(alert.getId(), alert);
                
                // 可以在这里添加通知逻辑，如发送消息给管理员
            }
        }
    }

    @Override
    public void resolveAlert(String alertId) {
        Alert alert = alerts.get(alertId);
        if (alert != null) {
            alert.setResolved(true);
        }
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