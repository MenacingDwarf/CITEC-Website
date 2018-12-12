package com.dreamwork.art.service;

import com.dreamwork.art.controllers.GithubApiCaller;
import com.dreamwork.art.model.Metric;
import com.dreamwork.art.model.MetricsBatch;
import com.dreamwork.art.repository.MetricsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MetricsConverter {
    private final static Logger logger = LoggerFactory.getLogger(GithubApiCaller.class);

    private ClassPathScanningCandidateComponentProvider provider;
    private List<Converter> converters;
    private MetricsRepo metricsRepo;
    private Set<String> currBeans;

    @Autowired
    public MetricsConverter(MetricsRepo metricsRepo) throws Exception {
        this.provider = new ClassPathScanningCandidateComponentProvider(false);
        this.provider.addIncludeFilter(new AssignableTypeFilter(Converter.class));
        this.converters = new ArrayList<>();
        this.metricsRepo = metricsRepo;
        this.currBeans = new HashSet<>();
    }

    @Scheduled(fixedDelayString = "${metricsapi.update}")
    public void updateMetricConverters() throws ReflectiveOperationException {
        logger.info("Converters update started.");

        try {
            Set<String> newMetricTypes = new HashSet<>();

            final Set<BeanDefinition> beans = provider.findCandidateComponents("com.dreamwork.art.service.converters");

            for (BeanDefinition bean : beans) {
                String beanName = bean.getBeanClassName();

                if (!this.currBeans.contains(beanName)) {
                    Converter converter = (Converter) Class.forName(beanName).newInstance();
                    newMetricTypes.addAll(converter.types());
                    this.converters.add(converter);
                    this.currBeans.add(beanName);
                }
            }

            metricsRepo.addTypes(newMetricTypes);

            logger.info("Added new metric types: {}", newMetricTypes);
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
