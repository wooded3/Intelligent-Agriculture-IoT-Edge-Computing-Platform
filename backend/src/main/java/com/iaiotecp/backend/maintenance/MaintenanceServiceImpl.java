package com.iaiotecp.backend.maintenance;

import org.springframework.stereotype.Service;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Override
    public String status() {
        return "ok";
    }
}
