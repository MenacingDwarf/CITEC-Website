package com.dreamwork.art.payload;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class NewProject {
    @NonNull
    private final String name;
    @NonNull
    private final String client;
    @NonNull
    private final String status;
    @NonNull
    private final String description;
    @NonNull
    private final String githubRepo;
}
