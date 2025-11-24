package com.iaiotecp.backend.maintenance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

	private final MaintenanceService maintenanceService;

	public MaintenanceController(MaintenanceService maintenanceService) {
		this.maintenanceService = maintenanceService;
	}

	@GetMapping("/status")
	public String status() {
		return maintenanceService.status();
	}
}
