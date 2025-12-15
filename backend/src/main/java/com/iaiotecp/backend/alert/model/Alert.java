package com.iaiotecp.backend.alert.model;

import java.time.LocalDateTime;

/**
 * 告警实体类
 * 存储告警相关信息
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
public class Alert {
    private String id;
    private String ruleName;
    private String deviceId;
    private String deviceName;
    private double value;
    private double threshold;
    private LocalDateTime timestamp;
    private boolean isResolved;

    // 构造函数
    public Alert() {}

    public Alert(String ruleName, String deviceId, String deviceName, 
                double value, double threshold, LocalDateTime timestamp) {
        this.ruleName = ruleName;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.value = value;
        this.threshold = threshold;
        this.timestamp = timestamp;
        this.isResolved = false;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }
}