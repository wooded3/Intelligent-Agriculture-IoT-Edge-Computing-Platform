package com.iaiotecp.backend.control;

import com.iaiotecp.backend.control.model.ControlCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class ControlController {

	private final ControlService controlService;

	public ControlController(ControlService controlService) {
		this.controlService = controlService;
	}

	@PostMapping("/actions")
	public ResponseEntity<Void> executeControl(@RequestBody ControlCommand command) {
		controlService.execute(command);
		return ResponseEntity.accepted().build();
	}
}
