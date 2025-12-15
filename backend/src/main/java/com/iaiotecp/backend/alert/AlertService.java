package com.iaiotecp.backend.alert;

import java.util.List;

import com.iaiotecp.backend.alert.model.ActiveAlert;
import com.iaiotecp.backend.alert.model.AlertRule;

/**
 * 告警服务接口
 * 定义告警相关的业务逻辑
 *
 * @author 开发者姓名
 * @version 1.0
 * @since 2024-01-01
 */
public interface AlertService {

    /**
     * 获取所有告警规则
     *
     * @return 告警规则列表
     */
    List<AlertRule> listRules();

    /**
     * 创建新的告警规则
     *
     * @param rule 告警规则信息
     * @return 创建后的告警规则（包含ID和创建时间）
     */
    AlertRule createRule(AlertRule rule);

    /**
     * 获取当前活跃的告警
     *
     * @return 活跃告警列表
     */
    List<ActiveAlert> listActiveAlerts();

    /**
     * 检查并生成新的告警
     * 根据设备数据和告警规则判断是否需要生成告警
     *
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param metricName 指标名称
     * @param value 指标值
     */
    void checkAndGenerateAlert(String deviceId, String deviceName, String metricName, double value);

    /**
     * 标记告警为已解决
     *
     * @param alertId 告警ID
     */
    void resolveAlert(String alertId);
}
