package com.dreamwork.art.controllers;

import com.dreamwork.art.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UpdateController {
    // TODO:
    String url = "https://api.github.com/graphql";
    String token = "Bearer c3a7c8a7290460d8d0bad49c14f532c96725f355";
    HttpEntity<String> entity;

    private RestTemplate rest;

    JdbcTemplate jdbc;

    @Autowired
    public UpdateController(RestTemplate rest) {
        this.rest = rest;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        entity = new HttpEntity<>(headers);
    }

    @Scheduled(fixedDelay = 10000)
    public void updateMetrics() {
        // TODO: 17.10.2018  
    }
}
