package com.iaiotecp.backend.device.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Device {
    private String id;
    private String name;
    private String type;  // SENSOR, ACTUATOR, GATEWAY
    private String status; // ONLINE, OFFLINE, ERROR
    private String classroomId;
    private String config; // JSON格式的配置信息
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Device() {}

    public Device(String id, String name, String type, String classroomId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.classroomId = classroomId;
        this.status = "OFFLINE";
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}