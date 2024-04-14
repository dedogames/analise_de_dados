-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS summary_by_regions_id_seq;

-- Table Definition
CREATE TABLE "public"."summary_by_regions" (
    "id" int4 NOT NULL DEFAULT nextval('summary_by_regions_id_seq'::regclass),
    "state" varchar(4) NOT NULL,
    "percent" number NOT NULL,
    "products" text NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
     PRIMARY KEY ("id")
);