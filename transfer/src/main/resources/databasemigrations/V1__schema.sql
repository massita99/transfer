SET LOCK_TIMEOUT 100;

DROP TABLE IF EXISTS ACCOUNT;

CREATE TABLE ACCOUNT (
  id        VARCHAR(255) PRIMARY KEY NOT NULL,
  balance   DECIMAL(20, 2) NOT NULL,
  created   TIMESTAMP NOT NULL
);
