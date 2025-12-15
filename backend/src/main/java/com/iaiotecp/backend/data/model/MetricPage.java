package com.iaiotecp.backend.data.model;

import java.util.List;

public class MetricPage {
    private long total;
    private List<MetricRecord> rows;

    public MetricPage(long total, List<MetricRecord> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<MetricRecord> getRows() {
        return rows;
    }

    public void setRows(List<MetricRecord> rows) {
        this.rows = rows;
    }
}



