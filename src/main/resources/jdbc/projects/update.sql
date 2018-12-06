UPDATE
  project
SET
  name        = COALESCE(?, name),
  client      = COALESCE(?, client),
  description = COALESCE(?, description),
  difficulty  = COALESCE(?, difficulty),
  status      = COALESCE(?, status),
  startedAt   = COALESCE(?, startedAt),
  closedAt    = COALESCE(?, closedAt)

WHERE id = ?;