package com.iaiotecp.backend.alert;

import com.iaiotecp.backend.alert.model.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 告警管理控制器
 * 处理告警相关的HTTP请求
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * 获取告警规则列表
     *
     * @return 包含告警规则列表的响应
     */
    @GetMapping("/rules")
    public ResponseEntity<Map<String, Object>> getAlertRules() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("code", 1);
            response.put("msg", "success");
            response.put("data", alertService.getAlertRules());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("msg", "获取告警规则失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 创建新的告警规则
     *
     * @param rule 告警规则信息
     * @return 包含操作结果的响应
     */
    @PostMapping("/rules")
    public ResponseEntity<Map<String, Object>> createAlertRule(@RequestBody AlertRule rule) {
        Map<String, Object> response = new HashMap<>();
        try {
            alertService.createAlertRule(rule);
            response.put("code", 1);
            response.put("msg", "告警规则创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("msg", "创建告警规则失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取当前活跃告警
     *
     * @return 包含活跃告警列表的响应
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentAlerts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Alert> alerts = alertService.getCurrentActiveAlerts();
            response.put("code", 1);
            response.put("msg", "success");
            response.put("data", alerts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("msg", "获取活跃告警失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}