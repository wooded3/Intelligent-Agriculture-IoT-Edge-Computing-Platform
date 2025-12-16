package com.iaiotecp.backend.data;

import com.iaiotecp.backend.data.model.MetricPage;
import com.iaiotecp.backend.data.model.MetricRecord;

public interface DataService {

    void saveMetric(String deviceId, Double value, String unit, String timestamp);

    MetricPage queryMetrics(String deviceId, String startTime, String endTime, Integer page, Integer pageSize);
}
