ALTER TABLE orders
    ADD COLUMN total_price DECIMAL(7, 2) NOT NULL DEFAULT 0.00 AFTER creation_time;
