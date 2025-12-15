package com.iaiotecp.backend.alert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iaiotecp.backend.alert.model.ActiveAlert;
import com.iaiotecp.backend.alert.model.AlertRule;
import com.iaiotecp.backend.device.model.Result;

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
    public ResponseEntity<Result<List<AlertRule>>> listRules() {
        return ResponseEntity.ok(Result.success(alertService.listRules()));
    }

    /**
     * 创建新的告警规则
     *
     * @param rule 告警规则信息
     * @return 包含操作结果的响应
     */
    @PostMapping("/rules")
    public ResponseEntity<Result<AlertRule>> createRule(@RequestBody AlertRule rule) {
        try {
            return ResponseEntity.ok(Result.success("告警规则创建成功", alertService.createRule(rule)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("创建失败: " + e.getMessage()));
        }
    }

    /**
     * 获取当前活跃告警
     *
     * @return 包含活跃告警列表的响应
     */
    @GetMapping("/current")
    public ResponseEntity<Result<List<ActiveAlert>>> current() {
        return ResponseEntity.ok(Result.success(alertService.listActiveAlerts()));
    }
}
