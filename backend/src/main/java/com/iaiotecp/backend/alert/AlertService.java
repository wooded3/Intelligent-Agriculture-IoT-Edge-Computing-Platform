package com.iaiotecp.backend.alert;

import com.iaiotecp.backend.alert.model.Alert;

import java.util.List;

public interface AlertService {

    List<Alert> listAlerts();
}
