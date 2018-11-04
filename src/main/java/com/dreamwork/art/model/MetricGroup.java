package com.dreamwork.art.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class MetricGroup {
    @NonNull
    private Timestamp timestamp;
    @NonNull
    private List<Metric> metrics;
}
