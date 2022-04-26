ALTER TABLE pancakes
    ADD COLUMN order_id BIGINT NULL AFTER id,
    ADD INDEX order_id_idx (order_id ASC) VISIBLE;
;
ALTER TABLE pancakes
    ADD CONSTRAINT order_id
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;
