-- DROP TABLE IF EXISTS group_members;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS USER_FLAG;
DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS city_seq;
DROP TABLE IF EXISTS groups;
DROP SEQUENCE IF EXISTS group_seq;
DROP TYPE IF EXISTS GROUP_TYPE;
DROP TABLE IF EXISTS projects;
DROP SEQUENCE IF EXISTS project_seq;

CREATE SEQUENCE city_seq START 100000;

CREATE TABLE cities (
  id   INTEGER PRIMARY KEY DEFAULT nextval('city_seq'),
  city TEXT NOT NULL,
  code TEXT NOT NULL
);

CREATE UNIQUE INDEX code_idx ON cities (code);

CREATE TYPE USER_FLAG AS ENUM ('active', 'deleted', 'superuser');

CREATE SEQUENCE user_seq START 100000;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT      NOT NULL,
  email     TEXT      NOT NULL,
  flag      USER_FLAG NOT NULL,
  city_id   INTEGER   NOT NULL,
  FOREIGN KEY (city_id) REFERENCES cities (id)
);

CREATE UNIQUE INDEX email_idx ON users (email);

CREATE SEQUENCE project_seq START 100000;

CREATE TABLE projects (
  id INTEGER PRIMARY KEY DEFAULT nextval('project_seq'),
  project TEXT NOT NULL,
  description TEXT NOT NULL
);

CREATE SEQUENCE group_seq START 100000;

CREATE TYPE GROUP_TYPE AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE TABLE groups (
  id INTEGER PRIMARY KEY DEFAULT nextval('group_seq'),
  name TEXT NOT NULL,
  type GROUP_TYPE NOT NULL,
  project_id INTEGER NOT NULL,
  FOREIGN KEY (project_id) REFERENCES projects (id)
);

/*
CREATE TABLE group_members (
  group_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  FOREIGN KEY (group_id) REFERENCES groups (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
);*/
