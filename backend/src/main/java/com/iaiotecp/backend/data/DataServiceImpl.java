package com.iaiotecp.backend.data;

import com.iaiotecp.backend.data.model.MetricRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger log = LoggerFactory.getLogger(DataServiceImpl.class);

    @Override
    public void saveMetric(MetricRecord record) {
        // TODO: persist metrics
        log.info("Received metric: deviceId={}, ts={}, temperature={}",
                record.getDeviceId(), record.getTimestamp(), record.getTemperature());
    }
}
