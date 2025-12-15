package com.iaiotecp.backend.control.model;

import java.time.LocalDateTime;

/**
 * 控制指令实体类
 * 存储设备控制指令相关信息
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
public class ControlCommand {
    private String id;
    private String deviceId;
    private String deviceName;
    private String commandType; // 如：SWITCH、ADJUST、MODE_CHANGE等
    private String commandParams; // 指令参数，JSON格式字符串
    private String status; // PENDING、EXECUTED、FAILED
    private LocalDateTime createTime;
    private LocalDateTime executeTime;
    private String createdBy;
    private String classroomId;

    // 构造函数
    public ControlCommand() {}

    public ControlCommand(String deviceId, String deviceName, String commandType, 
                         String commandParams, String createdBy, String classroomId) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.commandType = commandType;
        this.commandParams = commandParams;
        this.status = "PENDING";
        this.createTime = LocalDateTime.now();
        this.createdBy = createdBy;
        this.classroomId = classroomId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getCommandParams() {
        return commandParams;
    }

    public void setCommandParams(String commandParams) {
        this.commandParams = commandParams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }
}