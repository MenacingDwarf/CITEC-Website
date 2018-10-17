package com.dreamwork.art.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "metrics")
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter @Setter
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final @NonNull Long id;

    @Enumerated(EnumType.STRING)
    private final @NonNull MetricType type;

    private double value;
}
