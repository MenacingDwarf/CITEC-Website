package com.dreamwork.art.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class MetricType {
    private Long id;

    @NonNull
    private String name;
}
