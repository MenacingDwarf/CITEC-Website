package com.dreamwork.art.repository;

import com.dreamwork.art.model.Metric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    Page<Metric> findByProjectId(Long projectId, Pageable pageable);
}
