create sequence if not exists id_sequence;

create table movies
(
    id           bigint primary key,
    name         varchar not null,
    publish_year int     not null
);
