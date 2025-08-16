-- USERS
INSERT INTO users (id, username, password, email, full_name, role, enabled) VALUES
(1, 'admin', '$2a$10$M1bJ9nJcJt7QxF2ZQyR0Oe2g2zW9mKpP3n8M8hQ0zY1p3R7mWQ4me', 'admin@example.com', 'Administrator', 'ROLE_ADMIN', true)
ON CONFLICT DO NOTHING;

INSERT INTO users (id, username, password, email, full_name, role, enabled) VALUES
(2, 'user', '$2a$10$M1bJ9nJcJt7QxF2ZQyR0Oe2g2zW9mKpP3n8M8hQ0zY1p3R7mWQ4me', 'user@example.com', 'Sample User', 'ROLE_CUSTOMER', true)
ON CONFLICT DO NOTHING;

-- CATEGORIES
INSERT INTO categories (id, name, description) VALUES
(1, 'Electronics', 'Gadgets and devices'),
(2, 'Clothing', 'Apparel and fashion')
ON CONFLICT DO NOTHING;

-- PRODUCTS
INSERT INTO products (id, name, price, stock, image_url, category_id) VALUES
(1, 'Smartphone X', 12000000, 50, NULL, 1),
(2, 'Laptop Pro 14', 28000000, 20, NULL, 1),
(3, 'T-Shirt Basic', 150000, 200, NULL, 2),
(4, 'Headphones', 850000, 80, NULL, 1)
ON CONFLICT DO NOTHING;

-- REVIEWS (sample)
INSERT INTO reviews (user_id, product_id, rating, comment, created_at) VALUES
(2, 1, 5, 'Great phone!', now())
ON CONFLICT DO NOTHING;

-- CART for user id=2
INSERT INTO carts (id, user_id, created_at) VALUES
(1, 2, now())
ON CONFLICT DO NOTHING;

INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(1, 1, 1),
(1, 3, 2)
ON CONFLICT DO NOTHING;

-- SAMPLE ORDER (for demo) for user id=2
INSERT INTO orders (id, user_id, status, total_price, created_at) VALUES
(1, 2, 'PENDING', 12150000, now())
ON CONFLICT DO NOTHING;

INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 12000000),
(1, 3, 1, 150000)
ON CONFLICT DO NOTHING;
