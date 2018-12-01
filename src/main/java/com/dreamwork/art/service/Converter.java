package com.dreamwork.art.service;

import com.dreamwork.art.model.Metric;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public interface Converter {
    List<Metric> convert(LinkedHashMap projectData);

    Set<String> types();
}
