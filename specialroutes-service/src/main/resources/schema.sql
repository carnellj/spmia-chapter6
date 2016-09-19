DROP TABLE IF EXISTS abtesting;

CREATE TABLE abtesting (
  service_name      VARCHAR(100) PRIMARY KEY NOT NULL,
  endpoint          VARCHAR(100) NOT NULL,
  weight            INT);


INSERT INTO abtesting (service_name,  endpoint, weight) VALUES ('organizationservice', 'http://orgservice-new:8087',2);
