CREATE TABLE IF NOT EXISTS inventories (
    id SERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO inventories (product_id, quantity)
VALUES
    (1, 20),
    (2, 50),
    (3, 15),
    (4, 40),
    (5, 10);