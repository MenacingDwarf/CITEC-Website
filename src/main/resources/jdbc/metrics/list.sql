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
    GROUP BY DATEADD(MINUTE, DATEDIFF(MINUTE, '1970-01-01 00:00:01', createdAt) / ? * ?, '1970-01-01 00:00:01')

  ) AS t

  INNER JOIN metric ON t.id = metric.groupId

ORDER BY t.mintime DESC