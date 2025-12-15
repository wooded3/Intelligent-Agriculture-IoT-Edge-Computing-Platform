package com.iaiotecp.backend.device;

import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceRegisterRequest;
import com.iaiotecp.backend.device.model.DeviceSummary;
import com.iaiotecp.backend.device.model.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public ResponseEntity<Result<DeviceSummary>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String classroomId,
            @RequestParam(required = false) String type) {
        try {
            DeviceSummary summary = deviceService.getDeviceList(page, pageSize, classroomId, type);
            return ResponseEntity.ok(Result.success(summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<Device>> detail(@PathVariable String id) {
        try {
            return ResponseEntity.ok(Result.success(deviceService.getDeviceById(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Result<Device>> register(@RequestBody DeviceRegisterRequest request) {
        try {
            Device device = deviceService.registerDevice(
                    request.getName(),
                    request.getType(),
                    request.getClassroomId(),
                    request.getConfig());
            return ResponseEntity.ok(Result.success("设备注册成功", device));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("设备注册失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<Device>> update(@PathVariable String id,
                                                 @RequestBody DeviceRegisterRequest request) {
        try {
            Device device = deviceService.updateDevice(
                    id,
                    request.getName(),
                    request.getType(),
                    request.getClassroomId(),
                    request.getConfig());
            return ResponseEntity.ok(Result.success("设备更新成功", device));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("设备更新失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable String id) {
        try {
            deviceService.deleteDevice(id);
            return ResponseEntity.ok(Result.success("设备删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("设备删除失败: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Result<Void>> updateStatus(@PathVariable String id,
                                                     @RequestParam String status) {
        try {
            deviceService.updateDeviceStatus(id, status);
            return ResponseEntity.ok(Result.success("状态更新成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("状态更新失败: " + e.getMessage()));
        }
    }
}