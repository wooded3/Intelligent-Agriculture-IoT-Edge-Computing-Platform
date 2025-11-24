package com.iaiotecp.backend.alert;

import com.iaiotecp.backend.alert.model.Alert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

	private final AlertService alertService;

	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}

	@GetMapping
	public List<Alert> listAlerts() {
		return alertService.listAlerts();
	}
}
