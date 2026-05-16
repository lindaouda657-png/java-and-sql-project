CREATE DATABASE  online_shopping1;
USE online_shopping1;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE statuses (
    statuse_code  VARCHAR(20) ,
    statuse_name  VARCHAR(100) NOT NULL,
    PRIMARY KEY (statuse_code)
) ;

CREATE TABLE permissions (
    permission_id   INT AUTO_INCREMENT,
    permission_code VARCHAR(50)  NOT NULL,
    statuse_code_fk VARCHAR(20) ,
    PRIMARY KEY (permission_id),
    UNIQUE KEY uq_permission_code (permission_code),
    CONSTRAINT fk_permissions_statuse FOREIGN KEY (statuse_code_fk) REFERENCES statuses (statuse_code)
);
CREATE TABLE department (
    department_id   INT AUTO_INCREMENT,
    department_name VARCHAR(150) NOT NULL,
    statuse_code_fk VARCHAR(20) ,
    city            VARCHAR(100),
    street_name     VARCHAR(150),
    building_NO     VARCHAR(20),
    apartment_NO    VARCHAR(20),
    PRIMARY KEY (department_id),
    CONSTRAINT fk_department_statuse FOREIGN KEY (statuse_code_fk) REFERENCES statuses (statuse_code)
) ;
CREATE TABLE department_permissions (
    department_per_id INT AUTO_INCREMENT,
    department_id_fk  INT ,
    permission_id_fk  INT ,
    statuse_id_fk     VARCHAR(20),
    PRIMARY KEY (department_per_id,department_id_fk, permission_id_fk),
    CONSTRAINT fk_dp_department FOREIGN KEY (department_id_fk) REFERENCES department (department_id),
    CONSTRAINT fk_dp_permission FOREIGN KEY (permission_id_fk) REFERENCES permissions (permission_id),
    CONSTRAINT fk_dp_statuse    FOREIGN KEY (statuse_id_fk)    REFERENCES statuses (statuse_code)
);
CREATE TABLE users (
    user_id          INT AUTO_INCREMENT,
    user_name        VARCHAR(100) NOT NULL,
    user_email       VARCHAR(255) NOT NULL,
    statuse_code_fk  VARCHAR(20) ,
    department_id_fk INT,
    city             VARCHAR(100),
    street_name      VARCHAR(150),
    building_NO      VARCHAR(20),
    apartment_NO     VARCHAR(20),
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_user_email (user_email),
    CONSTRAINT fk_users_statuse    FOREIGN KEY (statuse_code_fk)  REFERENCES statuses    (statuse_code),
    CONSTRAINT fk_users_department FOREIGN KEY (department_id_fk) REFERENCES department  (department_id)
) ;
CREATE TABLE user_credentials (
    credential_id INT  AUTO_INCREMENT,
    user_id_fk    INT,
    password_hash VARCHAR(255) NOT NULL,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (credential_id),
    UNIQUE KEY uq_uc_user (user_id_fk),
    CONSTRAINT fk_uc_user FOREIGN KEY (user_id_fk) REFERENCES users (user_id)
);
CREATE TABLE user_permissions (
    user_permission_id INT AUTO_INCREMENT,
    user_id_fk         INT ,
    permission_id_fk   INT ,
    granted_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_permission_id,user_id_fk, permission_id_fk),
    CONSTRAINT fk_up_user       FOREIGN KEY (user_id_fk)       REFERENCES users       (user_id)        ON DELETE CASCADE,
    CONSTRAINT fk_up_permission FOREIGN KEY (permission_id_fk) REFERENCES permissions (permission_id)  ON DELETE CASCADE
) ;
CREATE TABLE customers (
    customer_id    INT AUTO_INCREMENT,
    customer_name  VARCHAR(150) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    phone_NO       VARCHAR(30),
    statuse_code_fk VARCHAR(20),
    city           VARCHAR(100),
    street_name    VARCHAR(150),
    building_NO    VARCHAR(20),
    apartment_NO   VARCHAR(20),
    PRIMARY KEY (customer_id),
    UNIQUE KEY uq_customer_email (customer_email),
    CONSTRAINT fk_customers_statuse FOREIGN KEY (statuse_code_fk) REFERENCES statuses (statuse_code)
) ;
CREATE TABLE categories (
    category_id   INT AUTO_INCREMENT,
    category_name VARCHAR(150) NOT NULL,
    statuse_code_fk VARCHAR(20) ,
    PRIMARY KEY (category_id),
    CONSTRAINT fk_categories_statuse FOREIGN KEY (statuse_code_fk) REFERENCES statuses (statuse_code)
);
CREATE TABLE discounts (
    discounts_code      VARCHAR(50) ,
    total_discounts     DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_in_percentage DECIMAL(5,2),
     category_id_fk   INT AUTO_INCREMENT,
    PRIMARY KEY (discounts_code),
      CONSTRAINT fk_discounts_category
        FOREIGN KEY (category_id_fk) REFERENCES categories (category_id)
);
CREATE TABLE discounts_products(
    discounts_code VARCHAR(50) ,
    product_code   varchar(50) ,
    PRIMARY KEY (discounts_code, product_code),
    CONSTRAINT fk_dc_discount
        FOREIGN KEY (discounts_code) REFERENCES discounts (discounts_code),
    CONSTRAINT fk_dc_product
        FOREIGN KEY (product_code) REFERENCES products (product_code)
);
CREATE TABLE products_statuse (
    product_statuse_id   INT  AUTO_INCREMENT,
    product_statuse_code VARCHAR(20)  NOT NULL,
    state                VARCHAR(100) NOT NULL,
    PRIMARY KEY (product_statuse_id),
    UNIQUE KEY uq_ps_code (product_statuse_code)
) ;

