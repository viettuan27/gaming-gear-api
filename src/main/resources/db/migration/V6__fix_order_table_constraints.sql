ALTER TABLE orders
    ALTER COLUMN shipping_address TYPE VARCHAR(500),
    ALTER COLUMN note TYPE VARCHAR(500);

ALTER TABLE order_items
    DROP CONSTRAINT order_items_quantity_check,
    ADD CONSTRAINT chk_order_items_quantity CHECK (quantity > 0);
