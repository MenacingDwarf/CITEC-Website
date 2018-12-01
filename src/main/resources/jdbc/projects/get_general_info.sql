SELECT
  COUNT(*) total,
  COUNT(case when status = 1 then 1 else null end) active
FROM projects;