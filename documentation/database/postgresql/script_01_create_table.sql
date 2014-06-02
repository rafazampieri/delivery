-- Table: maps
CREATE TABLE maps
(
  id bigint NOT NULL,
  name character varying(255) NOT NULL,
  CONSTRAINT maps_pk PRIMARY KEY (id)
);

-- Table: map_location
CREATE TABLE map_location
(
  id bigint NOT NULL,
  location character varying(255) NOT NULL,
  json_edges text,
  maps_id bigint not null,
  CONSTRAINT map_location_pk PRIMARY KEY (id),
  CONSTRAINT map_location_fk_01 FOREIGN KEY (maps_id)
      REFERENCES maps (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE seq_maps INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
CREATE SEQUENCE seq_map_location INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;