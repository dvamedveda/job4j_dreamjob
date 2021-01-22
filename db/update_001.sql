CREATE TABLE photo (
                           id SERIAL PRIMARY KEY,
                           path TEXT
);

ALTER TABLE candidate ADD COLUMN photo integer;