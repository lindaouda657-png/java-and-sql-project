INSERT INTO statuses (statuse_code, statuse_name) VALUES
('ACTIVE', 'Active'),
('INACTIVE', 'Inactive'),
('PENDING', 'Pending'),
('BLOCKED', 'Blocked');
INSERT INTO permissions (permission_code, statuse_code_fk) VALUES
('ADD_PRODUCT', 'ACTIVE'),
('DELETE_PRODUCT', 'ACTIVE'),
('UPDATE_PRODUCT', 'ACTIVE'),
('VIEW_ORDERS', 'ACTIVE'),
('MANAGE_USERS', 'ACTIVE');
INSERT INTO department
(department_name, statuse_code_fk, city, street_name, building_NO, apartment_NO)
VALUES
('Sales', 'ACTIVE', 'Cairo', 'Nasr Street', '12', '5'),
('HR', 'ACTIVE', 'Giza', 'Tahrir Street', '20', '2'),
('IT', 'ACTIVE', 'Alexandria', 'Sea Road', '15', '7');
INSERT INTO department_permissions
(department_id_fk, permission_id_fk, statuse_id_fk)
VALUES
(1, 1, 'ACTIVE'),
(1, 4, 'ACTIVE'),
(2, 5, 'ACTIVE'),
(3, 3, 'ACTIVE');
INSERT INTO users
(user_name, user_email, statuse_code_fk, department_id_fk,
 city, street_name, building_NO, apartment_NO, salary)
VALUES
('Ahmed Ali', 'ahmed@gmail.com', 'ACTIVE', 1,
 'Cairo', 'Street 1', '10', '3', 12000),
('Sara Mohamed', 'sara@gmail.com', 'ACTIVE', 2,
 'Giza', 'Street 2', '11', '5', 15000),
('Omar Hassan', 'omar@gmail.com', 'ACTIVE', 3,
 'Alexandria', 'Street 3', '7', '1', 18000);
INSERT INTO user_credentials
(user_id_fk, password_hash)
VALUES
(1, 'ahmed123'),
(2, 'sara123'),
(3, 'omar123');
INSERT INTO user_permissions
(user_id_fk, permission_id_fk)
VALUES
(1, 1),
(1, 4),
(2, 5),
(3, 3);
INSERT INTO customers
(customer_name, customer_email, phone_NO, statuse_code_fk,
 city, street_name, building_NO, apartment_NO)
VALUES
('Mona Adel', 'mona@gmail.com', '01011111111', 'ACTIVE',
 'Cairo', 'Garden City', '8', '2'),
('Khaled Samy', 'khaled@gmail.com', '01022222222', 'ACTIVE',
 'Giza', 'Dokki', '14', '6');
INSERT INTO categories
(category_name, statuse_code_fk)
VALUES
('Electronics', 'ACTIVE'),
('Clothes', 'ACTIVE'),
('Accessories', 'ACTIVE');
INSERT INTO discounts
(discounts_code, total_discounts, total_in_percentage)
VALUES
('DISC10', 100.00, 10.00),
('DISC20', 200.00, 20.00);
INSERT INTO products_statuse
(product_statuse_code, state)
VALUES
('AVAILABLE', 'Available'),
('OUT_STOCK', 'Out Of Stock');
INSERT INTO products
(product_code, product_name, unit_price, quantity,
 category_id_fk, product_statuse_id_fk, discounts_code_fk)
VALUES
('P100', 'Laptop HP', 25000.00, 10, 1, 1, 'DISC10'),
('P101', 'iPhone 15', 50000.00, 5, 1, 1, 'DISC20'),
('P102', 'T-Shirt Black', 350.00, 20, 2, 1, NULL);
INSERT INTO product_images
(product_code_fk, image_url, is_primary)
VALUES
('P100', 'hp_laptop.jpg', 1),
('P101', 'iphone15.jpg', 1),
('P102', 'shirt.jpg', 1);
INSERT INTO order_statuse
(order_statuse_code, state)
VALUES
('NEW', 'New Order'),
('SHIPPED', 'Shipped'),
('DELIVERED', 'Delivered');
INSERT INTO orders
(customer_id_fk, user_id_fk, order_statuse_id_fk)
VALUES
(1, 1, 1),
(2, 2, 2);
INSERT INTO order_details
(order_details_code, order_id_pfk, product_code_pfk,
 total_amount_of_product, date)
