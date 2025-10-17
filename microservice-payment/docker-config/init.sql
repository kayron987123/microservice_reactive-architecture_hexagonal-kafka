CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO payments (order_id, payment_method, amount, status)
VALUES
    (1, 'CARD', 3850.00, 'PENDING'),
    (2, 'PAYPAL', 250.00, 'SUCCESS');
