package com.iaiotecp.backend.data;

import com.iaiotecp.backend.data.model.MetricPage;
import com.iaiotecp.backend.data.model.MetricRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger log = LoggerFactory.getLogger(DataServiceImpl.class);

    private final List<MetricRecord> records = new CopyOnWriteArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public void saveMetric(MetricRecord record) {
        record.setId("data_" + counter.getAndIncrement());
        if (record.getTimestamp() == null) {
            record.setTimestamp(Instant.now().toString());
        }
        records.add(record);
        log.info("Received metric: deviceId={}, ts={}, value={}, unit={}",
                record.getDeviceId(), record.getTimestamp(), record.getValue(), record.getUnit());
    }

    @Override
    public MetricPage queryMetrics(String deviceId, String startTime, String endTime, Integer page, Integer pageSize) {
        int currentPage = (page == null || page < 1) ? 1 : page;
        int currentPageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        Instant start = parseInstant(startTime);
        Instant end = parseInstant(endTime);

        List<MetricRecord> filtered = records.stream()
                .filter(r -> deviceId == null || deviceId.equals(r.getDeviceId()))
                .filter(r -> {
                    Instant ts = parseInstant(r.getTimestamp());
                    boolean afterStart = start == null || (ts != null && !ts.isBefore(start));
                    boolean beforeEnd = end == null || (ts != null && !ts.isAfter(end));
                    return afterStart && beforeEnd;
                })
                .sorted(Comparator.comparing(MetricRecord::getTimestamp).reversed())
                .collect(Collectors.toList());

        int total = filtered.size();
        int fromIndex = (currentPage - 1) * currentPageSize;
        if (fromIndex >= total) {
            return new MetricPage(total, new ArrayList<>());
        }
        int toIndex = Math.min(fromIndex + currentPageSize, total);
        return new MetricPage(total, filtered.subList(fromIndex, toIndex));
    }

    private Instant parseInstant(String ts) {
        if (ts == null || ts.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(ts);
        } catch (DateTimeParseException e) {
            log.warn("无法解析时间戳: {}", ts);
            return null;
        }
    }
}
