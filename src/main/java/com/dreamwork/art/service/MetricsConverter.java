package com.dreamwork.art.service;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.model.MetricsBatch;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MetricsConverter {
    private final List<Converter> converters;

    public MetricsConverter() throws ReflectiveOperationException {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Converter.class));

        final Set<BeanDefinition> definitions = provider.findCandidateComponents("com.dreamwork.art.service.converters");

        try {
            this.converters = new ArrayList<>(definitions.size());

            for (BeanDefinition def : definitions) {
                Converter converter = (Converter) Class.forName(def.getBeanClassName()).newInstance();
                this.converters.add(converter);
            }
        }

        catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new ReflectiveOperationException();
        }
    }

    public MetricsBatch convert(LinkedHashMap response) {
        Map data = (Map)response.get("data");

        //Map limit = (Map)data.get("rateLimit");

        List projects = (List)data.get("nodes");

        return batch(projects);
    }

    private MetricsBatch batch(List projects) {
        List<List<Metric>> out = new ArrayList<>(projects.size());
        int size = 0;

        for (Object o : projects) {
            LinkedHashMap project = (LinkedHashMap)o;

            List<Metric> m = new ArrayList<>();

            for (Converter converter : converters) {
                List<Metric> metrics = converter.convert(project);
                m.addAll(metrics);
                size += metrics.size();
            }

            out.add(m);
        }

        return new MetricsBatch(new Timestamp(System.currentTimeMillis()), out, size);
    }
}
