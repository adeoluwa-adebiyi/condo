CREATE EXTENSION "pgcrypto";

DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS app_user;
DROP TABLE IF EXISTS apartment_type;
DROP TABLE IF EXISTS apartment;
DROP TABLE IF EXISTS apartment_media;
DROP TABLE IF EXISTS offer_type;
DROP TABLE IF EXISTS offer;

CREATE TABLE IF NOT EXISTS location(
  id SERIAL,
  longitude NUMERIC,
  latitude NUMERIC,
  postcode VARCHAR(8) NOT NULL,
  city VARCHAR(100) NOT NULL,
  address VARCHAR(250) NOT NULL,
  country VARCHAR(250) NOT NULL,
  ref UUID NOT NULL DEFAULT(gen_random_uuid()),
  PRIMARY KEY(id),
  UNIQUE(country,address,city,postcode)
);

CREATE TABLE IF NOT EXISTS app_user(
  id SERIAL,
  firstname VARCHAR(20) NOT NULL,
  lastname VARCHAR(20) NOT NULL,
  dob DATE NOT NULL,
  ref UUID NOT NULL DEFAULT(gen_random_uuid()),
  PRIMARY KEY(id)
);

CREATE INDEX on app_user(firstname,lastname);
CREATE INDEX on app_user(dob);

CREATE TABLE IF NOT EXISTS apartment_type(
  id SERIAL,
  type VARCHAR(20) UNIQUE,
  ref UUID NOT NULL DEFAULT(gen_random_uuid()),
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS apartment(
  id SERIAL,
  owner BIGINT NOT NULL,
  name VARCHAR(20) NOT NULL,
  rooms INTEGER,
  type INTEGER,
  offer BIGINT,
  ref UUID NOT NULL DEFAULT(gen_random_uuid()),
  location BIGINT NOT NULL,
  has_sale BOOL DEFAULT(false),
  PRIMARY KEY(id),
  FOREIGN KEY(owner) REFERENCES app_user(id) ON DELETE CASCADE,
  FOREIGN KEY(location) REFERENCES location(id) ON DELETE SET NULL
);

CREATE INDEX on location(country,city);
CREATE INDEX on location(country,city,address);
CREATE INDEX on location(postcode);

CREATE INDEX on apartment(owner);
CREATE INDEX on apartment(rooms);
CREATE INDEX on apartment(type);

CREATE TABLE IF NOT EXISTS offer_type(
  id SERIAL,
  name VARCHAR(40) NOT NULL,
  type VARCHAR(20) UNIQUE,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS offer(
  id SERIAL,
  amount BIGINT NOT NULL,
  has_discount BOOL NOT NULL DEFAULT(false),
  discount BIGINT NOT NULL DEFAULT(0),
  type BIGINT NOT NULL,
  description TEXT NOT NULL DEFAULT('<Please add a description for your offer>'),
  PRIMARY KEY(id),
  FOREIGN KEY(type) REFERENCES offer_type(id)
);

CREATE TABLE IF NOT EXISTS apartment_media(
  id SERIAL,
  ref UUID NOT NULL DEFAULT(gen_random_uuid()),
  offer BIGINT NOT NULL,
  type VARCHAR(20),
  content TEXT,
  PRIMARY KEY(id),
  FOREIGN KEY(offer) references offer
);


CREATE INDEX on offer(id);
CREATE INDEX on offer(amount);
CREATE INDEX on offer(type);