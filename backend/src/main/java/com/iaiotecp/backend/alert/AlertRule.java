package com.iaiotecp.backend.alert;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 告警规则模型类
 * 存储告警规则相关信息
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
public class AlertRule {
    private String id;
    private String name;
    private String condition; // GREATER_THAN, LESS_THAN, EQUAL
    private double threshold;
    private List<String> deviceIds;
    private boolean enabled;
    private LocalDateTime createTime;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}