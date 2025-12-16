package com.iaiotecp.backend.data;

import com.iaiotecp.backend.data.model.MetricPage;
import com.iaiotecp.backend.data.model.MetricRecord;
import com.iaiotecp.backend.device.model.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class DataController {

	private final DataService dataService;

	public DataController(DataService dataService) {
		this.dataService = dataService;
	}

	@PostMapping("/sensor-data")
	public ResponseEntity<Result<Void>> receiveMetric(@RequestBody MetricRecord record) {
		try {
			dataService.saveMetric(record.getDeviceId(), record.getValue(), record.getUnit(), record.getTimestamp());
			return ResponseEntity.ok(Result.success(null));
		} catch (Exception e) {
			return ResponseEntity.ok(Result.error("数据接收失败: " + e.getMessage()));
		}
	}

	@GetMapping("/sensor-data")
	public ResponseEntity<Result<Map<String, Object>>> queryMetrics(
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		try {
			MetricPage pageResult = dataService.queryMetrics(deviceId, startTime, endTime, page, pageSize);
			Map<String, Object> payload = new HashMap<>();
			payload.put("total", pageResult.getTotal());
			payload.put("rows", pageResult.getRows());
			return ResponseEntity.ok(Result.success(payload));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Result.error("查询失败: " + e.getMessage()));
		}
	}
}
