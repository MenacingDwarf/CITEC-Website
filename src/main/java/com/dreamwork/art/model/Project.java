package com.dreamwork.art.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "projects")
@Getter
@Setter
@RequiredArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private final String name;

    @NonNull
    private final String githubRepo;

    @NonNull
    private final String node_id;
}
