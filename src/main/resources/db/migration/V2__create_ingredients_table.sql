CREATE TABLE ingredients
(
    id          INT           NOT NULL,
    name        VARCHAR(255)  NOT NULL,
    price       DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
    category_id INT           NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    UNIQUE INDEX name_UNIQUE (name ASC) VISIBLE,
    INDEX category_id_idx (category_id ASC) VISIBLE,
    CONSTRAINT category_id
        FOREIGN KEY (category_id)
            REFERENCES categories (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);