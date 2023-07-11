CREATE TABLE IF NOT EXISTS users
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    email   varchar(100) NOT NULL,
    name    varchar(250) NOT NULL,
    UNIQUE  (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    description varchar(200),
    count       BIGINT,
    available   varchar(50),
    name        varchar(100),
    request_id  BIGINT,
    CONSTRAINT  fk_items_to_users FOREIGN KEY(user_id) REFERENCES users(id),
    UNIQUE      (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    item_id     BIGINT,
    author_name varchar(250),
    created     timestamp,
    text        VARCHAR(1000),
    CONSTRAINT  items FOREIGN KEY(item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    item_id     BIGINT NOT NULL,
    status      varchar(200),
    start       timestamp,
    finish      timestamp,
    CONSTRAINT  fk_booking_to_users FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT  fk_booking_to_items FOREIGN KEY(item_id) REFERENCES items(id),
    UNIQUE      (id)
);

CREATE TABLE IF NOT EXISTS item_request
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    user_id     BIGINT,

    description varchar(1000),
    created     timestamp,

    UNIQUE      (id)
);

