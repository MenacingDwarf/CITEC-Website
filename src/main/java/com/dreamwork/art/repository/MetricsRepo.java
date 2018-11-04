package com.dreamwork.art.repository;

import com.dreamwork.art.model.MetricGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;

public class MetricsRepo {
    private JdbcTemplate jdbc;

    @Autowired
    public MetricsRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<MetricGroup> findMetricGroups(long projectId, Timestamp from, Timestamp until, int n) {

    }
}
