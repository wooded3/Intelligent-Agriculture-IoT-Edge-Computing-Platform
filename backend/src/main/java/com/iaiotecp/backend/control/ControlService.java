package com.iaiotecp.backend.control;

import com.iaiotecp.backend.control.model.ControlCommand;
import java.util.List;

/**
 * 设备控制服务接口
 * 定义设备控制相关的业务逻辑
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
public interface ControlService {

    /**
     * 下发控制指令到设备
     *
     * @param command 控制指令信息
     * @return 保存后的控制指令（包含ID和状态）
     */
    ControlCommand sendCommand(ControlCommand command);

    /**
     * 获取设备的控制指令历史
     *
     * @param deviceId 设备ID
     * @return 控制指令列表
     */
    List<ControlCommand> getCommandHistory(String deviceId);

    /**
     * 获取教室的控制指令历史
     *
     * @param classroomId 教室ID
     * @return 控制指令列表
     */
    List<ControlCommand> getClassroomCommandHistory(String classroomId);

    /**
     * 根据ID查询控制指令
     *
     * @param commandId 指令ID
     * @return 控制指令对象
     */
    ControlCommand getCommandById(String commandId);

    /**
     * 更新指令执行状态
     *
     * @param commandId 指令ID
     * @param status 执行状态（EXECUTED/FAILED）
     */
    void updateCommandStatus(String commandId, String status);
}