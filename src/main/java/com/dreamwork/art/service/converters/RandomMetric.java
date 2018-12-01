package com.dreamwork.art.service.converters;

import com.dreamwork.art.service.SingleValueConverter;

import java.util.LinkedHashMap;

public class RandomMetric implements SingleValueConverter {
    @Override
    public float convert(LinkedHashMap projectData) {
        return (float) Math.random();
    }

    @Override
    public String type() {
        return "random";
    }
}
