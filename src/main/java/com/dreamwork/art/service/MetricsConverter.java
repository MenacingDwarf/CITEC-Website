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

    public MetricsConverter() throws Exception {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Converter.class));

        final Set<BeanDefinition> definitions = provider.findCandidateComponents("com.dreamwork.art.service.converters");
        final Set<String> types = new HashSet<>();

        try {
            this.converters = new ArrayList<>(definitions.size());

            for (BeanDefinition def : definitions) {
                Converter converter = (Converter) Class.forName(def.getBeanClassName()).newInstance();
                this.converters.add(converter);

                Set<String> newTypes = converter.types();
                int preSize = types.size();
                types.addAll(newTypes);

                if (preSize + newTypes.size() != types.size()) {
                    throw new Exception("Types are not unique");
                }
            }
        }

        catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new ReflectiveOperationException();
        }
    }

    public MetricsBatch convert(List projects) {
        List<List<Metric>> out = new ArrayList<>(projects.size());
        int size = 0;

        for (Object o : projects) {
            LinkedHashMap project = (LinkedHashMap)o;

            List<Metric> m = new ArrayList<>(converters.size());

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
