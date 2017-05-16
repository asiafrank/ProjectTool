DROP database test;

CREATE database test;

use test;

DROP TABLE game;

CREATE TABLE IF NOT EXISTS game (
  id                         BIGINT AUTO_INCREMENT PRIMARY KEY,
  username                   VARCHAR(255) NOT NULL,
  password                   VARCHAR(255) NOT NULL,
  created_at                 TIMESTAMP,
  updated_at                 TIMESTAMP
);