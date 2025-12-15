package com.iaiotecp.backend.control;

import com.iaiotecp.backend.control.model.ControlCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备控制控制器
 * 处理设备控制相关的HTTP请求
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/v1/controls")
public class ControlController {

    private final ControlService controlService;

    @Autowired
    public ControlController(ControlService controlService) {
        this.controlService = controlService;
    }

    /**
     * 下发新的控制指令
     *
     * @param command 控制指令信息
     * @return 包含操作结果的响应
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> sendCommand(@RequestBody ControlCommand command) {
        Map<String, Object> response = new HashMap<>();
        try {
            ControlCommand result = controlService.sendCommand(command);
            response.put("code", 1);
            response.put("msg", "控制指令下发成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("msg", "控制指令下发失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 查询设备的控制指令历史
     *
     * @param deviceId 设备ID
     * @return 包含指令历史的响应
     */
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getCommandHistory(@RequestParam String deviceId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ControlCommand> history = controlService.getCommandHistory(deviceId);
            response.put("code", 1);
            response.put("msg", "success");
            response.put("data", history);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("msg", "查询指令历史失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 查询教室的控制指令历史
     *
     * @param classroomId 教室ID
     * @return 包含指令历史的响应
     */
    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<Map<String, Object>> getClassroomCommandHistory(@PathVariable String classroomId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ControlCommand> history = controlService.getClassroomCommandHistory(classroomId);
            response.put("code", 1);
            response.put("msg", "success");
            response.put("data", history);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("msg", "查询教室指令历史失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 查询指令执行状态
     *
     * @param commandId 指令ID
     * @return 包含指令状态的响应
     */
    @GetMapping("/{commandId}")
    public ResponseEntity<Map<String, Object>> getCommandStatus(@PathVariable String commandId) {
        Map<String, Object> response = new HashMap<>();
        try {
            ControlCommand command = controlService.getCommandById(commandId);
            if (command == null) {
                response.put("code", 0);
                response.put("msg", "指令不存在");
                return ResponseEntity.notFound().build();
            }
            
            response.put("code", 1);
            response.put("msg", "success");
            response.put("data", command);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("msg", "查询指令状态失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}