package com.iaiotecp.backend.device;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceSummary;

@Service
public class DeviceServiceImpl implements DeviceService {

    // 使用内存 Map 模拟数据库
    private final Map<String, Device> deviceStore = new ConcurrentHashMap<>();
    private final AtomicLong deviceCounter = new AtomicLong(1);

    @Override
    public Device registerDevice(String name, String type, String classroomId, Map<String, Object> config) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("设备名称不能为空");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("设备类型不能为空");
        }
        if (classroomId == null || classroomId.trim().isEmpty()) {
            throw new IllegalArgumentException("教室ID不能为空");
        }
        if (!isValidDeviceType(type)) {
            throw new IllegalArgumentException("无效的设备类型: " + type);
        }

        String deviceId = "device_" + deviceCounter.getAndIncrement();
        Device device = new Device(deviceId, name, type.toUpperCase(), classroomId);
        device.setConfig(config);

        deviceStore.put(deviceId, device);
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
        int currentPage = (page == null || page < 1) ? 1 : page;
        int currentPageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        List<Device> filteredDevices = deviceStore.values().stream()
                .filter(device -> classroomId == null || classroomId.equals(device.getClassroomId()))
                .filter(device -> type == null || type.equalsIgnoreCase(device.getType()))
                .sorted(Comparator.comparing(Device::getCreateTime).reversed())
                .collect(Collectors.toList());

        int total = filteredDevices.size();
        int fromIndex = (currentPage - 1) * currentPageSize;
        if (fromIndex >= total) {
            return new DeviceSummary((long) total, new ArrayList<>());
        }
        int toIndex = Math.min(fromIndex + currentPageSize, total);
        List<Device> pageDevices = filteredDevices.subList(fromIndex, toIndex);

        return new DeviceSummary((long) total, pageDevices);
    }

    @Override
    public Device updateDevice(String id, String name, String type, String classroomId, Map<String, Object> config) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        Device existingDevice = deviceStore.get(id);
        if (existingDevice == null) {
            throw new RuntimeException("设备不存在: " + id);
        }

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
        return existingDevice;
    }

    @Override
    public boolean deleteDevice(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        Device removedDevice = deviceStore.remove(id);
        if (removedDevice != null) {
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
        return true;
    }

    @Override
    public List<Device> getAllDevices() {
        return new ArrayList<>(deviceStore.values());
    }

    private boolean isValidDeviceType(String type) {
        return type != null &&
                (type.equalsIgnoreCase("SENSOR") ||
                        type.equalsIgnoreCase("ACTUATOR") ||
                        type.equalsIgnoreCase("GATEWAY"));
    }

    private boolean isValidDeviceStatus(String status) {
        return status != null &&
                (status.equalsIgnoreCase("ONLINE") ||
                        status.equalsIgnoreCase("OFFLINE") ||
                        status.equalsIgnoreCase("ERROR"));
    }
}