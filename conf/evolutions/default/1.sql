# --- !Ups

CREATE TABLE "payments" (
    "id" SERIAL PRIMARY KEY,
    "name" varchar,
    "amount" bigint,
    "card_no" bigint,
    "exp_month" bigint,
    "exp_year" bigint,
    "cvv_no" bigint,
    "token" varchar,
    "status" varchar,
    "p_date" varchar,
    "fee" bigint,
    "total" bigint,
    "trans_id" varchar
);

CREATE TABLE "conference" (
    "id" SERIAL PRIMARY KEY,
    "name" varchar,
    "email" varchar,
    "description" varchar,
    "from" varchar,
    "to" varchar,
    "place" varchar
);

INSERT INTO "conference" values (1,'Scala Days', 'scaladays@gmail.com', 'Scala Days Conference', 'April 18th 2017','April 21st 2017','Chicago');
INSERT INTO "conference" values (2,'Spark Summit', 'sparksummit@spark.com', 'Spark Summit Conference', 'FEBRUARY 7th, 2017', 'FEBRUARY 9th, 2017', 'Boston');


# --- !Downs

DROP TABLE "payments";
DROP TABLE "conference";
