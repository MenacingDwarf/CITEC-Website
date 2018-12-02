SELECT
  id, name, client, status, description, githubRepo, startedAt, closedAt
FROM project
ORDER BY startedAt DESC
LIMIT ? OFFSET ?;