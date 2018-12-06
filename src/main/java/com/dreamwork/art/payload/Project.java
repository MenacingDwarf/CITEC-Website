package com.dreamwork.art.payload;

import com.dreamwork.art.model.Metric;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Project {
    private Long id;
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
    private String difficulty;
    @NonNull
    private Timestamp startedAt;

    private Timestamp closedAt;

    private List<String> tags;

    private List<Metric> metrics;
}
