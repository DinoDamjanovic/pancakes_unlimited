CREATE TABLE categories
(
    id   INT          NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    UNIQUE INDEX name_UNIQUE (name ASC) VISIBLE
);