VALUES
('OD1', 1, 'P100', 25000.00, '2026-05-08'),
('OD2', 2, 'P102', 700.00, '2026-05-08');
INSERT INTO returns
(product_code_fk, order_id_fk, refund, reason, date)
VALUES
('P102', 2, 350.00, 'Wrong Size', '2026-05-08');
INSERT INTO reviews
(review_code, the_topic, rate,
 product_code_fk_u, customer_id_fk_u)
VALUES
('REV1', 'Very good laptop', 5, 'P100', 1),
('REV2', 'Nice quality', 4, 'P102', 2);
INSERT INTO payments
(order_id_fk, total, payment_method, customer_id)
VALUES
(1, 25000.00, 'Credit Card', 1),
(2, 700.00, 'Cash', 2);
INSERT INTO audits
(audit_code, action_type, old_state, new_state,
 user_id_fk, product_code_fk)
VALUES
('AUD1', 'UPDATE_PRODUCT', 'Price 24000', 'Price 25000', 1, 'P100'),
('AUD2', 'ADD_PRODUCT', NULL, 'New Product Added', 3, 'P102');
INSERT INTO cart
(customer_id_fk)
VALUES
(1),
(2);
INSERT INTO cart_items
(cart_id_fk, product_code_fk, quantity)
VALUES
(1, 'P101', 1),
(1, 'P102', 2),
(2, 'P100', 1);
INSERT INTO notifications
(user_id_fk, customer_id_fk, message, is_read)
VALUES
(1, NULL, 'New order received', 0),
(NULL, 1, 'Your order has been shipped', 0),
(2, 2, 'Payment confirmed', 1);


-- the other DML operations--

-- This query  adds a new category called 'Gaming' to the categories table.
INSERT INTO categories (category_name, statuse_code_fk) 
VALUES ('Gaming', 'ACTIVE');

-- This query adds a new status type for our products to the products_statuse table.
INSERT INTO products_statuse (product_statuse_code, state) 
VALUES ('LIMITED_EDITION', 'Limited Edition');

-- This query adds a new customer named 'Ahmed Ali' from Cairo to the customers table.
INSERT INTO customers (customer_name, customer_email, phone_NO, statuse_code_fk, city) 
VALUES ('Ahmed Ali', 'ahmed.ali@email.com', '01023456789', 'ACTIVE', 'Cairo');

-- This query  finds all products that have a unit price higher than 5000.
SELECT * FROM products WHERE unit_price > 5000;

-- This query showes only the orders that are currently marked as 'PENDING'.
SELECT * FROM orders WHERE order_statuse_id_fk = 1;

-- This query  finds customers who live specifically in the city of 'Cairo'.
SELECT customer_name, customer_email FROM customers WHERE city = 'Cairo';

-- This query changes the price of the 'Samsung Galaxy A54' to a new value of 8500.00.
UPDATE products SET unit_price = 8500.00 WHERE product_code = 'PROD001';

-- This query updatesa customer's phone number based on their unique ID.
UPDATE customers SET phone_NO = '01199988877' WHERE customer_id = 1;

-- This query markes all notifications as 'read' for a specific user.
UPDATE notifications SET is_read = 1 WHERE user_id_fk = 2;

-- This query removes a specific review from the database using its unique code.
DELETE FROM reviews WHERE review_code = 'REV001';

-- This query  deletes an item from a shopping cart using its ID.
DELETE FROM cart_items WHERE cart_item_id = 5;

-- This query removes a specific audit log entry from the system.
DELETE FROM audits WHERE audit_code = 'AUD002';

