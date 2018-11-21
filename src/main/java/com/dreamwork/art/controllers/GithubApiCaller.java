package com.dreamwork.art.controllers;

import com.dreamwork.art.model.MetricGroup;
import com.dreamwork.art.payload.GraphQLRequest;
import com.dreamwork.art.repository.MetricsRepo;
import com.dreamwork.art.repository.ProjectRepo;
import com.dreamwork.art.service.MetricsConverter;
import com.dreamwork.art.tools.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private MetricsRepo metricsRepo;
    private MetricsConverter converter;

    @Autowired
    public GithubApiCaller(RestTemplate rest, ProjectRepo projectRepo, MetricsRepo metricsRepo, MetricsConverter converter) throws IOException {
        this.rest = rest;
        this.projectRepo = projectRepo;
        this.metricsRepo = metricsRepo;
        this.converter = converter;

        this.authHeader = new HttpHeaders();
        this.authHeader.set(HttpHeaders.AUTHORIZATION, "Bearer 510cbac79217532b1a2014185b7a173999fab71d");

        this.query = StreamUtils.copyToString(new ClassPathResource("github/query.sdl").getInputStream(), Charset.defaultCharset());
    }

    @Scheduled(fixedDelayString = "${githubapi.update}")
    public void update() {
        updateRepos();

        List<String> activeNodes = projectRepo.listActiveGithubNodes();

        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(query);
        request.addVariable("ids", activeNodes);

        ResponseEntity<LinkedHashMap> response = rest.exchange(
                GRAPHQL_API_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, authHeader),
                LinkedHashMap.class
        );

        List<MetricGroup> groups = this.converter.convert(response.getBody());

        metricsRepo.batchInsert(groups);
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
