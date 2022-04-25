ALTER TABLE pancakes_with_ingredients
    DROP FOREIGN KEY ingredient_id,
    DROP FOREIGN KEY pancake_id;
ALTER TABLE pancakes_with_ingredients
    CHANGE COLUMN pancake_id pancake_id BIGINT NOT NULL,
    CHANGE COLUMN ingredient_id ingredient_id INT NOT NULL;
ALTER TABLE pancakes_with_ingredients
    ADD CONSTRAINT ingredient_id
        FOREIGN KEY (ingredient_id)
            REFERENCES ingredients (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT pancake_id
        FOREIGN KEY (pancake_id)
            REFERENCES pancakes (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;