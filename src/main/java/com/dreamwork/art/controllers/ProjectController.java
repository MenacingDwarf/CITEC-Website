package com.dreamwork.art.controllers;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.payload.Project;
import com.dreamwork.art.payload.NewProject;
import com.dreamwork.art.payload.ProjectsInfo;
import com.dreamwork.art.repository.MetricsRepo;
import com.dreamwork.art.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private ProjectRepo projectRepo;
    private MetricsRepo metricsRepo;

    @Autowired
    public ProjectController(ProjectRepo projectRepo, MetricsRepo metricsRepo) {
        this.projectRepo = projectRepo;
        this.metricsRepo = metricsRepo;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getProjectList(
            @RequestParam int limit,
            @RequestParam int offset,
            @RequestParam(value = "metrics") Optional<List<String>> metrics) {

        List<Project> projects = projectRepo.list(limit, offset);

        if (metrics.isPresent()) {
            Map<Long, List<Metric>> metricsMap = metricsRepo.getLatestMetrics(metrics.get(), projects.stream().map(Project::getId).toArray(Long[]::new));
            projects.forEach(project -> {
                List<Metric> list = metricsMap.get(project.getId());

                if (list != null)
                    project.setMetrics(list);
                else
                    project.setMetrics(Collections.emptyList());
            });
        }

        return projects;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ProjectsInfo repoInfo() {
        return projectRepo.getGeneralInfo();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createProject(@Valid @RequestBody NewProject dto) {
        projectRepo.add(dto);
    }
}
