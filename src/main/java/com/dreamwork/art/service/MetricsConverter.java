package com.dreamwork.art.service;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.model.MetricGroup;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MetricsConverter {
    public List<MetricGroup> convert(LinkedHashMap obj) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<MetricGroup> output = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            MetricGroup group = new MetricGroup((long) (i + 1), timestamp);

            group.addMetric(new Metric("type0", (float) Math.random()));
            group.addMetric(new Metric("type1", (float) Math.random()));
            group.addMetric(new Metric("type2", (float) Math.random()));

            output.add(group);
        }

        return output;
    }
}
