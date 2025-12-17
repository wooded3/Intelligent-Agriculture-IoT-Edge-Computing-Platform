package com.iaiotecp.backend.data;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iaiotecp.backend.data.mapper.MetricMapper;
import com.iaiotecp.backend.data.model.MetricPage;
import com.iaiotecp.backend.data.model.MetricRecord;

@ExtendWith(MockitoExtension.class)
@DisplayName("数据管理服务测试")
class DataServiceTest {

    @Mock
    private MetricMapper metricMapper;

    @InjectMocks
    private DataServiceImpl dataService;

    private MetricRecord testRecord;

    @BeforeEach
    void setUp() {
        testRecord = new MetricRecord();
        testRecord.setId("metric_12345678");
        testRecord.setDeviceId("device_001");
        testRecord.setValue(25.6);
        testRecord.setUnit("°C");
        testRecord.setTimestamp("2024-01-01T10:00:00Z");
    }

    @Test
    @DisplayName("保存数据 - 成功路径")
    void testSaveMetric_Success() {
        // Given
        String deviceId = "device_001";
        Double value = 25.6;
        String unit = "°C";
        String timestamp = "2024-01-01T10:00:00Z";

        doNothing().when(metricMapper).insert(any(MetricRecord.class));

        // When
        assertDoesNotThrow(() -> {
            dataService.saveMetric(deviceId, value, unit, timestamp);
        });

        // Then
        verify(metricMapper, times(1)).insert(any(MetricRecord.class));
    }

    @Test
    @DisplayName("保存数据 - 异常路径：设备ID为空")
    void testSaveMetric_EmptyDeviceId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> dataService.saveMetric("", 25.6, "°C", null)
        );
        assertEquals("设备ID不能为空", exception.getMessage());
        verify(metricMapper, never()).insert(any(MetricRecord.class));
    }

    @Test
    @DisplayName("保存数据 - 异常路径：数值为空")
    void testSaveMetric_NullValue() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> dataService.saveMetric("device_001", null, "°C", null)
        );
        assertEquals("数值不能为空", exception.getMessage());
        verify(metricMapper, never()).insert(any(MetricRecord.class));
    }

    @Test
    @DisplayName("查询数据 - 成功路径")
    void testQueryMetrics_Success() {
        // Given
        String deviceId = "device_001";
        List<MetricRecord> records = Arrays.asList(testRecord);
        when(metricMapper.selectList(deviceId, null, null)).thenReturn(records);

        // When
        MetricPage result = dataService.queryMetrics(deviceId, null, null, 1, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRows().size());
        verify(metricMapper, times(1)).selectList(deviceId, null, null);
    }

    @Test
    @DisplayName("查询数据 - 分页参数默认值")
    void testQueryMetrics_DefaultPagination() {
        // Given
        List<MetricRecord> records = Arrays.asList(testRecord);
        when(metricMapper.selectList(null, null, null)).thenReturn(records);

        // When
        MetricPage result = dataService.queryMetrics(null, null, null, null, null);

        // Then
        assertNotNull(result);
        verify(metricMapper, times(1)).selectList(null, null, null);
    }
}




