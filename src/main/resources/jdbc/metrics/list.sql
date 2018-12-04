SELECT
  t.id,
  t.mintime,
  metric.value,
  metric.type

FROM
  (
    SELECT
      min(id) AS id,
      min(createdAt) AS mintime

    FROM metric_group
    WHERE projectId = ? AND createdAt BETWEEN ? AND ?
    GROUP BY TIMESTAMP WITH TIME ZONE 'epoch' + INTERVAL '1 second' * round(extract('epoch' FROM createdAt) / (? * 60)) * (? * 60)

  ) AS t

  INNER JOIN metric ON t.id = metric.groupId

ORDER BY t.mintime DESC