package com.dreamwork.art.controllers;

import com.dreamwork.art.model.Project;
import com.dreamwork.art.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    ProjectRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Project> getAllProjects(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Project createProject(@Valid @RequestBody Project project) {
        return repository.save(project);
    }
}
