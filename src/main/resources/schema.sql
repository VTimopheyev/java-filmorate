CREATE TABLE IF NOT exists "FILMS" (
  "FILM_ID" int generated by default as identity primary key,
  "NAME" varchar,
  "DESCRIPTION" varchar,
  "DURATION" int,
  "RELEASE_DATE" date,
  "MPA_ID" int
);

CREATE TABLE IF NOT EXISTS "GENRES" (
  "GENRE_ID" int PRIMARY KEY NOT NULL,
  "NAME" varchar
);

CREATE TABLE IF NOT exists "USERS" (
  "USER_ID" int generated by default as identity primary key,
  "NAME" varchar,
  "EMAIL" varchar,
  "LOGIN" varchar,
  "BIRTHDATE" date
);

CREATE TABLE IF NOT exists "MPA_RATING" (
  "MPA_ID" int PRIMARY KEY,
  "MPA_NAME" varchar
);

CREATE TABLE IF NOT exists "FILMS_BY_GENRE" (
  "FILMS_BY_GENRE_ID" int generated by default as identity primary key,
  "FILM_ID" int,
  "GENRE_ID" int
);

CREATE TABLE IF NOT exists "LIKES" (
  "LIKE_ID" int generated by default as identity primary key,
  "FILM_ID" int,
  "USER_ID" int
);

CREATE TABLE IF NOT exists "FRIENDSHIP_APPROVED" (
  "FRIENDSHIP_APPROVED_ID" int generated by default as identity primary key,
  "USER_ID" int,
  "FRIEND_ID" int
);

CREATE TABLE IF NOT exists "FRIENDSHIP_REQUESTS" (
  "friendship_request_id" int generated by default as identity primary key,
  "FRIEND_ID" int,
  "USER_ID" int
);

ALTER TABLE "FILMS" ADD FOREIGN KEY ("MPA_ID") REFERENCES "MPA_RATING" ("MPA_ID");

ALTER TABLE "FILMS_BY_GENRE" ADD FOREIGN KEY ("FILM_ID") REFERENCES "FILMS" ("FILM_ID");

ALTER TABLE "LIKES" ADD FOREIGN KEY ("FILM_ID") REFERENCES "FILMS" ("FILM_ID");

ALTER TABLE "LIKES" ADD FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");

ALTER TABLE "FRIENDSHIP_REQUESTS" ADD FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");

ALTER TABLE "FRIENDSHIP_REQUESTS" ADD FOREIGN KEY ("FRIEND_ID") REFERENCES "USERS" ("USER_ID");

ALTER TABLE "FRIENDSHIP_APPROVED" ADD FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");

ALTER TABLE "FRIENDSHIP_APPROVED" ADD FOREIGN KEY ("FRIEND_ID") REFERENCES "USERS" ("USER_ID");

ALTER TABLE "FILMS_BY_GENRE" ADD FOREIGN KEY ("GENRE_ID") REFERENCES "GENRES" ("GENRE_ID");

INSERT INTO MPA_RATING (MPA_ID, MPA_NAME) VALUES(1, 'G');
INSERT INTO MPA_RATING (MPA_ID, MPA_NAME) VALUES(2, 'PG');
INSERT INTO MPA_RATING (MPA_ID, MPA_NAME) VALUES(3, 'PG-13');
INSERT INTO MPA_RATING (MPA_ID, MPA_NAME) VALUES(4, 'R');
INSERT INTO MPA_RATING (MPA_ID, MPA_NAME) VALUES(5, 'NC-17');
INSERT INTO GENRES (GENRE_ID, NAME) VALUES(1, 'Комедия');
INSERT INTO GENRES (GENRE_ID, NAME) VALUES(2, 'Драма');
INSERT INTO GENRES (GENRE_ID, NAME) VALUES(3, 'Мультфильм');
INSERT INTO GENRES (GENRE_ID, NAME) VALUES(4, 'Триллер');
INSERT INTO GENRES (GENRE_ID, NAME) VALUES(5, 'Документальный');
INSERT INTO GENRES (GENRE_ID, NAME) VALUES(6, 'Боевик');
