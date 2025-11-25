package com.iaiotecp.backend.device.model;

import lombok.Data;
import java.util.List;
import com.iaiotecp.backend.device.model.Device;

@Data
public class DeviceSummary {
    private Long total;
    private List<Device> devices;

    public DeviceSummary() {}

    public DeviceSummary(Long total, List<Device> devices) {
        this.total = total;
        this.devices = devices;
    }
}