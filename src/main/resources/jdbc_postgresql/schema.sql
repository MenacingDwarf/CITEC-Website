DROP TABLE    IF EXISTS metric;
DROP TABLE    IF EXISTS metric_type;
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
  description VARCHAR(4095),
  githubRepo VARCHAR(255),
  difficulty VARCHAR(63),
  status SMALLINT,
  startedAt TIMESTAMP,
  closedAt TIMESTAMP
);

CREATE TABLE tag
(
  projectId BIGINT REFERENCES project(id) ON DELETE CASCADE,
  tag VARCHAR(255),
  CONSTRAINT pk_tag PRIMARY KEY (projectId, tag)
);

CREATE TABLE metric_group
(
  id BIGINT NOT NULL DEFAULT nextval('metric_group_id_seq') PRIMARY KEY,
  projectId BIGINT REFERENCES project(id) ON DELETE CASCADE,
  createdAt TIMESTAMP
);

CREATE TABLE metric_type
(
  id SMALLSERIAL PRIMARY KEY,
  type VARCHAR(127)
);

CREATE TABLE metric
(
  groupId BIGINT REFERENCES metric_group(id) ON DELETE CASCADE,
  typeId SMALLINT REFERENCES metric_type(id),
  value FLOAT,
  CONSTRAINT pk_metric PRIMARY KEY (groupId, typeId)
);

