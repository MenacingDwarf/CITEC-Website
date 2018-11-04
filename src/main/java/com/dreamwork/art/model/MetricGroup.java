package com.dreamwork.art.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MetricGroup {
    @NonNull
    @Getter
    private final Timestamp timestamp;
    @NonNull
    @Getter
    private final List<Metric> metrics;

    public MetricGroup(Timestamp timestamp) {
        this.timestamp = timestamp;
        this.metrics = new ArrayList<>();
    }

    public void addMetric(Metric metric) {
        this.metrics.add(metric);
    }
}
