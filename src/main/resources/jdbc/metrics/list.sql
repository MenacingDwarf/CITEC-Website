SELECT
  t.id,
  t.mintime,
  metric.value,
  metric.type

FROM
  (
    SELECT
      min(id) AS id,
      max(createdAt) AS mintime

    FROM metric_group
    WHERE projectId = ? AND createdAt BETWEEN ? AND ?
    GROUP BY to_timestamp(floor(extract('epoch' FROM createdAt) / (? * 60)) * (? * 60))

  ) AS t

  INNER JOIN metric ON t.id = metric.groupId

ORDER BY t.mintime DESC