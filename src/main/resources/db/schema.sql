CREATE TABLE IF NOT EXISTS {schemas.table} (
  id bigint NOT NULL,
  title varchar(255) DEFAULT NULL,
  created_by varchar(255) DEFAULT NULL,
  content text DEFAULT NULL,
  PRIMARY KEY (id)
);
CREATE sequence IF NOT EXISTS {schemas.table}_id_seq;

-- 02.06.2019
-- add unique index to schemas
-- set title not null
-- add new column schema_type
CREATE UNIQUE INDEX IF NOT EXISTS schema_title_idx ON {schemas.table} (title);
ALTER TABLE {schemas.table} ALTER COLUMN title SET NOT NULL;
ALTER TABLE {schemas.table} ADD COLUMN schema_type varchar(255);


