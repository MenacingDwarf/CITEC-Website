package com.dreamwork.art.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Project {
    private Long id;
    @NonNull
    private String node_id;
    @NonNull
    private String name;
    @NonNull
    private String client;
    @NonNull
    private String status;
    @NonNull
    private String description;
    @NonNull
    private String githubRepo;
    @NonNull
    private List<String> tags;
    @NonNull
    private List<MetricGroup> groups;
}
