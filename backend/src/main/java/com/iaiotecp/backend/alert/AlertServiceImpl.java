package com.iaiotecp.backend.alert;

import com.iaiotecp.backend.alert.model.ActiveAlert;
import com.iaiotecp.backend.alert.model.AlertRule;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlertServiceImpl implements AlertService {

    private final List<AlertRule> rules = new CopyOnWriteArrayList<>();
    private final List<ActiveAlert> activeAlerts = new CopyOnWriteArrayList<>();
    private final AtomicLong ruleCounter = new AtomicLong(1);
    private final AtomicLong alertCounter = new AtomicLong(1);

    public AlertServiceImpl() {
        // 预置一条示例规则和告警，便于前端联调
        AlertRule sampleRule = new AlertRule(
                "rule_" + ruleCounter.getAndIncrement(),
                "高温告警",
                "GREATER_THAN",
                30.0,
                List.of("device_1", "device_2"),
                true
        );
        rules.add(sampleRule);
        activeAlerts.add(new ActiveAlert(
                "alert_" + alertCounter.getAndIncrement(),
                sampleRule.getName(),
                "温度传感器-101教室",
                31.5,
                sampleRule.getThreshold(),
                Instant.now().toString()
        ));
    }

    @Override
    public List<AlertRule> listRules() {
        return new ArrayList<>(rules);
    }

    @Override
    public AlertRule createRule(AlertRule rule) {
        rule.setId("rule_" + ruleCounter.getAndIncrement());
        rule.setCreateTime(Instant.now().toString());
        rules.add(rule);
        return rule;
    }

    @Override
    public List<ActiveAlert> listActiveAlerts() {
        return new ArrayList<>(activeAlerts);
    }
}
