package com.dreamwork.art.service.converters;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.service.Converter;

import java.util.*;

public class RandomMetric implements Converter {
    private Set<String> types = Collections.singleton("random");

    @Override
    public List<Metric> convert(LinkedHashMap projectData) {
        return Collections.singletonList(new Metric("random", (float) Math.random()));
    }

    @Override
    public Set<String> types() {
        return types;
    }
}
