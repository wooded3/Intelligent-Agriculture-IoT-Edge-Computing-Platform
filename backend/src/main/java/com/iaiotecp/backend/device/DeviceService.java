package com.iaiotecp.backend.device;

import com.iaiotecp.backend.device.model.DeviceSummary;

import java.util.List;

public interface DeviceService {

    List<DeviceSummary> listDevices();
}
