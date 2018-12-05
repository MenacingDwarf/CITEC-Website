INSERT INTO project (name, client, status, description, githubRepo, startedAt) VALUES
  ('Project #2', 'Client #2', 1, 'This is #2 project', '/SilverTiger/lwjgl3-tutorial', '2008-11-11'),
  ('Project #1', 'Client #1', 1, 'This is #1 project', '/SilverTiger/lwjgl3', '2007-11-11'),
  ('Project #4', 'Client #4', 0, 'This is #4 project', '/ocornut/imgui', '2010-11-11'),
  ('Project #3', 'Client #3', 1, 'This is #3 project', '/aws-amplify/amplify-js', '2009-11-11');

INSERT INTO tag (projectId, tag) VALUES
  (2, 'lwjgl'), (2, 'java'),
  (3, 'imgui'),
  (4, 'aws'), (4, 'amazon');

INSERT INTO metric_group (projectId, createdAt) VALUES
  (1, '2012-09-17 17:44:42'), (1, '2012-09-17 18:47:52'), (1, '2012-09-17 19:45:15');

INSERT INTO metric (groupId, type, value) VALUES
  (1, 'test0', 1), (2, 'test0', 2), (3, 'test0', 3);

