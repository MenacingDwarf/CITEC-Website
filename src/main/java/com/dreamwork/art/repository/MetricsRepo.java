package com.dreamwork.art.repository;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.model.MetricGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class MetricsRepo {
    private final JdbcTemplate jdbc;
    private final String cmd;

    @Autowired
    public MetricsRepo(JdbcTemplate jdbc) throws IOException {
        this.jdbc = jdbc;
        this.cmd = StreamUtils.copyToString(new ClassPathResource("jdbc/template.txt").getInputStream(), Charset.defaultCharset());
    }

    public List<MetricGroup> find(long projectId, Timestamp from, Timestamp until, int n) {
        return jdbc.query(
                cmd,

                statement -> {
                    statement.setLong(1, projectId);
                    statement.setTimestamp(2, from);
                    statement.setTimestamp(3, until);
                    statement.setInt(4, n);
                },

                rs -> {
                    List<MetricGroup> groups = new ArrayList<>();

                    if (rs.next()) {
                        MetricGroup temp = new MetricGroup(rs.getTimestamp("timestamp"));

                        temp.addMetric(new Metric(
                                rs.getString("type"),
                                rs.getFloat("value")
                        ));

                        long tempId = rs.getLong("id");


                        while (rs.next()) {
                            Metric metric = new Metric(rs.getString("type"), rs.getFloat("value"));

                            long id = rs.getLong("id");

                            if (tempId != id) {
                                temp = new MetricGroup(rs.getTimestamp("timestamp"));
                                groups.add(temp);
                            }

                            temp.addMetric(metric);
                        }
                    }

                    return groups;
                }
        );
    }
}
