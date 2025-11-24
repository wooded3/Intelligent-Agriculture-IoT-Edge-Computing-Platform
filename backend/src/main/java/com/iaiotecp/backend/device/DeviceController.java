package com.iaiotecp.backend.device;

import com.iaiotecp.backend.device.model.DeviceSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

	private final DeviceService deviceService;

	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	@GetMapping
	public List<DeviceSummary> listDevices() {
		return deviceService.listDevices();
	}
}
