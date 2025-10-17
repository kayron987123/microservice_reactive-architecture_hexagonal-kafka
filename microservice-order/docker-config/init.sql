CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
    );

INSERT INTO orders (customer_name, total_amount, status)
VALUES
    ('Juan Pérez', 3850.00, 'PENDING'),
    ('María López', 250.00, 'PAID');

INSERT INTO order_items (order_id, product_id, quantity, price)
VALUES
    (1, 1, 1, 3500.00),
    (1, 2, 1, 120.00),
    (2, 4, 1, 250.00);
