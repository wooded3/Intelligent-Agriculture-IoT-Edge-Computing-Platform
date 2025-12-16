package com.iaiotecp.backend.device;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iaiotecp.backend.device.mapper.DeviceMapper;
import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceSummary;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
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

        String deviceId = "device_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Device device = new Device(deviceId, name, type.toUpperCase(), classroomId);
        device.setConfigMap(config);
        if (config != null) {
            try {
                device.setConfig(objectMapper.writeValueAsString(config));
            } catch (Exception e) {
                device.setConfig("{}");
            }
        } else {
            device.setConfig("{}");
        }
        device.setStatus("OFFLINE");
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());

        deviceMapper.insert(device);
        return device;
    }

    @Override
    public Device getDeviceById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new RuntimeException("设备不存在: " + id);
        }
        // 解析config JSON字符串为Map
        if (device.getConfig() != null && !device.getConfig().isEmpty()) {
            try {
                device.setConfigMap(objectMapper.readValue(device.getConfig(), Map.class));
            } catch (Exception e) {
                device.setConfigMap(null);
            }
        }
        return device;
    }

    @Override
    public DeviceSummary getDeviceList(Integer page, Integer pageSize, String classroomId, String type) {
        int currentPage = (page == null || page < 1) ? 1 : page;
        int currentPageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        PageHelper.startPage(currentPage, currentPageSize);
        List<Device> devices = deviceMapper.selectList(classroomId, type);
        // 解析每个设备的config
        devices.forEach(d -> {
            if (d.getConfig() != null && !d.getConfig().isEmpty()) {
                try {
                    d.setConfigMap(objectMapper.readValue(d.getConfig(), Map.class));
                } catch (Exception e) {
                    d.setConfigMap(null);
                }
            }
        });
        PageInfo<Device> pageInfo = new PageInfo<>(devices);

        return new DeviceSummary(pageInfo.getTotal(), devices);
    }

    @Override
    @Transactional
    public Device updateDevice(String id, String name, String type, String classroomId, Map<String, Object> config) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        Device existingDevice = deviceMapper.selectById(id);
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
            existingDevice.setConfigMap(config);
            try {
                existingDevice.setConfig(objectMapper.writeValueAsString(config));
            } catch (Exception e) {
                existingDevice.setConfig("{}");
            }
        }
        existingDevice.setUpdateTime(LocalDateTime.now());

        deviceMapper.update(existingDevice);
        return existingDevice;
    }

    @Override
    @Transactional
    public boolean deleteDevice(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new RuntimeException("设备不存在: " + id);
        }
        deviceMapper.deleteById(id);
        return true;
    }

    @Override
    @Transactional
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
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new RuntimeException("设备不存在: " + id);
        }
        deviceMapper.updateStatus(id, status.toUpperCase());
        return true;
    }

    @Override
    public List<Device> getAllDevices() {
        List<Device> devices = deviceMapper.selectList(null, null);
        devices.forEach(d -> {
            if (d.getConfig() != null && !d.getConfig().isEmpty()) {
                try {
                    d.setConfigMap(objectMapper.readValue(d.getConfig(), Map.class));
                } catch (Exception e) {
                    d.setConfigMap(null);
                }
            }
        });
        return devices;
    }

    public int getDeviceCount() {
        return deviceMapper.countAll();
    }

    public int getDeviceCountByStatus(String status) {
        return deviceMapper.countByStatus(status);
    }

    public int getDeviceCountByClassroomId(String classroomId) {
        return deviceMapper.countByClassroomId(classroomId);
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
