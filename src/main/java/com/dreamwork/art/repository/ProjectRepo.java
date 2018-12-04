package com.dreamwork.art.repository;

import com.dreamwork.art.payload.ListedMetricGroup;
import com.dreamwork.art.payload.Project;
import com.dreamwork.art.payload.NewProject;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ProjectRepo {
    private JdbcTemplate jdbc;

    private final String addCmd;
    private final String listCmd;
    private final String listActiveCmd;
    private final String listUntrackedCmd;
    private final String setGithubNodesCmd;
    private final String getGeneralInfoCmd;

    @Autowired
    public ProjectRepo(JdbcTemplate jdbc) throws IOException {
        this.jdbc = jdbc;

        this.addCmd = StringLoader.load("jdbc/projects/add.sql");
        this.listCmd = StringLoader.load("jdbc/projects/list.sql");
        this.listActiveCmd = StringLoader.load("jdbc/projects/list_active.sql");
        this.listUntrackedCmd = StringLoader.load("jdbc/projects/list_untracked.sql");
        this.setGithubNodesCmd = StringLoader.load("jdbc/projects/set_github_nodes.sql");
        this.getGeneralInfoCmd = StringLoader.load("jdbc/projects/get_general_info.sql");
    }

    public List<Project> list(int limit, int offset) {
        return jdbc.query(
                this.listCmd,

                statement -> {
                    statement.setInt(1, limit);
                    statement.setInt(2, offset);
                },

                rs -> {
                    List<Project> projects = new ArrayList<>();

                    long currId = 0;
                    Project currProject = null;

                    while (rs.next()) {
                        long id = rs.getLong(1);

                        if (currId != id) {
                            currId = id;
                            currProject = new Project();
                            currProject.setId(rs.getLong(1));
                            currProject.setName(rs.getString(3));
                            currProject.setClient(rs.getString(4));
                            currProject.setStatus(rs.getShort(5));
                            currProject.setDescription(rs.getString(6));
                            currProject.setGithubRepo(rs.getString(7));
                            currProject.setStartedAt(rs.getTimestamp(8));
                            currProject.setClosedAt(rs.getTimestamp(9));

                            String tag = rs.getString(2);

                            if (rs.wasNull()) {
                                currProject.setTags(Collections.emptyList());
                            }

                            else {
                                List<String> tags = new ArrayList<>();
                                tags.add(tag);
                                currProject.setTags(tags);
                            }

                            projects.add(currProject);
                        }

                        else {
                            currProject.getTags().add(rs.getString(2));
                        }
                    }

                    return projects;
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

    public void add(NewProject newProject) {
        jdbc.update(addCmd, ps -> {
            ps.setString(1, newProject.getName());
            ps.setString(2, newProject.getClient());
            ps.setString(3, newProject.getMembers());
            ps.setString(4, newProject.getDescription());
            ps.setString(5, newProject.getGithubRepo());
            ps.setShort(6, newProject.getStatus());
            ps.setTimestamp(7, newProject.getStartedAt());
        });
    }
}
