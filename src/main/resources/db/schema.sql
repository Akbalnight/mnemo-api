CREATE TABLE IF NOT EXISTS schemas (
  id bigint NOT NULL,
  title varchar(255) DEFAULT NULL,
  created_by varchar(255) DEFAULT NULL,
  content text DEFAULT NULL,
  PRIMARY KEY (id)
);
CREATE sequence IF NOT EXISTS schemas_id_seq;
