package com.iaiotecp.backend.alert.model;

public class ActiveAlert {
    private String id;
    private String ruleName;
    private String deviceName;
    private Double value;
    private Double threshold;
    private String timestamp;

    public ActiveAlert() {}

    public ActiveAlert(String id, String ruleName, String deviceName, Double value, Double threshold, String timestamp) {
        this.id = id;
        this.ruleName = ruleName;
        this.deviceName = deviceName;
        this.value = value;
        this.threshold = threshold;
        this.timestamp = timestamp;
    }

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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}














