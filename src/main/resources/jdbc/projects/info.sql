SELECT
  COUNT(*) total,
  COUNT(case when status = 'active' then 1 else null end) active
FROM projects;