package com.dreamwork.art.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
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
    private Short status;
    @NonNull
    private String description;
    @NonNull
    private String githubRepo;
    @NonNull
    private Timestamp startedAt;
}
