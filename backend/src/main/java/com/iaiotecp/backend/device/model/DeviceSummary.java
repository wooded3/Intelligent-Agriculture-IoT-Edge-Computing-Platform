package com.iaiotecp.backend.device.model;

import java.util.List;

public class DeviceSummary {
    private Long total;
    private List<Device> rows;

    public DeviceSummary() {
    }

    public DeviceSummary(Long total, List<Device> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Device> getRows() {
        return rows;
    }

    public void setRows(List<Device> rows) {
        this.rows = rows;
    }
}