package com.iaiotecp.backend.device;

import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceSummary;
import java.util.List;

public interface DeviceService {

    // 注册设备
    Device registerDevice(String name, String type, String classroomId, String config);

    // 获取设备详情
    Device getDeviceById(String id);

    // 获取设备列表
    DeviceSummary getDeviceList(Integer page, Integer pageSize, String classroomId, String type);

    // 更新设备信息
    Device updateDevice(String id, String name, String type, String classroomId, String config);

    // 删除设备
    boolean deleteDevice(String id);

    // 更新设备状态
    boolean updateDeviceStatus(String id, String status);

    // 获取所有设备（不分页）
    List<Device> getAllDevices();
}