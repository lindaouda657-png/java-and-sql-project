-- This query combines the products table with categories using an INNER JOIN to show each product's category name.
SELECT p.product_name, c.category_name 
FROM products p 
INNER JOIN categories c ON p.category_id_fk = c.category_id;

-- This query uses a LEFT JOIN to show all products and their images, including products that don't have an image yet.
SELECT p.product_name, pi.image_url 
FROM products p 
LEFT JOIN product_images pi ON p.product_code = pi.product_code_fk;

-- This query joins  orders, customers, and statuses to show a list of who ordered what and the status of that order.
SELECT o.order_id, c.customer_name, os.state 
FROM orders o 
JOIN customers c ON o.customer_id_fk = c.customer_id 
JOIN order_statuse os ON o.order_statuse_id_fk = os.order_statuse_id;

-- This query counts how many total orders have been placed in the system.
SELECT COUNT(order_id) AS total_orders_count FROM orders;

-- This query calculates the total sum of money collected from all payments.
SELECT SUM(total) AS total_revenue FROM payments;

-- This query finds the highest (MAX) and lowest (MIN) product prices in the inventory.
SELECT MAX(unit_price) AS most_expensive, MIN(unit_price) AS cheapest FROM products;

-- This query groups products by category and only showing categories that have more than 2 products.
SELECT category_id_fk, COUNT(*) AS product_count 
FROM products 
GROUP BY category_id_fk 
HAVING product_count > 2;

-- This query groups payments by customer and showing those who have spent more than 10,000 in total.
SELECT customer_id, SUM(total) AS total_spent 
FROM payments 
GROUP BY customer_id 
HAVING total_spent > 10000;

-- This query groups reviews by product and showing products that have an average rating higher than 4.
SELECT product_code_fk_u, AVG(rate) AS avg_rating 
FROM reviews 
GROUP BY product_code_fk_u 
HAVING avg_rating > 4;

-- This query sorts  all products by price from the highest to the lowest.
SELECT product_name, unit_price FROM products ORDER BY unit_price DESC;

-- This query lists customers alphabetically by their names.
SELECT customer_name FROM customers ORDER BY customer_name ASC;

-- This query showes the most recent audits by sorting them by date in descending order.
SELECT * FROM audits ORDER BY date DESC;

-- This query does finds all customers who have placed at least one order by using the IN clause.
SELECT customer_name 
FROM customers 
WHERE customer_id IN (SELECT customer_id_fk FROM orders);

-- This query does finds products that have a price higher than the average price of all products.
SELECT product_name, unit_price 
FROM products 
WHERE unit_price > (SELECT AVG(unit_price) FROM products);

-- This query does selects users who belong to the 'Management' department using a subquery.
SELECT user_name 
FROM users 
WHERE department_id_fk = (SELECT department_id FROM department WHERE department_name = 'Management');

-- This query generates a monthly sales report showing total revenue and number of orders for the month.
SELECT MONTH(date) AS order_month, COUNT(order_id_pfk) AS total_orders, SUM(total_amount_of_product) AS monthly_revenue 
FROM order_details 
GROUP BY order_month;

-- This query finds the top 3 best-selling products based on the total quantity sold.
SELECT p.product_name, SUM(ci.quantity) AS total_sold 
FROM products p 
JOIN cart_items ci ON p.product_code = ci.product_code_fk 
GROUP BY p.product_name 
ORDER BY total_sold DESC 
LIMIT 3;

-- This query creates a customer activity report showing how many reviews each customer has written.
SELECT c.customer_name, COUNT(r.review_code) AS total_reviews 
FROM customers c 
LEFT JOIN reviews r ON c.customer_id = r.customer_id_fk_u 
GROUP BY c.customer_name;