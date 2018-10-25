package com.dreamwork.art.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Metric {
    @NonNull
    private MetricType type;

    @NonNull
    private Float value;
}
