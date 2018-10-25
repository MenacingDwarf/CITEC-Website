package com.dreamwork.art.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Project {
    @NonNull
    private String name;

    @NonNull
    private String githubRepo;

    @NonNull
    private String node_id;
}
