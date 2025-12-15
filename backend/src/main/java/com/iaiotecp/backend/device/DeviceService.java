package com.iaiotecp.backend.device;

import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceSummary;

import java.util.List;
import java.util.Map;

public interface DeviceService {

    Device registerDevice(String name, String type, String classroomId, Map<String, Object> config);

    Device getDeviceById(String id);

    DeviceSummary getDeviceList(Integer page, Integer pageSize, String classroomId, String type);

    Device updateDevice(String id, String name, String type, String classroomId, Map<String, Object> config);

    boolean deleteDevice(String id);

    boolean updateDeviceStatus(String id, String status);

    List<Device> getAllDevices();
}