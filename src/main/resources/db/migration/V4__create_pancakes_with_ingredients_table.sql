CREATE TABLE pancakes_with_ingredients
(
    pancake_id    BIGINT NULL,
    ingredient_id INT    NULL,
    INDEX pancake_id_idx (pancake_id ASC) VISIBLE,
    INDEX ingredient_id_idx (ingredient_id ASC) VISIBLE,
    CONSTRAINT pancake_id
        FOREIGN KEY (pancake_id)
            REFERENCES pancakes (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT ingredient_id
        FOREIGN KEY (ingredient_id)
            REFERENCES ingredients (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);