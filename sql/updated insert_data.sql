SET SQL_SAFE_UPDATES = 0;
set foreign_key_checks=0;
USE online_shopping1;

DELETE FROM user_permissions;
DELETE FROM department_permissions;
DELETE FROM permissions;

INSERT INTO permissions (permission_code, statuse_code_fk) VALUES
('MANAGE_PRODUCTS',   'ACTIVE'),
('MANAGE_ORDERS',     'ACTIVE'),
('MANAGE_CUSTOMERS',  'ACTIVE'),
('MANAGE_PAYMENTS',   'ACTIVE'),
('MANAGE_CATEGORIES', 'ACTIVE'),
('MANAGE_REVIEWS',    'ACTIVE'),
('MANAGE_EMPLOYEES',  'ACTIVE');

CREATE TABLE IF NOT EXISTS customer_credentials (
    cred_id        INT          NOT NULL AUTO_INCREMENT,
    customer_id_fk INT          NOT NULL,
    password_hash  VARCHAR(255) NOT NULL,
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cred_id),
    UNIQUE KEY uq_cc_customer (customer_id_fk),
    CONSTRAINT fk_cc_customer FOREIGN KEY (customer_id_fk) REFERENCES customers (customer_id)
) ;

INSERT IGNORE INTO customer_credentials (customer_id_fk, password_hash)
SELECT customer_id, 'mona123' FROM customers WHERE customer_email = 'mona@gmail.com';

-- Remove the old misnamed permission codes
DELETE FROM user_permissions WHERE permission_id_fk IN (
    SELECT permission_id FROM permissions 
    WHERE permission_code IN ('ADD_PRODUCT','DELETE_PRODUCT','UPDATE_PRODUCT','VIEW_ORDERS','MANAGE_USERS')
);
DELETE FROM permissions WHERE permission_code IN ('ADD_PRODUCT','DELETE_PRODUCT','UPDATE_PRODUCT','VIEW_ORDERS','MANAGE_USERS');

-- Insert the correct ones
INSERT IGNORE INTO permissions (permission_code, statuse_code_fk) VALUES
('MANAGE_PRODUCTS',   'ACTIVE'),
('MANAGE_ORDERS',     'ACTIVE'),
('MANAGE_CUSTOMERS',  'ACTIVE'),
('MANAGE_PAYMENTS',   'ACTIVE'),
('MANAGE_CATEGORIES', 'ACTIVE'),
('MANAGE_REVIEWS',    'ACTIVE'),
('MANAGE_EMPLOYEES',  'ACTIVE');

-- Re-grant to employees using the correct codes
INSERT IGNORE INTO user_permissions (user_id_fk, permission_id_fk)
SELECT u.user_id, p.permission_id FROM users u, permissions p
WHERE u.user_email = 'ahmed@shop.com' AND p.permission_code IN ('MANAGE_PRODUCTS','MANAGE_ORDERS');

INSERT IGNORE INTO user_permissions (user_id_fk, permission_id_fk)
SELECT u.user_id, p.permission_id FROM users u, permissions p
WHERE u.user_email = 'sara@shop.com' AND p.permission_code = 'MANAGE_EMPLOYEES';

INSERT IGNORE INTO user_permissions (user_id_fk, permission_id_fk)
SELECT u.user_id, p.permission_id FROM users u, permissions p
WHERE u.user_email = 'omar@shop.com' AND p.permission_code IN ('MANAGE_CATEGORIES','MANAGE_REVIEWS');

INSERT IGNORE INTO customer_credentials (customer_id_fk, password_hash)
SELECT customer_id, 'khaled123' FROM customers WHERE customer_email = 'khaled@gmail.com';

SET SQL_SAFE_UPDATES = 1;