package com.dreamwork.art.model;

import lombok.Getter;
import lombok.NonNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MetricGroup {
    @NonNull
    @Getter
    private final Long projectId;
    @NonNull
    @Getter
    private final List<Metric> metrics;

    public MetricGroup(Long projectId, Timestamp timestamp) {
        this.projectId = projectId;
        this.metrics = new ArrayList<>();
    }

    public void addMetric(Metric metric) {
        this.metrics.add(metric);
    }

    public Metric getMetric(int i) {
        return this.metrics.get(i);
    }
}
