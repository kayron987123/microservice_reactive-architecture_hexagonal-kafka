CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO products (name, description, price, category)
VALUES
    ('Laptop Lenovo', 'Laptop de alto rendimiento', 3500.00, 'Tecnología'),
    ('Mouse Logitech', 'Mouse inalámbrico', 120.00, 'Accesorios'),
    ('Monitor Samsung', 'Monitor 27 pulgadas 144Hz', 950.00, 'Pantallas'),
    ('Teclado Mecánico', 'Teclado retroiluminado RGB', 250.00, 'Accesorios'),
    ('Impresora HP', 'Impresora multifuncional', 750.00, 'Oficina');