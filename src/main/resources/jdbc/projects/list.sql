SELECT
  id, tag.tag, name, client, status, description, githubRepo, startedAt, closedAt
FROM
  (
    SELECT
      id, name, client, status, description, githubRepo, startedAt, closedAt
    FROM
      project
    ORDER BY
      startedAt DESC
    LIMIT ? OFFSET ?

  ) AS p

  LEFT JOIN tag ON tag.projectId = id

ORDER BY p.startedAt DESC