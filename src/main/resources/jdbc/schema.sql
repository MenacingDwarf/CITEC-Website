CREATE SEQUENCE groups_id_seq START WITH 1;

CREATE TABLE projects
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  githubNodeId VARCHAR(127),
  name VARCHAR(127),
  client VARCHAR(255),
  status VARCHAR(127),
  description VARCHAR(4095),
  githubRepo VARCHAR(255),
  startedAt TIMESTAMP,
  closedAt TIMESTAMP
);

CREATE TABLE groups
(
  id BIGINT DEFAULT groups_id_seq.nextval PRIMARY KEY,
  projectId BIGINT REFERENCES projects(id),
  createdAt TIMESTAMP
);

CREATE TABLE types
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  type VARCHAR(255)
);

CREATE TABLE metrics
(
  groupId BIGINT REFERENCES groups(id),
  typeId BIGINT REFERENCES types(id),
  value FLOAT,

  CONSTRAINT pk_metrics PRIMARY KEY (groupId, typeId)
);