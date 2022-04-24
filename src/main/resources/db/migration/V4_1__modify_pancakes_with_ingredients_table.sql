-- adding PK because all JPA entities must have PK
ALTER TABLE pancakes_with_ingredients
    ADD COLUMN id BIGINT NOT NULL AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE;