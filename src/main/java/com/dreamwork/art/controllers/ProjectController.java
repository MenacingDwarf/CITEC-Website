package com.dreamwork.art.controllers;

import com.dreamwork.art.model.Project;
import com.dreamwork.art.payload.NewProjectDTO;
import com.dreamwork.art.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    // TODO:
    String url = "https://api.github.com/repos/";
    String token = "Bearer 7c8285a2a7e55ff1694bd5720ffe25fe455baf02";
    HttpEntity<String> entity;

    private ProjectRepo repo;
    private RestTemplate rest;

    @Autowired
    public ProjectController(ProjectRepo repo, RestTemplate rest) {
        this.repo = repo;
        this.rest = rest;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        entity = new HttpEntity<>(headers);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getProjects(@RequestParam int limit, @RequestParam int offset) {
        return repo.findProjects(limit, offset);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createProject(@Valid @RequestBody NewProjectDTO dto) {
        ResponseEntity<HashMap> response = rest.exchange(url + dto.getGithubRepo(), HttpMethod.GET, entity, HashMap.class);

        String node_id = (String)response.getBody().get("node_id");

        Project project = new Project(dto.getName(), dto.getGithubRepo(), node_id);

        repo.save(project);
    }
}
