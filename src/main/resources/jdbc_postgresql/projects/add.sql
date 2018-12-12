INSERT INTO project (name, client, description, githubRepo, difficulty, status, startedAt, closedAt)
VALUES (?, ?, ?, ?, ?, ?, ?, ?)
RETURNING id;