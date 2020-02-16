create table "users"
(
    id         serial primary key UNIQUE,
    username   varchar(16) not null UNIQUE,
    password   varchar(60) not null,
    first_name varchar(16) not null,
    last_name  varchar(16) not null,
    role       varchar     not null,
    status     varchar     not null,
    created_At timestamp
);

