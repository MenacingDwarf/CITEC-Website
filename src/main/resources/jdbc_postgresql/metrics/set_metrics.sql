INSERT INTO
  metric (groupId, typeId, value)
VALUES
  (?, (SELECT id FROM metric_type WHERE type = ?), ?);