CREATE TABLE projects (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255),
  githubRepo VARCHAR(255),
  node_id VARCHAR(255),

  PRIMARY KEY (id)
);

CREATE TABLE groups (
  id BIGINT NOT NULL AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  timestamp TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES projects(id)
);

CREATE TABLE metricTypes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255),

  PRIMARY KEY (id)
);

CREATE TABLE metrics (
  type_id BIGINT NOT NULL,
  group_id BIGINT NOT NULL,
  value FLOAT,

  CONSTRAINT pk_metric PRIMARY KEY(type_id, group_id),
  CONSTRAINT fk_type FOREIGN KEY (type_id) REFERENCES metricTypes(id),
  CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES groups(id)
);