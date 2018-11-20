package com.dreamwork.art.payload;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ListedProject {
    private Long id;
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
    private Timestamp startedAt;

    private Timestamp closedAt;
}
