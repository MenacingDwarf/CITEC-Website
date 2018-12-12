SELECT
  id, githubRepo
FROM
  project
WHERE
  githubNodeId IS NULL AND githubRepo IS NOT NULL;