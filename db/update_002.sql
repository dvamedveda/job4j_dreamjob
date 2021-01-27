alter table photo
    add column
        user_id integer;

alter table candidate
    drop column photo;