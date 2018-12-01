package com.dreamwork.art.controllers;

import com.dreamwork.art.payload.ListedProject;
import com.dreamwork.art.payload.NewProject;
import com.dreamwork.art.payload.ProjectsInfo;
import com.dreamwork.art.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private ProjectRepo repo;

    @Autowired
    public ProjectController(ProjectRepo repo) {
        this.repo = repo;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ListedProject> getProjectList(
            @RequestParam int limit,
            @RequestParam int offset,
            @RequestParam(value = "tags") Optional<List<String>> tags) {

        if (tags.isPresent()) {
            // TODO
        }

        return repo.list(limit, offset);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ProjectsInfo repoInfo() {
        return repo.getGeneralInfo();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createProject(@Valid @RequestBody NewProject dto) {
        repo.add(dto);
    }
}
