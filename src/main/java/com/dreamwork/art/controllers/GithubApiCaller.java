package com.dreamwork.art.controllers;

import com.dreamwork.art.handlers.GithubApiRestErrorHandler;
import com.dreamwork.art.model.MetricsBatch;
import com.dreamwork.art.payload.GraphQLRequest;
import com.dreamwork.art.repository.MetricsRepo;
import com.dreamwork.art.repository.ProjectRepo;
import com.dreamwork.art.service.MetricsConverter;
import com.dreamwork.art.tools.Pair;
import com.dreamwork.art.tools.StringLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GithubApiCaller {
    private final static Logger logger = LoggerFactory.getLogger(GithubApiCaller.class);

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
            MetricsConverter converter) throws IOException {

        this.rest = builder
                .errorHandler(new GithubApiRestErrorHandler())
                .build();

        this.projectRepo = projectRepo;
        this.metricsRepo = metricsRepo;
        this.converter   = converter;

        this.authHeader = new HttpHeaders();
        this.authHeader.set(HttpHeaders.CONTENT_TYPE, "application/json");
        this.authHeader.set(HttpHeaders.ACCEPT, "application/json");
        this.authHeader.set(HttpHeaders.AUTHORIZATION, "bearer " + System.getenv("GITHUB_TOKEN"));

        this.query = StringLoader.load("github/query.sdl");
    }

    @Scheduled(fixedDelayString = "${githubapi.update-in-millisec}")
    public void update() {
        updateRepos();
        updateMetrics();
    }

    private void updateRepos() {
        List<Pair<Long, String>> untracked = projectRepo.listUntracked();

        if (!untracked.isEmpty()) {
            logger.info("Repositories update has started. Total untracked: {}.", untracked.size());

            for (Pair<Long, String> p : untracked) {
                ResponseEntity<HashMap> response = rest.exchange(
                        REPOSITORIES_API_URL + p.getSecond(),
                        HttpMethod.GET,
                        new HttpEntity<>(null, authHeader),
                        HashMap.class
                );

                p.setSecond((String)response.getBody().get("node_id"));
            }

            projectRepo.setGithubNodes(untracked);
        }
    }

    private void updateMetrics() {
        List<Pair<Long, String>> activeProjects = projectRepo.listActiveProjects();

        logger.info("Metrics update has started. Total active projects: {}.", activeProjects.size());

        String[] githubIds = new String[activeProjects.size()];
        Long[]   localIds  = new Long[activeProjects.size()];

        int i = 0;
        for (Pair<Long, String> p : activeProjects) {
            githubIds[i] = p.getSecond();
            localIds[i] = p.getFirst();
            i++;
        }

        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(query);
        request.addVariable("ids", githubIds);

        ResponseEntity<LinkedHashMap> response = rest.exchange(
                GRAPHQL_API_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, authHeader),
                LinkedHashMap.class
        );

        Map data = (Map)response.getBody().get("data");

        processApiLimits((Map)data.get("rateLimit"));

        MetricsBatch batch = this.converter.convert((List)data.get("nodes"));

        logger.info("Batch created. Size: {}.", batch.getSize());

        metricsRepo.setMetrics(batch, localIds);
    }

    private void processApiLimits(Map limit) {
        logger.info("Update cost: {}, Reset at: {}.", limit.get("cost"), limit.get("resetAt"));
        logger.warn("Remaining points: {}", limit.get("remaining"));
    }
}
