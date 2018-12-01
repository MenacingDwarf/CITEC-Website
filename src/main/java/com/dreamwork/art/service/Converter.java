package com.dreamwork.art.service;

import com.dreamwork.art.model.Metric;

import java.util.LinkedHashMap;
import java.util.List;

public interface Converter {
    //float convert(LinkedHashMap projectData);

    List<Metric> convert(LinkedHashMap projectData);

    //String type();
}
