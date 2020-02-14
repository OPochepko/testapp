create table "users"
(
    id         serial primary key,
    username   varchar(16) not null,
    password   varchar(60) not null,
    first_name varchar(16) not null,
    last_name  varchar(16) not null,
    role       varchar     not null,
    status     varchar     not null,
    created_At timestamp
);

create unique index users_id_uindex
    on "users" (id);

create unique index users_username_uindex
    on "users" (username);
