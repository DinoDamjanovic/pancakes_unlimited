ALTER TABLE ingredients
    ADD COLUMN is_healthy TINYINT NOT NULL DEFAULT 0 AFTER category_id;