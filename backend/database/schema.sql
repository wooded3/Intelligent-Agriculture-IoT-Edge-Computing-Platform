-- 智慧教室物联网系统数据库表结构
-- 数据库名: smart_classroom
-- 字符集: utf8mb4

CREATE DATABASE IF NOT EXISTS smart_classroom DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE smart_classroom;

-- ============================================
-- 1. 设备管理模块
-- ============================================

-- 1.1 设备表
CREATE TABLE IF NOT EXISTS devices (
    id VARCHAR(64) PRIMARY KEY COMMENT '设备ID',
    name VARCHAR(100) NOT NULL COMMENT '设备名称',
    type VARCHAR(20) NOT NULL COMMENT '设备类型: SENSOR, ACTUATOR, GATEWAY',
    status VARCHAR(20) DEFAULT 'OFFLINE' COMMENT '设备状态: ONLINE, OFFLINE, ERROR',
    classroom_id VARCHAR(64) NOT NULL COMMENT '教室ID',
    config TEXT COMMENT '设备配置(JSON格式)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_classroom_id (classroom_id),
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';

-- ============================================
-- 2. 数据管理模块
-- ============================================

-- 2.1 传感器数据表（注意：代码中使用的是 sensor_data 表名）
CREATE TABLE IF NOT EXISTS sensor_data (
    id VARCHAR(64) PRIMARY KEY COMMENT '数据ID',
    device_id VARCHAR(64) NOT NULL COMMENT '设备ID',
    value DECIMAL(10, 2) NOT NULL COMMENT '传感器数值',
    unit VARCHAR(20) COMMENT '数值单位',
    timestamp DATETIME NOT NULL COMMENT '采集时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    INDEX idx_device_id (device_id),
    INDEX idx_timestamp (timestamp),
    INDEX idx_device_timestamp (device_id, timestamp),
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='传感器数据表';

-- ============================================
-- 3. 告警管理模块
-- ============================================

-- 3.1 告警规则表
CREATE TABLE IF NOT EXISTS alert_rules (
    id VARCHAR(64) PRIMARY KEY COMMENT '规则ID',
    name VARCHAR(100) NOT NULL COMMENT '规则名称',
    condition_type VARCHAR(20) NOT NULL COMMENT '条件类型: GREATER_THAN, LESS_THAN, EQUAL',
    threshold DECIMAL(10, 2) NOT NULL COMMENT '阈值',
    device_ids TEXT COMMENT '设备ID列表(JSON数组格式)',
    enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_enabled (enabled),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警规则表';

-- 3.2 活跃告警表
CREATE TABLE IF NOT EXISTS active_alerts (
    id VARCHAR(64) PRIMARY KEY COMMENT '告警ID',
    rule_id VARCHAR(64) COMMENT '规则ID',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    device_id VARCHAR(64) NOT NULL COMMENT '设备ID',
    device_name VARCHAR(100) COMMENT '设备名称',
    value DECIMAL(10, 2) NOT NULL COMMENT '触发值',
    threshold DECIMAL(10, 2) NOT NULL COMMENT '阈值',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '告警状态: ACTIVE-活跃, RESOLVED-已解决',
    timestamp DATETIME NOT NULL COMMENT '触发时间',
    resolve_time DATETIME COMMENT '解决时间',
    INDEX idx_rule_id (rule_id),
    INDEX idx_device_id (device_id),
    INDEX idx_status (status),
    INDEX idx_timestamp (timestamp),
    FOREIGN KEY (rule_id) REFERENCES alert_rules(id) ON DELETE SET NULL,
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活跃告警表';

-- 3.3 告警历史表（可选，用于记录已解决的告警）
CREATE TABLE IF NOT EXISTS alert_history (
    id VARCHAR(64) PRIMARY KEY COMMENT '历史记录ID',
    rule_id VARCHAR(64) COMMENT '规则ID',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    device_id VARCHAR(64) NOT NULL COMMENT '设备ID',
    device_name VARCHAR(100) COMMENT '设备名称',
    value DECIMAL(10, 2) NOT NULL COMMENT '触发值',
    threshold DECIMAL(10, 2) NOT NULL COMMENT '阈值',
    status VARCHAR(20) DEFAULT 'RESOLVED' COMMENT '告警状态',
    trigger_time DATETIME NOT NULL COMMENT '触发时间',
    resolve_time DATETIME COMMENT '解决时间',
    INDEX idx_rule_id (rule_id),
    INDEX idx_device_id (device_id),
    INDEX idx_trigger_time (trigger_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警历史表';

-- ============================================
-- 4. 自动化控制模块
-- ============================================

-- 4.1 自动化规则表
CREATE TABLE IF NOT EXISTS automation_rules (
    id VARCHAR(64) PRIMARY KEY COMMENT '规则ID',
    name VARCHAR(100) NOT NULL COMMENT '规则名称',
    condition_desc TEXT NOT NULL COMMENT '触发条件描述',
    action_desc TEXT NOT NULL COMMENT '执行动作描述',
    enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_enabled (enabled),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自动化规则表';

-- 4.2 控制指令表（用于记录设备控制指令）
CREATE TABLE IF NOT EXISTS control_commands (
    id VARCHAR(64) PRIMARY KEY COMMENT '指令ID',
    device_id VARCHAR(64) NOT NULL COMMENT '设备ID',
    device_name VARCHAR(100) COMMENT '设备名称',
    classroom_id VARCHAR(64) COMMENT '教室ID',
    command_type VARCHAR(50) NOT NULL COMMENT '指令类型: SWITCH, ADJUST, MODE_CHANGE等',
    command_params TEXT COMMENT '指令参数(JSON格式)',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '执行状态: PENDING-待执行, EXECUTED-已执行, FAILED-失败',
    created_by VARCHAR(100) COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    execute_time DATETIME COMMENT '执行时间',
    INDEX idx_device_id (device_id),
    INDEX idx_classroom_id (classroom_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='控制指令表';

-- ============================================
-- 5. 系统维护模块
-- ============================================

-- 5.1 教室表
CREATE TABLE IF NOT EXISTS classrooms (
    id VARCHAR(64) PRIMARY KEY COMMENT '教室ID',
    name VARCHAR(100) NOT NULL COMMENT '教室名称',
    location VARCHAR(200) COMMENT '教室位置',
    capacity INT COMMENT '容纳人数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教室表';

-- ============================================
-- 插入示例数据
-- ============================================

-- 插入示例教室数据
INSERT INTO classrooms (id, name, location, capacity) VALUES
('classroom_101', '101教室', '教学楼A栋1层', 50),
('classroom_102', '102教室', '教学楼A栋1层', 45),
('classroom_201', '201教室', '教学楼A栋2层', 60)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 插入示例设备数据
INSERT INTO devices (id, name, type, status, classroom_id, config) VALUES
('device_001', '温度传感器-101', 'SENSOR', 'ONLINE', 'classroom_101', '{"samplingInterval": 30, "threshold": 28.5}'),
('device_002', '湿度传感器-101', 'SENSOR', 'ONLINE', 'classroom_101', '{"samplingInterval": 30, "threshold": 60.0}'),
('device_003', '空调-101', 'ACTUATOR', 'ONLINE', 'classroom_101', '{"power": "220V", "coolingCapacity": "3.5kW"}'),
('device_004', '温度传感器-102', 'SENSOR', 'OFFLINE', 'classroom_102', '{"samplingInterval": 30, "threshold": 28.5}'),
('device_005', '灯光控制器-101', 'ACTUATOR', 'ONLINE', 'classroom_101', '{"brightness": 80, "autoMode": true}')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 插入示例传感器数据
INSERT INTO sensor_data (id, device_id, value, unit, timestamp) VALUES
('metric_001', 'device_001', 25.6, '°C', NOW() - INTERVAL 10 MINUTE),
('metric_002', 'device_001', 25.8, '°C', NOW() - INTERVAL 9 MINUTE),
('metric_003', 'device_001', 26.0, '°C', NOW() - INTERVAL 8 MINUTE),
('metric_004', 'device_002', 65.5, '%', NOW() - INTERVAL 10 MINUTE),
('metric_005', 'device_002', 66.0, '%', NOW() - INTERVAL 9 MINUTE)
ON DUPLICATE KEY UPDATE value=VALUES(value);

-- 插入示例告警规则
INSERT INTO alert_rules (id, name, condition_type, threshold, device_ids, enabled) VALUES
('rule_001', '高温告警', 'GREATER_THAN', 30.0, '["device_001", "device_004"]', 1),
('rule_002', '低温告警', 'LESS_THAN', 18.0, '["device_001"]', 1),
('rule_003', '湿度过高告警', 'GREATER_THAN', 80.0, '["device_002"]', 1)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 插入示例活跃告警
INSERT INTO active_alerts (id, rule_id, rule_name, device_id, device_name, value, threshold, status, timestamp) VALUES
('alert_001', 'rule_001', '高温告警', 'device_001', '温度传感器-101', 31.5, 30.0, 'ACTIVE', NOW())
ON DUPLICATE KEY UPDATE rule_name=VALUES(rule_name);

-- 插入示例自动化规则
INSERT INTO automation_rules (id, name, condition_desc, action_desc, enabled) VALUES
('auto_001', '自动调光', '光照度 < 300', '打开灯光', 1),
('auto_002', '自动调温', '温度 > 28', '打开空调', 1)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- ============================================
-- 插入示例数据
-- ============================================

-- 插入示例控制指令
INSERT INTO control_commands (id, device_id, device_name, classroom_id, command_type, command_params, status, created_by, execute_time) VALUES
('cmd_001', 'device_003', '空调-101', 'classroom_101', 'MODE_CHANGE', '{"mode": "cool", "temperature": 26}', 'EXECUTED', 'system', NOW() - INTERVAL 5 MINUTE),
('cmd_002', 'device_005', '灯光控制器-101', 'classroom_101', 'ADJUST', '{"brightness": 80}', 'EXECUTED', 'system', NOW() - INTERVAL 3 MINUTE)
ON DUPLICATE KEY UPDATE status=VALUES(status);



