package com.dreamwork.art.controllers;

import com.dreamwork.art.payload.ListedProject;
import com.dreamwork.art.payload.ProjectsInfo;
import com.dreamwork.art.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    // TODO:
    /*
    String url = "https://api.github.com/repos/";
    String token = "Bearer 23ba5f53bb83329b15a9d9b2264201b4bf38ca4d";
    HttpEntity<String> entity;
    */

    private ProjectRepo repo;

    @Autowired
    public ProjectController(ProjectRepo repo) {
        this.repo = repo;

       // HttpHeaders headers = new HttpHeaders();
       // headers.set(HttpHeaders.AUTHORIZATION, token);
       // entity = new HttpEntity<>(headers);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ListedProject> getProjectList(@RequestParam int limit, @RequestParam int offset, @RequestParam(value = "tags") Optional<List<String>> tags) {
        if (tags.isPresent()) {
            // TODO
        }

        return repo.find(limit, offset);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ProjectsInfo repoInfo() {
        return repo.info();
    }

    /*
    @RequestMapping(method = RequestMethod.POST)
    public Project createProject(@Valid @RequestBody NewProject dto) {
        //ResponseEntity<HashMap> response = rest.exchange(url + dto.getGithubRepo(), HttpMethod.GET, entity, HashMap.class);

        //String node_id = (String)response.getBody().get("node_id");

        Project project = new Project();

        project.setName(dto.getName());
        project.setClient(dto.getClient());
        project.setStatus(dto.getStatus());
        project.setDescription(dto.getDescription());
        project.setGithubRepo(dto.getGithubRepo());

        return repo.save(project);
    }
    */
}
