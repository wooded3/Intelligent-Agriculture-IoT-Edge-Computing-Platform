package com.iaiotecp.backend.data.mapper;

import com.iaiotecp.backend.data.model.MetricRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MetricMapper {

    @Insert("INSERT INTO sensor_data (id, device_id, value, unit, timestamp, create_time) " +
            "VALUES (#{id}, #{deviceId}, #{value}, #{unit}, #{timestamp}, NOW())")
    int insert(MetricRecord record);

    @Select("<script>" +
            "SELECT * FROM sensor_data WHERE 1=1 " +
            "<if test='deviceId != null'>AND device_id = #{deviceId}</if> " +
            "<if test='startTime != null'>AND timestamp &gt;= #{startTime}</if> " +
            "<if test='endTime != null'>AND timestamp &lt;= #{endTime}</if> " +
            "ORDER BY timestamp DESC" +
            "</script>")
    @Results({
        @Result(property = "deviceId", column = "device_id"),
        @Result(property = "timestamp", column = "timestamp")
    })
    List<MetricRecord> selectList(@Param("deviceId") String deviceId,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime);

    @Select("SELECT COUNT(*) FROM sensor_data WHERE device_id = #{deviceId}")
    int countByDeviceId(String deviceId);
}





