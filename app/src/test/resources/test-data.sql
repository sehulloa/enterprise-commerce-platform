-- CUSTOMER
--INSERT INTO customers (id, name) VALUES (1, 'Test Customer');

-- BRANCH
--INSERT INTO branches (id, name) VALUES (1, 'Main Branch');

-- CATEGORY
INSERT INTO categories (
    id, name, description, active, created_at, created_by, updated_at, updated_by
)
VALUES (
    1, 'Test Category', 'Test Description', true, NOW(), 'test', NOW(), 'test'
);

-- PRODUCT
INSERT INTO products (
    id, sku, name, description, category_id, active, created_at, created_by, updated_at, updated_by
)
VALUES (
    1, 'SKU-001', 'Test Product', 'Test Product Description', 1, true, NOW(), 'test', NOW(), 'test'
);

-- PRODUCT PRICE (vigente)
INSERT INTO product_prices (
    id, product_id, price, effective_from, effective_to, created_at, created_by, updated_at, updated_by
)
VALUES (
    1, 1, 899.99, NOW(), NULL, NOW(), 'test', NOW(), 'test'
);

-- INVENTORY (stock suficiente)
INSERT INTO inventory_items (
    id, product_id, branch_id, total_quantity, reserved_quantity, active, created_at, created_by, updated_at, updated_by
)
VALUES (
    1, 1, 1, 10, 0, true, NOW(), 'test', NOW(), 'test'
);