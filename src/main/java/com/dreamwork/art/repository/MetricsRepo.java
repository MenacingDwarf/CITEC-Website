package com.dreamwork.art.repository;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.model.MetricGroup;
import com.dreamwork.art.payload.ListedMetricGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class MetricsRepo {
    private final JdbcTemplate jdbc;
    private final String cmd;
    private final String insertMetricsCmd;

    @Autowired
    public MetricsRepo(JdbcTemplate jdbc) throws IOException {
        this.jdbc = jdbc;
        this.cmd = StreamUtils.copyToString(new ClassPathResource("jdbc/metrics/list_all.sql").getInputStream(), Charset.defaultCharset());
        this.insertMetricsCmd = StreamUtils.copyToString(new ClassPathResource("jdbc/metrics/insert_metrics.sql").getInputStream(), Charset.defaultCharset());
    }

    public void batchInsert(List<MetricGroup> groups) {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        long firstId = jdbc.queryForObject("SELECT currval('groups_id_seq')", Long.class) + 1;

        jdbc.batchUpdate("INSERT INTO groups (projectId, createdAt) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MetricGroup group = groups.get(i);
                ps.setLong(1, group.getProjectId());
                ps.setTimestamp(2, createdAt);
            }

            @Override
            public int getBatchSize() {
                return groups.size();
            }
        });

        int metricsInEachGroup = 3;
        int values = groups.size() * metricsInEachGroup;

        jdbc.batchUpdate(this.insertMetricsCmd, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int groupIndex = i / metricsInEachGroup;
                int metricIndexInGroup = i % metricsInEachGroup;

                MetricGroup group = groups.get(groupIndex);
                Metric metric = group.getMetric(metricIndexInGroup);

                ps.setLong(1, groupIndex + firstId);
                ps.setString(2, metric.getType());
                ps.setFloat(3, metric.getValue());
            }

            @Override
            public int getBatchSize() {
                return values;
            }
        });
    }

    public List<ListedMetricGroup> findEachNRow(long projectId, Timestamp from, Timestamp until, int minutes) {
        return jdbc.query(
                cmd,

                statement -> {
                    statement.setLong(1, projectId);
                    statement.setTimestamp(2, from);
                    statement.setTimestamp(3, until);
                    statement.setInt(4, minutes);
                    statement.setInt(5, minutes);
                },

                rs -> {
                    List<ListedMetricGroup> groups = new ArrayList<>();

                    long currId = 0;
                    ListedMetricGroup currGroup = null;

                    while (rs.next()) {
                        Metric metric = new Metric(rs.getString(4), rs.getFloat(3));

                        long id = rs.getLong(1);

                        if (currId != id) {
                            currId = id;
                            currGroup = new ListedMetricGroup(rs.getTimestamp(2));
                            groups.add(currGroup);
                        }

                        currGroup.addMetric(metric);
                    }

                    return groups;
                }
        );
    }
}
