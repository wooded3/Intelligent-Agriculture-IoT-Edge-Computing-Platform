package com.iaiotecp.backend.alert;

import com.iaiotecp.backend.alert.model.ActiveAlert;
import com.iaiotecp.backend.alert.model.AlertRule;
import com.iaiotecp.backend.device.model.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

	private final AlertService alertService;

	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}

	@GetMapping("/rules")
	public ResponseEntity<Result<List<AlertRule>>> listRules() {
		return ResponseEntity.ok(Result.success(alertService.listRules()));
	}

	@PostMapping("/rules")
	public ResponseEntity<Result<AlertRule>> createRule(@RequestBody AlertRule rule) {
		try {
			return ResponseEntity.ok(Result.success("告警规则创建成功", alertService.createRule(rule)));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Result.error("创建失败: " + e.getMessage()));
		}
	}

	@GetMapping("/current")
	public ResponseEntity<Result<List<ActiveAlert>>> current() {
		return ResponseEntity.ok(Result.success(alertService.listActiveAlerts()));
	}
}
