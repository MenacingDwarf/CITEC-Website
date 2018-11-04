CREATE TABLE projects
(
  id BIGINT AUTO_INCREMENT,
  name VARCHAR(255),

  PRIMARY KEY (id)
);

CREATE TABLE groups
(
  id BIGINT AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  timestamp TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES projects(id)
);

CREATE TABLE metrics
(
  group_id BIGINT NOT NULL,
  type VARCHAR(255),
  value FLOAT,

  CONSTRAINT pk_metric PRIMARY KEY(group_id, type),
  CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES groups(id)
);