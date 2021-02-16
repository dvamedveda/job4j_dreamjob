CREATE TABLE city
(
    id   SERIAL PRIMARY KEY,
    name varchar(25)
);

insert into city(id, name)
values (0, 'UNKNOWN'),
       (1, 'Москва'),
       (2, 'Владивосток'),
       (3, 'Воронеж');

alter table candidate
    add column
        city_id integer references city (id) default 0;