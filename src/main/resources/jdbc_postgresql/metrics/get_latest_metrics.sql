SELECT
  metric_type.type,
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
INNER JOIN
  metric_type ON metric_type.id = metric.typeId
WHERE
  metric_type.type = ANY (?)

ORDER BY
  g.projectId ASC;



