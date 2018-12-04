package com.dreamwork.art.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class MetricsBatch {
    private final Timestamp createdAt;
    private final List<List<Metric>> metrics;
    private final int size;

    @Override
    public String toString() {
        return metrics.toString();
    }
}
