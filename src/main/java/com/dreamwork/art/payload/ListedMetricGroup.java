package com.dreamwork.art.payload;

import com.dreamwork.art.model.Metric;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ListedMetricGroup {
    private final Timestamp timestamp;

    private final List<Metric> metrics;

    public ListedMetricGroup(Timestamp timestamp) {
        this.timestamp = timestamp;
        this.metrics = new ArrayList<>();
    }

    public void addMetric(Metric metric) {
        this.metrics.add(metric);
    }
}
