-- removing primary key as it is not needed anymore after code refactoring
ALTER TABLE pancakes_with_ingredients
    DROP COLUMN id,
    DROP INDEX id_UNIQUE,
    DROP PRIMARY KEY;
;