CREATE TABLE products (
    product_code          VARCHAR(50) ,
    product_name          VARCHAR(200)  NOT NULL,
    unit_price            DECIMAL(12,2) NOT NULL,
    quantity              INT           NOT NULL DEFAULT 0,
    category_id_fk        INT ,
    product_statuse_id_fk INT ,
    discounts_code_fk     VARCHAR(50),
    PRIMARY KEY (product_code),
     CONSTRAINT fk_products_category
        FOREIGN KEY (category_id_fk) REFERENCES categories (category_id),
    CONSTRAINT fk_products_statuse
        FOREIGN KEY (product_statuse_id_fk) REFERENCES products_statuse (product_statuse_id),
    CONSTRAINT fk_products_discount
        FOREIGN KEY (discounts_code_fk) REFERENCES discounts (discounts_code)

);
CREATE TABLE product_images (
    image_id         INT AUTO_INCREMENT,
    product_code_fk  VARCHAR(50) ,
    image_url        VARCHAR(500) NOT NULL,
    is_primary       INT NOT NULL DEFAULT 0,
    uploaded_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (image_id),
    CONSTRAINT fk_pi_product FOREIGN KEY (product_code_fk) REFERENCES products (product_code)
);
CREATE TABLE order_statuse (
    order_statuse_id   INT AUTO_INCREMENT,
    order_statuse_code VARCHAR(20)  NOT NULL,
    state              VARCHAR(100) NOT NULL,
    PRIMARY KEY (order_statuse_id),
    UNIQUE KEY uq_os_code (order_statuse_code)
) ;
CREATE TABLE orders (
    order_id            INT AUTO_INCREMENT,
    customer_id_fk      INT,
    user_id_fk          INT ,
    order_statuse_id_fk INT ,
    PRIMARY KEY (order_id),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id_fk)      REFERENCES customers    (customer_id),
    CONSTRAINT fk_orders_user     FOREIGN KEY (user_id_fk)          REFERENCES users        (user_id),
    CONSTRAINT fk_orders_statuse  FOREIGN KEY (order_statuse_id_fk) REFERENCES order_statuse (order_statuse_id)
);
CREATE TABLE order_details (
    order_details_code      VARCHAR(50) ,
    order_id_pfk            INT           NOT NULL,
    product_code_pfk        VARCHAR(50)   NOT NULL,
    total_amount_of_product DECIMAL(14,2) NOT NULL,
    date                    DATE          NOT NULL,
    PRIMARY KEY (order_details_code,  order_id_pfk , order_id_pfk   ),
    UNIQUE KEY uq_od_composite (order_id_pfk, product_code_pfk),
    CONSTRAINT fk_od_order   FOREIGN KEY (order_id_pfk)      REFERENCES orders   (order_id),
    CONSTRAINT fk_od_product FOREIGN KEY (product_code_pfk)  REFERENCES products (product_code)
);
CREATE TABLE returns (
    return_id       INT AUTO_INCREMENT,
    product_code_fk VARCHAR(50)  ,
    order_id_fk     INT ,
    refund          DECIMAL(12,2),
    reason          TEXT,
    date            DATE         NOT NULL,
    PRIMARY KEY (return_id),
    CONSTRAINT fk_returns_product FOREIGN KEY (product_code_fk) REFERENCES products (product_code),
    CONSTRAINT fk_returns_order   FOREIGN KEY (order_id_fk)     REFERENCES orders   (order_id)
);
CREATE TABLE reviews (
    review_code       VARCHAR(50) ,
    the_topic         TEXT,
    rate              INT     NOT NULL CHECK (rate BETWEEN 1 AND 5),
    product_code_fk_u VARCHAR(50) ,
    customer_id_fk_u  INT ,
    PRIMARY KEY (review_code),
    CONSTRAINT fk_reviews_product  FOREIGN KEY (product_code_fk_u) REFERENCES products  (product_code),
    CONSTRAINT fk_reviews_customer FOREIGN KEY (customer_id_fk_u)  REFERENCES customers (customer_id)
) ;
CREATE TABLE payments (
    payment_id     INT  AUTO_INCREMENT,
    order_id_fk    INT ,
    total          DECIMAL(14,2) NOT NULL,
    payment_method VARCHAR(50)   NOT NULL,
	customer_id         INT ,   
     order_id            INT,
    PRIMARY KEY (payment_id),
     CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT fk_payment_customers
        FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
);
CREATE TABLE audits (
    audit_code      VARCHAR(50) ,
    action_type     VARCHAR(100) NOT NULL,
    old_state       TEXT,
    new_state       TEXT,
    date            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id_fk      INT,
    product_code_fk VARCHAR(50),
    PRIMARY KEY (audit_code),
    CONSTRAINT fk_audits_user    FOREIGN KEY (user_id_fk)      REFERENCES users    (user_id),
    CONSTRAINT fk_audits_product FOREIGN KEY (product_code_fk) REFERENCES products (product_code)
);
CREATE TABLE cart (
    cart_id        INT AUTO_INCREMENT,
    customer_id_fk INT,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_id),
    CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id_fk) REFERENCES customers (customer_id)
) ;
CREATE TABLE cart_items (
    cart_item_id    INT AUTO_INCREMENT,
    cart_id_fk      INT,
    product_code_fk VARCHAR(50),
    quantity        INT         NOT NULL DEFAULT 1,
    added_at        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_item_id),
    CONSTRAINT fk_ci_cart    FOREIGN KEY (cart_id_fk)      REFERENCES cart     (cart_id),
    CONSTRAINT fk_ci_product FOREIGN KEY (product_code_fk) REFERENCES products (product_code)
);
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT,
    user_id_fk      INT,
    customer_id_fk  INT,
    message         TEXT     NOT NULL,
    is_read         INT(1) NOT NULL DEFAULT 0,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (notification_id),
    CONSTRAINT fk_notif_user     FOREIGN KEY (user_id_fk)     REFERENCES users      (user_id),
    CONSTRAINT fk_notif_customer FOREIGN KEY (customer_id_fk) REFERENCES customers  (customer_id)
);
SET FOREIGN_KEY_CHECKS = 1;

