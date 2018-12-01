SELECT
  id, name, client, status, description, githubRepo, startedAt, closedAt
FROM projects
ORDER BY startedAt DESC
LIMIT ? OFFSET ?;