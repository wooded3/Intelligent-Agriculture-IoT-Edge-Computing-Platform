package com.iaiotecp.backend.device;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceRegisterRequest;
import com.iaiotecp.backend.device.model.DeviceSummary;
import com.iaiotecp.backend.device.model.Result;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<Result<DeviceSummary>> getDeviceList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String classroomId,
            @RequestParam(required = false) String type) {
        try {
            DeviceSummary summary = deviceService.getDeviceList(page, pageSize, classroomId, type);
            return ResponseEntity.ok(Result.success(summary));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error(e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Result<Map<String, Object>>> getDeviceStats() {
        try {
            DeviceServiceImpl serviceImpl = (DeviceServiceImpl) deviceService;
            Map<String, Object> stats = Map.of(
                "total", serviceImpl.getDeviceCount(),
                "online", serviceImpl.getDeviceCountByStatus("ONLINE"),
                "offline", serviceImpl.getDeviceCountByStatus("OFFLINE"),
                "error", serviceImpl.getDeviceCountByStatus("ERROR")
            );
            return ResponseEntity.ok(Result.success(stats));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<Device>> getDeviceById(@PathVariable String id) {
        try {
            Device device = deviceService.getDeviceById(id);
            return ResponseEntity.ok(Result.success(device));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Result<Device>> registerDevice(@RequestBody DeviceRegisterRequest request) {
        try {
            Device device = deviceService.registerDevice(
                    request.getName(),
                    request.getType(),
                    request.getClassroomId(),
                    request.getConfig()
            );
            return ResponseEntity.ok(Result.success(device));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<Device>> updateDevice(
            @PathVariable String id,
            @RequestBody DeviceRegisterRequest request) {
        try {
            Device device = deviceService.updateDevice(
                    id,
                    request.getName(),
                    request.getType(),
                    request.getClassroomId(),
                    request.getConfig()
            );
            return ResponseEntity.ok(Result.success(device));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deleteDevice(@PathVariable String id) {
        try {
            deviceService.deleteDevice(id);
            return ResponseEntity.ok(Result.success(null));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Result<Void>> updateDeviceStatus(
            @PathVariable String id,
            @RequestParam String status) {
        try {
            deviceService.updateDeviceStatus(id, status);
            return ResponseEntity.ok(Result.success(null));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.error(e.getMessage()));
        }
    }
}
