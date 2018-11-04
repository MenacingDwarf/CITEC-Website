package com.dreamwork.art.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class Metric {
    @NonNull
    private final String type;

    private final float value;
}
