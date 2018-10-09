package com.dreamwork.art.model;

import javax.persistence.*;

@Entity
@Table(name = "metrics")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MetricType type;

    private double value;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Override
    public String toString() {
        return String.format(
                "Metric[id=%d, type='%s', value='%f']", id, type.name(), value);
    }
}
