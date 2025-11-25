package com.iaiotecp.backend.device;

import com.iaiotecp.backend.device.model.Device;
import com.iaiotecp.backend.device.model.DeviceSummary;
import com.iaiotecp.backend.device.model.Result;
import com.iaiotecp.backend.device.model.DeviceRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
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
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Result<Device>> registerDevice(@RequestBody DeviceRegisterRequest request) {
        try {
            Device device = deviceService.registerDevice(request.getName(), request.getType(), request.getClassroomId(), request.getConfig());
            return ResponseEntity.ok(Result.success("设备注册成功", device));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("设备注册失败: " + e.getMessage()));
        }
    }

    // 其他方法类似修改...
}