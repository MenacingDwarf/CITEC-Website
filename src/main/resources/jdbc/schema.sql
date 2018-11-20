CREATE TABLE projects
(
  id BIGINT AUTO_INCREMENT,
  github_node_id VARCHAR(127),
  name VARCHAR(127),
  client VARCHAR(255),
  status VARCHAR(127),
  description VARCHAR(4095),
  githubRepo VARCHAR(255),
  startedAt TIMESTAMP,
  closedAt TIMESTAMP,

  PRIMARY KEY(id)
);

CREATE TABLE groups
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY ,
  project_id BIGINT REFERENCES projects(id),
  timestamp TIMESTAMP,
);

CREATE TABLE metrics
(
  group_id BIGINT REFERENCES groups(id),
  type VARCHAR(255),
  value FLOAT,

  CONSTRAINT pk_metric PRIMARY KEY(group_id, type),
);