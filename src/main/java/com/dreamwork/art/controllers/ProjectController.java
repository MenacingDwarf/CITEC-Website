package com.dreamwork.art.controllers;

import com.dreamwork.art.model.Project;
import com.dreamwork.art.payload.NewProjectDTO;
import com.dreamwork.art.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    // TODO:
    String url = "https://api.github.com/repos/";
    String token = "Bearer c3a7c8a7290460d8d0bad49c14f532c96725f355";
    HttpEntity<String> entity;

    private ProjectRepository repository;
    private RestTemplate rest;

    @Autowired
    public ProjectController(ProjectRepository repository, RestTemplate rest) {
        this.repository = repository;
        this.rest = rest;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        entity = new HttpEntity<>(headers);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Project> getAllProjects(@PageableDefault(value = 15) Pageable pageable) {
        return repository.findAll(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Project createProject(@Valid @RequestBody NewProjectDTO dto) {
        ResponseEntity<HashMap> response = rest.exchange(url + dto.getGithubRepo(), HttpMethod.GET, entity, HashMap.class);

        String node_id = (String)response.getBody().get("node_id");

        Project project = new Project(dto.getName(), dto.getGithubRepo(), node_id);

        return repository.save(project);
    }
}