CREATE INDEX idx_products_category      ON products      (category_id_fk);
CREATE INDEX idx_products_statuse       ON products      (product_statuse_id_fk);
CREATE INDEX idx_orders_customer        ON orders        (customer_id_fk);
CREATE INDEX idx_orders_user            ON orders        (user_id_fk);
CREATE INDEX idx_orders_statuse         ON orders        (order_statuse_id_fk);
CREATE INDEX idx_order_details_order    ON order_details (order_id_pfk);
CREATE INDEX idx_order_details_product  ON order_details (product_code_pfk);
CREATE INDEX idx_returns_order          ON returns       (order_id_fk);
CREATE INDEX idx_returns_product        ON returns       (product_code_fk);
CREATE INDEX idx_reviews_product        ON reviews       (product_code_fk_u);
CREATE INDEX idx_reviews_customer       ON reviews       (customer_id_fk_u);
CREATE INDEX idx_payments_order         ON payments      (order_id_fk);
CREATE INDEX idx_audits_user            ON audits        (user_id_fk);
CREATE INDEX idx_audits_product         ON audits        (product_code_fk);
CREATE INDEX idx_users_department       ON users         (department_id_fk);
CREATE INDEX idx_cart_items_cart        ON cart_items    (cart_id_fk);
CREATE INDEX idx_cart_items_product     ON cart_items    (product_code_fk);
CREATE INDEX idx_notifications_customer ON notifications (customer_id_fk);
CREATE INDEX idx_product_images_product ON product_images (product_code_fk);


ALTER TABLE users ADD COLUMN salary DECIMAL(10,2) NOT NULL DEFAULT 0.00;

ALTER TABLE users
    CHANGE COLUMN salary user_salary DECIMAL(10,2) NOT NULL DEFAULT 0.00;

SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

commit 
