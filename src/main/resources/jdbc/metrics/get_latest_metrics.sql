SELECT
  metric.type,
  metric.value,
  g.projectId

FROM
  (
    SELECT DISTINCT ON (projectId)
      id,
      projectId,
      createdAt
    FROM
      metric_group
    WHERE
      projectId = ANY (?)
    ORDER BY
      projectId, createdAt DESC

  ) AS g

INNER JOIN
  metric ON g.id = metric.groupId
WHERE
  metric.type = ANY (?)

ORDER BY
  g.projectId ASC;



