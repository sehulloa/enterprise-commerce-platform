INSERT INTO roles (name, description, active, created_at, created_by)
VALUES
    ('ADMIN', 'System administrator', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('SALES_AGENT', 'Sales agent role', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('INVENTORY_MANAGER', 'Inventory manager role', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('PAYMENT_MANAGER', 'Payment manager role', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('AUDITOR', 'Auditor role', TRUE, CURRENT_TIMESTAMP, 'system');

INSERT INTO permissions (name, description, active, created_at, created_by)
VALUES
    ('USER_MANAGE', 'Manage users', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('ROLE_MANAGE', 'Manage roles', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('PERMISSION_MANAGE', 'Manage permissions', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('CUSTOMER_VIEW', 'View customers', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('CUSTOMER_CREATE', 'Create customers', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('PRODUCT_VIEW', 'View products', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('ORDER_VIEW', 'View orders', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('ORDER_CREATE', 'Create orders', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('INVENTORY_VIEW', 'View inventory', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('INVENTORY_ADJUST', 'Adjust inventory', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('PAYMENT_VIEW', 'View payments', TRUE, CURRENT_TIMESTAMP, 'system'),
    ('PAYMENT_REGISTER', 'Register payments', TRUE, CURRENT_TIMESTAMP, 'system');

INSERT INTO users (username, email, password, enabled, locked, status, created_at, created_by)
VALUES
    ('admin', 'admin@local.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', TRUE, FALSE, 'ACTIVE', CURRENT_TIMESTAMP, 'system');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin'
  AND r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN';
