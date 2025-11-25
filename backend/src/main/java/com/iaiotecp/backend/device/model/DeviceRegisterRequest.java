package com.iaiotecp.backend.device.model;

import lombok.Data;

@Data
public class DeviceRegisterRequest {
    private String name;
    private String type;
    private String classroomId;
    private String config;
}