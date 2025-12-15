package com.iaiotecp.backend.control;

import com.iaiotecp.backend.control.model.ControlCommand;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 设备控制服务实现类
 * 实现设备控制相关的业务逻辑
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
@Service
public class ControlServiceImpl implements ControlService {

    // 模拟存储控制指令
    private final Map<String, ControlCommand> commands = new ConcurrentHashMap<>();

    @Override
    public ControlCommand sendCommand(ControlCommand command) {
        // 生成指令ID
        String commandId = "cmd_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        command.setId(commandId);
        
        // 设置创建时间
        if (command.getCreateTime() == null) {
            command.setCreateTime(LocalDateTime.now());
        }
        
        // 初始状态为待执行
        command.setStatus("PENDING");
        
        // 存储指令
        commands.put(commandId, command);
        
        // 这里应该有实际的指令下发逻辑，如通过MQTT发送到设备
        // 简化处理：模拟指令发送成功
        simulateCommandExecution(commandId);
        
        return command;
    }

    @Override
    public List<ControlCommand> getCommandHistory(String deviceId) {
        // 从存储中查询指定设备的所有指令，并按时间倒序排列
        return commands.values().stream()
                .filter(cmd -> deviceId.equals(cmd.getDeviceId()))
                .sorted((cmd1, cmd2) -> cmd2.getCreateTime().compareTo(cmd1.getCreateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ControlCommand> getClassroomCommandHistory(String classroomId) {
        // 从存储中查询指定教室的所有指令，并按时间倒序排列
        return commands.values().stream()
                .filter(cmd -> classroomId.equals(cmd.getClassroomId()))
                .sorted((cmd1, cmd2) -> cmd2.getCreateTime().compareTo(cmd1.getCreateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public ControlCommand getCommandById(String commandId) {
        return commands.get(commandId);
    }

    @Override
    public void updateCommandStatus(String commandId, String status) {
        ControlCommand command = commands.get(commandId);
        if (command != null) {
            command.setStatus(status);
            // 如果是执行成功或失败，记录执行时间
            if ("EXECUTED".equals(status) || "FAILED".equals(status)) {
                command.setExecuteTime(LocalDateTime.now());
            }
            commands.put(commandId, command);
        }
    }

    /**
     * 模拟指令执行过程
     * 实际应用中应该是与设备通信的逻辑
     */
    private void simulateCommandExecution(String commandId) {
        // 模拟网络延迟
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 模拟1秒的执行时间
                // 模拟执行成功
                updateCommandStatus(commandId, "EXECUTED");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                updateCommandStatus(commandId, "FAILED");
            }
        }).start();
    }
}