CREATE TABLE IF NOT EXISTS public.mnemo_schemas (
  id bigint NOT NULL,
  title varchar(255) DEFAULT NULL,
  created_by varchar(255) DEFAULT NULL,
  content text DEFAULT NULL,
  PRIMARY KEY (id)
);
CREATE sequence IF NOT EXISTS public.mnemo_schemas_id_seq;

-- 02.06.2019
-- add unique index to schemas
-- set title not null
-- add new column schema_type
CREATE UNIQUE INDEX IF NOT EXISTS schema_title_idx ON public.mnemo_schemas (title);
ALTER TABLE public.mnemo_schemas ALTER COLUMN title SET NOT NULL;
ALTER TABLE public.mnemo_schemas ADD COLUMN schema_type varchar(255);


