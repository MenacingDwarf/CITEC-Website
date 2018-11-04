package com.dreamwork.art.controllers;

import com.dreamwork.art.payload.GraphQLRequest;
import com.dreamwork.art.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UpdateController {
    // TODO:
    String url = "https://api.github.com/graphql";
    String token = "Bearer 7c8285a2a7e55ff1694bd5720ffe25fe455baf02";
    String query = "query($ids: [ID!]!) {  rateLimit {    cost    remaining    resetAt  }    nodes(ids: $ids) {    ... on Repository {      name      description      createdAt      updatedAt    }  }}";

    private RestTemplate rest;
    private ProjectRepo projectRepo;
    private HttpHeaders authHeader;

    @Autowired
    public UpdateController(RestTemplate rest, ProjectRepo projectRepo) {
        this.rest = rest;
        this.projectRepo = projectRepo;

        this.authHeader = new HttpHeaders();
        this.authHeader.set(HttpHeaders.AUTHORIZATION, token);
    }

    @Scheduled(fixedDelay = 60000)
    public void updateMetrics() {
        List<String> ids = projectRepo.findAllNodeIds();

        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(query);
        request.addVariable("ids", ids);

        /*
        ResponseEntity response = rest.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request, authHeader),
                Object.class
        );
        */
    }
}
