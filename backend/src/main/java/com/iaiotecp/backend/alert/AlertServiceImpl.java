package com.iaiotecp.backend.alert;

import com.iaiotecp.backend.alert.model.Alert;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Override
    public List<Alert> listAlerts() {
        // TODO: replace with real implementation
        return Collections.emptyList();
    }
}
