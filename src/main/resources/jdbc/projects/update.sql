UPDATE
  project
SET
  name        = COALESCE(?, name),
  client      = COALESCE(?, client),
  description = COALESCE(?, description),
  status      = COALESCE(?, status),
  startedAt   = COALESCE(?, startedAt),
  closedAt    = COALESCE(?, closedAt)

WHERE id = ?;