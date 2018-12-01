package com.dreamwork.art.repository;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.model.MetricsBatch;
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
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Component
public class MetricsRepo {
    private final JdbcTemplate jdbc;
    private final Calendar calendar;
    private final String cmd;
    private final String insertMetricsCmd;

    @Autowired
    public MetricsRepo(JdbcTemplate jdbc) throws IOException {
        this.jdbc = jdbc;
        this.calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));

        this.cmd = StreamUtils.copyToString(new ClassPathResource("jdbc/metrics/list_all.sql").getInputStream(), Charset.defaultCharset());
        this.insertMetricsCmd = StreamUtils.copyToString(new ClassPathResource("jdbc/metrics/insert_metrics.sql").getInputStream(), Charset.defaultCharset());
    }

    @SuppressWarnings("ConstantConditions")
    public List<ListedMetricGroup> list(long projectId, Timestamp from, Timestamp until, int minutes) {
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

    @SuppressWarnings("ConstantConditions")
    public void setMetrics(MetricsBatch batch, List<Long> projects) {
        long firstFreeGroup = jdbc.queryForObject("SELECT currval('groups_id_seq')", Long.class) + 1;

        jdbc.batchUpdate("INSERT INTO groups (projectId, createdAt) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, projects.get(i));
                ps.setTimestamp(2, batch.getCreatedAt(), calendar);
            }

            @Override
            public int getBatchSize() {
                return projects.size();
            }
        });

        jdbc.batchUpdate(this.insertMetricsCmd, new BatchPreparedStatementSetter() {
            int currGroupIndex = 0;
            int currMetricIndex = 0;

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                List<Metric> group = batch.getMetrics().get(currGroupIndex);

                if (group.size() == currMetricIndex) {
                    group = batch.getMetrics().get(++currGroupIndex);
                    currMetricIndex = 0;
                }

                Metric metric = group.get(currMetricIndex++);
                ps.setLong(1, currGroupIndex + firstFreeGroup);
                ps.setString(2, metric.getType());
                ps.setFloat(3, metric.getValue());
            }

            @Override
            public int getBatchSize() {
                return batch.getSize();
            }
        });
    }
}
