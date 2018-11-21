SELECT
  t.id,
  t.mintime,
  metrics.value,
  types.type

FROM
  (
    SELECT
      min(id) AS id,
      min(createdAt) AS mintime

    FROM groups
    WHERE projectId = ? AND createdAt BETWEEN ? AND ?
    GROUP BY DATEADD(MINUTE, DATEDIFF(MINUTE, '1970-01-01 00:00:01', createdAt) / ? * ?, '1970-01-01 00:00:01')

  ) AS t

  INNER JOIN metrics ON t.id = metrics.groupId
  INNER JOIN types ON metrics.typeId = types.id

ORDER BY t.mintime DESC