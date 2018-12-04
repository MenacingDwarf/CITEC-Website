package com.dreamwork.art.repository;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.model.MetricsBatch;
import com.dreamwork.art.payload.ListedMetricGroup;
import com.dreamwork.art.payload.Project;
import com.dreamwork.art.tools.StringLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@Component
public class MetricsRepo {
    private final JdbcTemplate jdbc;
    private final Calendar calendar;

    private final String listCmd;
    private final String setGroupsCmd;
    private final String setMetricsCmd;
    private final String getLatestMetricsCmd;
    private final String getCurrentGroupSeqIndexCmd;

    @Autowired
    public MetricsRepo(JdbcTemplate jdbc) throws IOException {
        this.jdbc = jdbc;
        this.calendar = Calendar.getInstance(TimeZone.getDefault());

        this.listCmd = StringLoader.load("jdbc/metrics/list.sql");
        this.setGroupsCmd = StringLoader.load("jdbc/metrics/set_groups.sql");
        this.setMetricsCmd = StringLoader.load("jdbc/metrics/set_metrics.sql");
        this.getLatestMetricsCmd = StringLoader.load("jdbc/metrics/get_latest_metrics.sql");
        this.getCurrentGroupSeqIndexCmd = StringLoader.load("jdbc/metrics/get_current_group_seq_index.sql");
    }

    @SuppressWarnings("ConstantConditions")
    public List<ListedMetricGroup> list(long projectId, Timestamp from, Timestamp until, int minutes) {
        return jdbc.query(
                this.listCmd,

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
    public Map<Long, List<Metric>> getLatestMetrics(List<String> types, Long[] projects) {
        return jdbc.query(
                this.getLatestMetricsCmd,

                statement -> {
                    Array projectsArray = statement.getConnection().createArrayOf("BIGINT", projects);
                    Array typesArray = statement.getConnection().createArrayOf("VARCHAR", types.toArray(new String[types.size()]));
                    statement.setArray(1, projectsArray);
                    statement.setArray(2, typesArray);
                },

                rs -> {
                    Map<Long, List<Metric>> output = new HashMap<>();

                    long currId = 0;
                    List<Metric> currList = null;

                    while (rs.next()) {
                        Metric metric = new Metric(rs.getString(1), rs.getFloat(2));

                        long id = rs.getLong(3);

                        if (currId != id) {
                            currId = id;
                            currList = new ArrayList<>();
                            output.put(currId, currList);
                        }

                        currList.add(metric);
                    }

                    return output;
                }
        );
    }

    @SuppressWarnings("ConstantConditions")
    public void setMetrics(MetricsBatch batch, Long[] projects) {
        if (projects.length != 0) {
            jdbc.batchUpdate(this.setGroupsCmd, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, projects[i]);
                    ps.setTimestamp(2, batch.getCreatedAt(), calendar);
                }

                @Override
                public int getBatchSize() {
                    return projects.length;
                }
            });

            long firstAddedGroup = jdbc.queryForObject(getCurrentGroupSeqIndexCmd, Long.class) - projects.length;

            jdbc.batchUpdate(this.setMetricsCmd, new BatchPreparedStatementSetter() {
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
                    ps.setLong(1, currGroupIndex + firstAddedGroup);
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
}
