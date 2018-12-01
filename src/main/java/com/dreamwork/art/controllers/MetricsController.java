package com.dreamwork.art.controllers;

import com.dreamwork.art.payload.ListedMetricGroup;
import com.dreamwork.art.repository.MetricsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
    @Value("${metricsapi.defaultInterval}")
    private Integer defaultInterval;

    private MetricsRepo repo;

    @Autowired
    public MetricsController(MetricsRepo repo) {
        this.repo = repo;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ListedMetricGroup> getMetricsList(
            @RequestParam long projectId,
            @RequestParam Timestamp from,
            @RequestParam Timestamp until,
            @RequestParam(value = "interval") Optional<Integer> interval) {

        if (interval.isPresent()) {
            return repo.list(projectId, from, until, interval.get());
        }

        return repo.list(projectId, from, until, defaultInterval);
    }
}
