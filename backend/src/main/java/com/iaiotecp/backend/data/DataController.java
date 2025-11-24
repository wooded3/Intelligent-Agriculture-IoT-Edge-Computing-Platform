package com.iaiotecp.backend.data;

import com.iaiotecp.backend.data.model.MetricRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {

	private final DataService dataService;

	public DataController(DataService dataService) {
		this.dataService = dataService;
	}

	@PostMapping("/metrics")
	public ResponseEntity<Void> receiveMetric(@RequestBody MetricRecord record) {
		dataService.saveMetric(record);
		return ResponseEntity.accepted().build();
	}
}
