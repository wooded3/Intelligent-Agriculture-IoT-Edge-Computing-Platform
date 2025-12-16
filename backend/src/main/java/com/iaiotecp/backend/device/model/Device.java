package com.iaiotecp.backend.device.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Device {
    private String id;
    private String name;
    private String type;      // SENSOR, ACTUATOR, GATEWAY
    private String status;    // ONLINE, OFFLINE, ERROR
    private String classroomId;
    private String config; // JSON 配置字符串（数据库存储）
    private Map<String, Object> configMap; // JSON 配置对象（应用层使用）
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Device() {
    }

    public Device(String id, String name, String type, String classroomId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.classroomId = classroomId;
        this.status = "OFFLINE";
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
