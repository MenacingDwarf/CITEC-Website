package com.dreamwork.art.controllers;

import com.dreamwork.art.handlers.GithubApiRestErrorHandler;
import com.dreamwork.art.model.MetricsBatch;
import com.dreamwork.art.payload.GraphQLRequest;
import com.dreamwork.art.repository.MetricsRepo;
import com.dreamwork.art.repository.ProjectRepo;
import com.dreamwork.art.service.MetricsConverter;
import com.dreamwork.art.tools.Pair;
import com.dreamwork.art.tools.StringLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
import java.util.stream.Collectors;

@Component
public class GithubApiCaller {
    @Value("${githubapi.reposURL}")
    private String REPOSITORIES_API_URL;

    @Value("${githubapi.graphql}")
    private String GRAPHQL_API_URL;

    private HttpHeaders authHeader;
    private String query;

    private RestTemplate rest;
    private ProjectRepo projectRepo;
    private MetricsRepo metricsRepo;
    private MetricsConverter converter;

    @Autowired
    public GithubApiCaller(
            RestTemplateBuilder builder,
            ProjectRepo projectRepo,
            MetricsRepo metricsRepo,
            MetricsConverter converter,
            @Value("${githubapi.accesstoken}") String token) throws IOException {

        this.rest = builder
                .errorHandler(new GithubApiRestErrorHandler())
                .build();

        this.projectRepo = projectRepo;
        this.metricsRepo = metricsRepo;
        this.converter = converter;

        this.authHeader = new HttpHeaders();
        this.authHeader.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        this.query = StringLoader.load("github/query.sdl");
    }

    @Scheduled(fixedDelayString = "${githubapi.update-in-millisec}")
    public void update() {
        updateRepos();

        List<Pair<Long, String>> activeNodes = projectRepo.listActiveProjects();

        List<String> githubIds = activeNodes.stream().map(Pair::getSecond).collect(Collectors.toList());
        List<Long> internalIds = activeNodes.stream().map(Pair::getFirst).collect(Collectors.toList());

        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(query);
        request.addVariable("ids", githubIds);

        ResponseEntity<LinkedHashMap> response = rest.exchange(
                GRAPHQL_API_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, authHeader),
                LinkedHashMap.class
        );

        MetricsBatch batch = this.converter.convert(response.getBody());

        metricsRepo.setMetrics(batch, internalIds);
    }

    private void updateRepos() {
        List<Pair<Long, String>> untracked = projectRepo.listUntracked();

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

            projectRepo.setGithubNodes(untracked);
        }
    }
}
