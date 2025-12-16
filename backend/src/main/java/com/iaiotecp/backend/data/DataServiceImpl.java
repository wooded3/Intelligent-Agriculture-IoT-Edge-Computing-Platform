package com.iaiotecp.backend.data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iaiotecp.backend.data.mapper.MetricMapper;
import com.iaiotecp.backend.data.model.MetricPage;
import com.iaiotecp.backend.data.model.MetricRecord;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private MetricMapper metricMapper;

    @Override
    @Transactional
    public void saveMetric(String deviceId, Double value, String unit, String timestamp) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        if (value == null) {
            throw new IllegalArgumentException("数值不能为空");
        }

        String id = "metric_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        MetricRecord record = new MetricRecord();
        record.setId(id);
        record.setDeviceId(deviceId);
        record.setValue(value);
        record.setUnit(unit);
        
        if (timestamp != null && !timestamp.trim().isEmpty()) {
            record.setTimestamp(timestamp);
        } else {
            record.setTimestamp(Instant.now().toString());
        }

        metricMapper.insert(record);
    }

    @Override
    public MetricPage queryMetrics(String deviceId, String startTime, String endTime, Integer page, Integer pageSize) {
        int currentPage = (page == null || page < 1) ? 1 : page;
        int currentPageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        PageHelper.startPage(currentPage, currentPageSize);
        List<MetricRecord> records = metricMapper.selectList(deviceId, startTime, endTime);
        PageInfo<MetricRecord> pageInfo = new PageInfo<>(records);

        return new MetricPage(pageInfo.getTotal(), records);
    }

    private Instant parseInstant(String timestamp) {
        if (timestamp == null || timestamp.trim().isEmpty()) {
            return Instant.now();
        }
        try {
            return Instant.parse(timestamp);
        } catch (Exception e) {
            return Instant.now();
        }
    }
}
