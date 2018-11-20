package com.dreamwork.art.repository;

import com.dreamwork.art.model.MetricGroup;
import com.dreamwork.art.payload.ListedProject;
import com.dreamwork.art.payload.ProjectsInfo;
import com.dreamwork.art.tools.Pair;
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
import java.util.*;

@Component
public class ProjectRepo {
    private JdbcTemplate jdbc;

    private final String selectCmd;
    private final String projectsInfoCmd;

    @Autowired
    public ProjectRepo(JdbcTemplate jdbc) throws IOException {
        this.jdbc = jdbc;
        //this.insertCmd = new SimpleJdbcInsert(jdbc)
        //        .withTableName("projects")
        //        .usingGeneratedKeyColumns("id");

        this.selectCmd = StreamUtils.copyToString(new ClassPathResource("jdbc/projects/list_all.sql").getInputStream(), Charset.defaultCharset());
        this.projectsInfoCmd = StreamUtils.copyToString(new ClassPathResource("jdbc/projects/info.sql").getInputStream(), Charset.defaultCharset());
    }

    public List<ListedProject> find(int limit, int offset) {
        return jdbc.query(
                this.selectCmd,

                statement -> {
                    statement.setInt(1, limit);
                    statement.setInt(2, offset);
                },

                (rs, i) -> {
                    ListedProject project = new ListedProject();
                    project.setId(rs.getLong("id"));
                    project.setName(rs.getString("name"));
                    project.setClient(rs.getString("client"));
                    project.setStatus(rs.getString("status"));
                    project.setDescription(rs.getString("description"));
                    project.setGithubRepo(rs.getString("githubRepo"));
                    project.setStartedAt(rs.getTimestamp("startedAt"));
                    project.setClosedAt(rs.getTimestamp("closedAt"));
                    return project;
                }
        );
    }

    public List<Pair<Long, String>> findUntrackedByGithub() {
        final String cmd = "SELECT id, githubRepo FROM projects WHERE github_node_id IS NULL";

        return jdbc.query(
                cmd,
                (rs, i) -> {
                    Pair<Long, String> p = new Pair<>();
                    p.setFirst(rs.getLong(1));
                    p.setSecond(rs.getString(2));
                    return p;
                }
        );
    }

    public void batchUpdateGithubNodes(List<Pair<Long, String>> nodes) {
        final String cmd = "UPDATE projects SET github_node_id = ? WHERE id = ?";

        jdbc.batchUpdate(cmd, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Pair<Long, String> p = nodes.get(i);
                ps.setString(1, p.getSecond());
                ps.setLong(2, p.getFirst());
            }

            @Override
            public int getBatchSize() {
                return nodes.size();
            }
        });
    }

    public List<String> listGithubNodes() {
        final String cmd = "SELECT github_node_id FROM projects";

        return jdbc.query(
                cmd,
                (rs, i) -> rs.getString(1)
        );
    }

    public ProjectsInfo info() {
        return jdbc.query(
                this.projectsInfoCmd,

                rs -> {
                    rs.next();
                    ProjectsInfo info = new ProjectsInfo();
                    info.setTotal(rs.getInt("total"));
                    info.setActive(rs.getInt("active"));
                    return info;
                }
        );
    }

    public void batchAddGroup(List<MetricGroup> groups) {
       // jdbc.batchUpdate()
    }
}
