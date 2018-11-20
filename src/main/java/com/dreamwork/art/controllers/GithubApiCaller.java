package com.dreamwork.art.controllers;

import com.dreamwork.art.model.MetricGroup;
import com.dreamwork.art.payload.GraphQLRequest;
import com.dreamwork.art.repository.ProjectRepo;
import com.dreamwork.art.service.MetricsConverter;
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
import java.nio.charset.Charset;
import java.util.*;

@Component
public class GithubApiCaller {
    private final String REPOSITORIES_API_URL = "https://api.github.com/repos/";
    private final String GRAPHQL_API_URL = "https://api.github.com/graphql";
    private final HttpHeaders authHeader;
    private final String query;

    private RestTemplate rest;
    private ProjectRepo projectRepo;
    private MetricsConverter converter;

    @Autowired
    public GithubApiCaller(RestTemplate rest, ProjectRepo projectRepo, MetricsConverter converter) throws IOException {
        this.rest = rest;
        this.projectRepo = projectRepo;
        this.converter = converter;

        this.authHeader = new HttpHeaders();
        this.authHeader.set(HttpHeaders.AUTHORIZATION, "Bearer 193a2d7d2179e598ce60be4f9f642685781bb2e9");

        this.query = StreamUtils.copyToString(new ClassPathResource("github/query.sdl").getInputStream(), Charset.defaultCharset());
    }

    @Scheduled(fixedDelay = 15000)
    public void update() {
        updateRepos();

        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(query);
        request.addVariable("ids", projectRepo.listGithubNodes());

        ResponseEntity<String> response = rest.exchange(
                GRAPHQL_API_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, authHeader),
                String.class
        );

        System.out.println(response.getBody());

        //List<MetricGroup> groups = this.converter.convert(response.getBody());
    }

    private void updateRepos() {
        List<Pair<Long, String>> untracked = projectRepo.findUntrackedByGithub();

        if (!untracked.isEmpty()) {
            for (Pair<Long, String> p : untracked) {
                ResponseEntity<HashMap> response = rest.exchange(
                        REPOSITORIES_API_URL + p.getSecond(),
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        HashMap.class
                );

                p.setSecond((String)response.getBody().get("node_id"));
            }

            projectRepo.batchUpdateGithubNodes(untracked);
        }
    }
}
