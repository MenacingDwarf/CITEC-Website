package com.dreamwork.art.service;

import com.dreamwork.art.model.MetricGroup;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MetricsConverter {
    public List<MetricGroup> convert(LinkedHashMap obj) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        System.out.println(obj);

        return null;
    }
}
