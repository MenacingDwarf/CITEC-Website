package com.dreamwork.art.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class NewProjectDTO {
    @NotBlank
    private final String name;

    @NotBlank
    private final String githubRepo;
}
