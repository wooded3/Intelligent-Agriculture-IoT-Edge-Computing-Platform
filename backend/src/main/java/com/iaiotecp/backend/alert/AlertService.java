package com.iaiotecp.backend.alert;

import com.iaiotecp.backend.alert.model.ActiveAlert;
import com.iaiotecp.backend.alert.model.AlertRule;

import java.util.List;

public interface AlertService {

    List<AlertRule> listRules();

    AlertRule createRule(AlertRule rule);

    List<ActiveAlert> listActiveAlerts();
}
