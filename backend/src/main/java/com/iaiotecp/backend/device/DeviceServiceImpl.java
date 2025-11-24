package com.iaiotecp.backend.device;

import com.iaiotecp.backend.device.model.DeviceSummary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Override
    public List<DeviceSummary> listDevices() {
        // TODO: replace with real implementation
        return Collections.emptyList();
    }
}
