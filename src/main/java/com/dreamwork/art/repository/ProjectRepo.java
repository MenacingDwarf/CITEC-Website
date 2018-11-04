package com.dreamwork.art.repository;

import com.dreamwork.art.model.MetricGroup;
import com.dreamwork.art.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProjectRepo {
    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insertCmd;

    @Autowired
    public ProjectRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insertCmd = new SimpleJdbcInsert(jdbc)
                .withTableName("projects")
                .usingGeneratedKeyColumns("id");
    }

    public List<Project> findProjects(int limit, int offset) {
        String cmd = "SELECT * FROM projects LIMIT " + limit + " OFFSET " + offset;

        return jdbc.query(cmd, (result, i) -> {
            Project project = new Project();
            project.setName(result.getString("name"));
            project.setGithubRepo(result.getString("githubRepo"));
            return project;
        });
    }

    public int totalNumberOfProjects() {
        String cmd = "SELECT COUNT(*) FROM projects";

        List<Integer> res = jdbc.query(cmd, (result, i) -> result.getInt(1));

        return res.get(0);
    }

    public void batchAddGroup(List<MetricGroup> groups) {
       // jdbc.batchUpdate()
    }

    public List<String> findAllNodeIds() {
        String cmd = "SELECT node_id FROM projects";

        return jdbc.query(cmd, (result, i) -> result.getString("node_id"));
    }


    public void save(Project project) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", project.getName());
        params.put("githubRepo", project.getGithubRepo());
        params.put("node_id", project.getNode_id());

        this.insertCmd.execute(params);
    }
}
