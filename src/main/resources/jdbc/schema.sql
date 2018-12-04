DROP TABLE    IF EXISTS metric;
DROP TABLE    IF EXISTS metric_group;
DROP TABLE    IF EXISTS tag;
DROP TABLE    IF EXISTS project;
DROP SEQUENCE IF EXISTS metric_group_id_seq;

CREATE SEQUENCE metric_group_id_seq START WITH 1;

CREATE TABLE project
(
  id SERIAL PRIMARY KEY,
  githubNodeId VARCHAR(127),
  name VARCHAR(127),
  client VARCHAR(255),
  members VARCHAR(4095),
  description VARCHAR(4095),
  githubRepo VARCHAR(255),
  status SMALLINT,
  startedAt TIMESTAMP,
  closedAt TIMESTAMP
);

CREATE TABLE tag
(
  projectId BIGINT REFERENCES project(id),
  tag VARCHAR(255),

  CONSTRAINT pk_tag PRIMARY KEY (projectId, tag)
);

CREATE TABLE metric_group
(
  id BIGINT NOT NULL DEFAULT nextval('metric_group_id_seq') PRIMARY KEY,
  projectId BIGINT REFERENCES project(id),
  createdAt TIMESTAMP
);

CREATE TABLE metric
(
  groupId BIGINT REFERENCES metric_group(id),
  type VARCHAR(255),
  value FLOAT,

  CONSTRAINT pk_metric PRIMARY KEY (groupId, type)
);