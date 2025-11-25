package com.iaiotecp.backend.device;

import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceSummary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    // 使用内存Map模拟数据库存储
    private final Map<String, Device> deviceStore = new ConcurrentHashMap<>();
    private long deviceCounter = 1;

    @Override
    public Device registerDevice(String name, String type, String classroomId, String config) {
        // 参数验证
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("设备名称不能为空");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("设备类型不能为空");
        }
        if (classroomId == null || classroomId.trim().isEmpty()) {
            throw new IllegalArgumentException("教室ID不能为空");
        }

        // 验证设备类型
        if (!isValidDeviceType(type)) {
            throw new IllegalArgumentException("无效的设备类型: " + type);
        }

        // 创建设备
        String deviceId = "device_" + (deviceCounter++);
        Device device = new Device(deviceId, name, type.toUpperCase(), classroomId);
        device.setConfig(config);

        // 存储设备
        deviceStore.put(deviceId, device);

        System.out.println("设备注册成功: " + deviceId + " - " + name);
        return device;
    }

    @Override
    public Device getDeviceById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }

        Device device = deviceStore.get(id);
        if (device == null) {
            throw new RuntimeException("设备不存在: " + id);
        }

        return device;
    }

    @Override
    public DeviceSummary getDeviceList(Integer page, Integer pageSize, String classroomId, String type) {
        // 设置默认值
        int currentPage = (page == null || page < 1) ? 1 : page;
        int currentPageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        // 过滤设备列表
        List<Device> filteredDevices = deviceStore.values().stream()
                .filter(device -> classroomId == null || classroomId.equals(device.getClassroomId()))
                .filter(device -> type == null || type.equalsIgnoreCase(device.getType()))
                .sorted((d1, d2) -> d2.getCreateTime().compareTo(d1.getCreateTime()))
                .collect(Collectors.toList());

        // 分页处理
        int total = filteredDevices.size();
        int fromIndex = (currentPage - 1) * currentPageSize;
        int toIndex = Math.min(fromIndex + currentPageSize, total);

        List<Device> pageDevices = filteredDevices.subList(fromIndex, toIndex);

        return new DeviceSummary((long) total, pageDevices);
    }

    @Override
    public Device updateDevice(String id, String name, String type, String classroomId, String config) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }

        Device existingDevice = deviceStore.get(id);
        if (existingDevice == null) {
            throw new RuntimeException("设备不存在: " + id);
        }

        // 更新字段
        if (name != null && !name.trim().isEmpty()) {
            existingDevice.setName(name);
        }
        if (type != null && !type.trim().isEmpty()) {
            if (!isValidDeviceType(type)) {
                throw new IllegalArgumentException("无效的设备类型: " + type);
            }
            existingDevice.setType(type.toUpperCase());
        }
        if (classroomId != null && !classroomId.trim().isEmpty()) {
            existingDevice.setClassroomId(classroomId);
        }
        if (config != null) {
            existingDevice.setConfig(config);
        }

        existingDevice.setUpdateTime(LocalDateTime.now());

        System.out.println("设备更新成功: " + id);
        return existingDevice;
    }

    @Override
    public boolean deleteDevice(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }

        Device removedDevice = deviceStore.remove(id);
        if (removedDevice != null) {
            System.out.println("设备删除成功: " + id);
            return true;
        } else {
            throw new RuntimeException("设备不存在: " + id);
        }
    }

    @Override
    public boolean updateDeviceStatus(String id, String status) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("设备状态不能为空");
        }

        if (!isValidDeviceStatus(status)) {
            throw new IllegalArgumentException("无效的设备状态: " + status);
        }

        Device device = deviceStore.get(id);
        if (device == null) {
            throw new RuntimeException("设备不存在: " + id);
        }

        device.setStatus(status.toUpperCase());
        device.setUpdateTime(LocalDateTime.now());

        System.out.println("设备状态更新: " + id + " -> " + status);
        return true;
    }

    @Override
    public List<Device> getAllDevices() {
        return new ArrayList<>(deviceStore.values());
    }

    // 验证设备类型
    private boolean isValidDeviceType(String type) {
        return type != null &&
                (type.equalsIgnoreCase("SENSOR") ||
                        type.equalsIgnoreCase("ACTUATOR") ||
                        type.equalsIgnoreCase("GATEWAY"));
    }

    // 验证设备状态
    private boolean isValidDeviceStatus(String status) {
        return status != null &&
                (status.equalsIgnoreCase("ONLINE") ||
                        status.equalsIgnoreCase("OFFLINE") ||
                        status.equalsIgnoreCase("ERROR"));
    }
}