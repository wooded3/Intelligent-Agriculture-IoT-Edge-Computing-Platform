package com.iaiotecp.backend.data;

import com.iaiotecp.backend.data.model.MetricPage;
import com.iaiotecp.backend.data.model.MetricRecord;

public interface DataService {

    void saveMetric(MetricRecord record);

    MetricPage queryMetrics(String deviceId, String startTime, String endTime, Integer page, Integer pageSize);
}
