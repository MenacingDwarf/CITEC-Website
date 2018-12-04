package com.dreamwork.art.service.converters;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.service.Converter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class AnotherMetric implements Converter {
    private Set<String> types = Collections.singleton("another");

    @Override
    public List<Metric> convert(LinkedHashMap projectData) {
        return Collections.singletonList(new Metric("another", (float) Math.random()));
    }

    @Override
    public Set<String> types() {
        return types;
    }
}
