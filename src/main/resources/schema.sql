create table if not exists users (
    id bigint generated always as identity not null primary key,
    email varchar(50) not null,
    name varchar(250) not null,
    unique(email)
);

create table if not exists items (
    id bigint generated always as identity not null primary key,
    name varchar(50),
    description varchar(250),
    is_available varchar(50),
    owner_id bigint not null,
    request_id bigint,
    constraint fk_items_to_users foreign key (owner_id) references users(id),
    unique(id)
);

create table if not exists comments (
    id bigint generated always as identity not null primary key,
    item_id bigint,
    author_id bigint,
    created timestamp,
    text varchar(250),
    constraint fk_comments_to_items foreign key (item_id) references items(id),
    constraint fk_comments_to_users foreign key (author_id) references users(id),
    unique(id)
);

create table if not exists bookings (
    id bigint generated always as identity not null primary key,
    booker_id bigint not null,
    item_id bigint not null,
    status varchar(50),
    start_time timestamp,
    end_time timestamp,
    constraint fk_booking_to_users foreign key (booker_id) references users(id),
    constraint fk_booking_to_items foreign key (item_id) references items(id),
    unique(id)
);