package com.dreamwork.art.controllers;

import com.dreamwork.art.payload.GraphQLRequest;
import com.dreamwork.art.repository.ProjectRepo;
import com.dreamwork.art.tools.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

@Component
public class GithubApiCaller {
    private final String REPOSITORIES_API_URL = "https://api.github.com/repos/";
    private final String GRAPHQL_API_URL = "https://api.github.com/graphql";
    private final String query;

    String token = "Bearer c675780f0cec1df803e3dc0da0a42264406f7393";

    private RestTemplate rest;
    private ProjectRepo projectRepo;
    private HttpHeaders authHeader;

    @Autowired
    public GithubApiCaller(RestTemplate rest, ProjectRepo projectRepo) throws IOException {
        this.rest = rest;
        this.projectRepo = projectRepo;

        this.authHeader = new HttpHeaders();
        this.authHeader.set(HttpHeaders.AUTHORIZATION, token);

        this.query = StreamUtils.copyToString(new ClassPathResource("github/query.sdl").getInputStream(), Charset.defaultCharset());
    }

    @Scheduled(fixedDelay = 60000)
    public void update() {
        List<Pair<Long, String>> untracked = projectRepo.findUntrackedByGithub();
        
        if (!untracked.isEmpty()) {
         //   startTracking(untracked);
        }

        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(query);
        request.addVariable("ids", Arrays.asList(
                "MDEwOlJlcG9zaXRvcnkyODE5MTE2MQ==",
                "MDEwOlJlcG9zaXRvcnk0OTk0NTQ1OA==",
                "MDEwOlJlcG9zaXRvcnkyMjA2NzUyMQ==",
                "MDEwOlJlcG9zaXRvcnkxMDU1OTA4Mzc="
        ));

        ResponseEntity response = rest.exchange(
                GRAPHQL_API_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, authHeader),
                Object.class
        );

        System.out.println(response.getBody());
    }

    private void startTracking(List<Pair<Long, String>> untracked) {
        List<String> orderedNodes = new ArrayList<>(untracked.size());

        for (Pair<Long, String> p : untracked) {
            ResponseEntity<HashMap> response = rest.exchange(
                    REPOSITORIES_API_URL + p.getSecond(),
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    HashMap.class
            );

            orderedNodes.add((String)response.getBody().get("node_id"));
        }

        System.out.println(orderedNodes);
    }
}
