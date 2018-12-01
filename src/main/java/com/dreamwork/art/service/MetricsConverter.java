package com.dreamwork.art.service;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.model.MetricGroup;
import com.dreamwork.art.model.MetricsBatch;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MetricsConverter {
    private final List<SingleValueConverter> converters;

    public MetricsConverter() throws ReflectiveOperationException {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(SingleValueConverter.class));

        final Set<BeanDefinition> definitions = provider.findCandidateComponents("com.dreamwork.art.service.converters");

        try {
            this.converters = new ArrayList<>(definitions.size());

            for (BeanDefinition def : definitions) {
                SingleValueConverter converter = (SingleValueConverter) Class.forName(def.getBeanClassName()).newInstance();
                this.converters.add(converter);
            }
        }

        catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new ReflectiveOperationException();
        }
    }

    public int numberOfMetrics() {
        return converters.size();
    }

    public MetricsBatch convert(LinkedHashMap response) {
        Map data = (Map)response.get("data");

        //Map limit = (Map)data.get("rateLimit");

        List projects = (List)data.get("nodes");

        return batch(projects);
    }

    private MetricsBatch batch(List projects) {
        List<List<Metric>> out = new ArrayList<>(projects.size());

        for (Object o : projects) {
            LinkedHashMap project = (LinkedHashMap)o;

            List<Metric> m = new ArrayList<>(numberOfMetrics());

            for (SingleValueConverter converter : converters) {
                m.add(new Metric(converter.type(), converter.convert(project)));
            }

            out.add(m);
        }

        return new MetricsBatch(new Timestamp(System.currentTimeMillis()), out);
    }
}
