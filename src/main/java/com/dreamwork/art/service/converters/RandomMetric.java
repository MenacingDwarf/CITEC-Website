package com.dreamwork.art.service.converters;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.service.Converter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class RandomMetric implements Converter {
    @Override
    public List<Metric> convert(LinkedHashMap projectData) {
        return Collections.singletonList(new Metric("random", (float) Math.random()));
    }
}
