package com.dreamwork.art.service;

import java.util.LinkedHashMap;

public interface SingleValueConverter {
    float convert(LinkedHashMap projectData);

    String type();
}
