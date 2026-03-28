CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(120) NOT NULL,
    last_name VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);
