package com.dreamwork.art.payload;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class UpdatedProject {
    @NonNull
    private final Long id;

    private final String name;

    private final String client;

    private final String description;

    private final Short status;

    private final Timestamp startedAt;

    private final Timestamp closedAt;

    private final List<String> tags;
}
