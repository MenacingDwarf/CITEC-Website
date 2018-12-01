package com.dreamwork.art.repository;

import com.dreamwork.art.payload.ListedProject;
import com.dreamwork.art.payload.ProjectsInfo;
import com.dreamwork.art.tools.Pair;
import com.dreamwork.art.tools.StringLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class ProjectRepo {
    private JdbcTemplate jdbc;

    private final String listCmd;
    private final String listActiveCmd;
    private final String listUntrackedCmd;
    private final String setGithubNodesCmd;
    private final String getGeneralInfoCmd;

    @Autowired
    public ProjectRepo(JdbcTemplate jdbc) throws IOException {
        this.jdbc = jdbc;

        this.listCmd = StringLoader.load("jdbc/projects/list.sql");
        this.listActiveCmd = StringLoader.load("jdbc/projects/list_active.sql");
        this.listUntrackedCmd = StringLoader.load("jdbc/projects/list_untracked.sql");
        this.setGithubNodesCmd = StringLoader.load("jdbc/projects/set_github_nodes.sql");
        this.getGeneralInfoCmd = StringLoader.load("jdbc/projects/get_general_info.sql");
    }

    public List<ListedProject> list(int limit, int offset) {
        return jdbc.query(
                this.listCmd,

                statement -> {
                    statement.setInt(1, limit);
                    statement.setInt(2, offset);
                },

                (rs, i) -> {
                    ListedProject project = new ListedProject();
                    project.setId(rs.getLong("id"));
                    project.setName(rs.getString("name"));
                    project.setClient(rs.getString("client"));
                    project.setStatus(rs.getShort("status"));
                    project.setDescription(rs.getString("description"));
                    project.setGithubRepo(rs.getString("githubRepo"));
                    project.setStartedAt(rs.getTimestamp("startedAt"));
                    project.setClosedAt(rs.getTimestamp("closedAt"));
                    return project;
                }
        );
    }

    public List<Pair<Long, String>> listUntracked() {
        return jdbc.query(
                this.listUntrackedCmd,

                (rs, i) -> {
                    Pair<Long, String> p = new Pair<>();
                    p.setFirst(rs.getLong(1));
                    p.setSecond(rs.getString(2));
                    return p;
                }
        );
    }

    public List<Pair<Long, String>> listActiveProjects() {
        return jdbc.query(
                this.listActiveCmd,

                (rs, i) -> {
                    Pair<Long, String> p = new Pair<>();
                    p.setFirst(rs.getLong(1));
                    p.setSecond(rs.getString(2));
                    return p;
                }
        );
    }

    public void setGithubNodes(List<Pair<Long, String>> nodes) {
        jdbc.batchUpdate(
                this.setGithubNodesCmd,

                new BatchPreparedStatementSetter() {
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

    public ProjectsInfo getGeneralInfo() {
        return jdbc.query(
                this.getGeneralInfoCmd,

                rs -> {
                    rs.next();
                    ProjectsInfo info = new ProjectsInfo();
                    info.setTotal(rs.getInt("total"));
                    info.setActive(rs.getInt("active"));
                    return info;
                }
        );
    }
}
