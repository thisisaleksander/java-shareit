create table if not exists users (
    id bigint generated always as identity not null primary key,
    email varchar(50) not null,
    name varchar(250) not null,
    unique (email)
);

create table if not exists items (
    id bigint generated always as identity not null primary key,
    user_id bigint not null,
    description varchar(200),
    count bigint,
    available varchar(50),
    name varchar(100),
    constraint fk_items_to_users foreign key (user_id) references users(id),
    unique (id)
);

create table if not exists comments (
    id bigint generated always as identity not null primary key,
    item_id bigint,
    author_name varchar(250),
    created timestamp,
    text varchar(1000),
    constraint items foreign key (item_id) references items(id)
);

create table if not exists bookings (
    id bigint generated always as identity not null primary key,
    user_id bigint not null,
    item_id bigint not null,
    status varchar(200),
    start timestamp,
    finish timestamp,
    constraint fk_booking_to_users foreign key (user_id) references users(id),
    constraint fk_booking_to_items foreign key (item_id) references items(id),
    UNIQUE (id)
);