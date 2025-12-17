package com.iaiotecp.backend.alert.model;

import java.time.Instant;
import java.util.List;

public class AlertRule {
    private String id;
    private String name;
    private String condition; // GREATER_THAN, LESS_THAN, EQUAL
    private Double threshold;
    private List<String> deviceIds;
    private boolean enabled;
    private String createTime;

    public AlertRule() {}

    public AlertRule(String id, String name, String condition, Double threshold, List<String> deviceIds, boolean enabled) {
        this.id = id;
        this.name = name;
        this.condition = condition;
        this.threshold = threshold;
        this.deviceIds = deviceIds;
        this.enabled = enabled;
        this.createTime = Instant.now().toString();
    }

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

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}














