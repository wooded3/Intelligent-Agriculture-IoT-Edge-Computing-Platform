package com.iaiotecp.backend.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.iaiotecp.backend.device.model.Device;

@Mapper
public interface DeviceMapper {

    @Insert("INSERT INTO devices (id, name, type, status, classroom_id, config, create_time, update_time) " +
            "VALUES (#{id}, #{name}, #{type}, #{status}, #{classroomId}, #{config}, #{createTime}, #{updateTime})")
    int insert(Device device);

    @Select("SELECT * FROM devices WHERE id = #{id}")
    @Results({
        @Result(property = "classroomId", column = "classroom_id"),
        @Result(property = "config", column = "config"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    Device selectById(String id);

    @Select("<script>" +
            "SELECT * FROM devices WHERE 1=1 " +
            "<if test='classroomId != null'>AND classroom_id = #{classroomId}</if> " +
            "<if test='type != null'>AND type = #{type}</if> " +
            "ORDER BY create_time DESC" +
            "</script>")
    @Results({
        @Result(property = "classroomId", column = "classroom_id"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    List<Device> selectList(@Param("classroomId") String classroomId, @Param("type") String type);

    @Update("UPDATE devices SET name = #{name}, type = #{type}, classroom_id = #{classroomId}, " +
            "config = #{config}, status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    int update(Device device);

    @Delete("DELETE FROM devices WHERE id = #{id}")
    int deleteById(String id);

    @Update("UPDATE devices SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") String id, @Param("status") String status);

    @Select("SELECT COUNT(*) FROM devices")
    int countAll();

    @Select("SELECT COUNT(*) FROM devices WHERE status = #{status}")
    int countByStatus(String status);

    @Select("SELECT COUNT(*) FROM devices WHERE classroom_id = #{classroomId}")
    int countByClassroomId(String classroomId);
}

