create table if not exists users (
    id bigint generated always as identity not null,
    email varchar(100) not null,
    name varchar(250) not null,
    constraint pk_users primary key (id),
    unique (email)
);

create table if not exists items (
    id bigint generated always as identity not null,
    user_id bigint not null,
    description varchar(200),
    count bigint,
    available varchar(50),
    name varchar(100),
    request_id  BIGINT,
    constraint pk_items primary key (id),
    constraint fk_items_to_users foreign key (user_id) references users(id),
    constraint uq_owner_item_name unique (user_id, name)
);

create table if not exists comments (
    id bigint generated always as identity not null,
    item_id bigint,
    author_name varchar(250),
    created timestamp,
    text varchar(1000),
    constraint pk_comments primary key (id),
    constraint items foreign key (item_id) references items(id)
);

create table if not exists bookings (
    id bigint generated always as identity not null,
    user_id bigint not null,
    item_id bigint not null,
    status varchar(200),
    start timestamp,
    finish timestamp,
    constraint pk_bookings primary key (id),
    constraint fk_booking_to_users foreign key (user_id) references users(id),
    constraint fk_booking_to_items foreign key (item_id) references items(id),
    unique (id)
);

create table if not exists item_request (
    id bigint generated always as identity not null,
    user_id bigint,
    description varchar(1000),
    created timestamp,
    constraint pk_item_request primary key (id),
    unique (id)
);
