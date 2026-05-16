 Marketplace Shop Management System
### Project Documentation

---

| Field | Details |
|---|---|
| **Project Name** | Marketplace Shop Management System |
| **Version** | 1.0-SNAPSHOT |
| **Date** | May 2026 |
| **Technology Stack** | Java 17 · JavaFX 17.0.2 · MySQL 8.0 · Maven 3.8+ |

---

## Table of Contents

1. [Executive Summary / Project Overview](#1-executive-summary--project-overview)
2. [Objectives and Requirements](#2-objectives-and-requirements)
3. [System Architecture](#3-system-architecture)
4. [UML Class Diagram](#4-uml-class-diagram)
5. [User Interface](#5-user-interface)
6. [Detailed Class Documentation](#6-detailed-class-documentation)
7. [Design Patterns Implemented](#7-design-patterns-implemented)
8. [Key Code Structure & Organization](#8-key-code-structure--organization)
9. [Installation and Running Instructions](#9-installation-and-running-instructions)
10. [Usage Guide](#10-usage-guide)
11. [Extensibility and Possible Improvements](#11-extensibility-and-possible-improvements)
12. [Conclusion](#12-conclusion)

---

## 1. Executive Summary / Project Overview

The **Marketplace Shop Management System** is a full-featured desktop application built with Java 17 and JavaFX, backed by a MySQL relational database. It models a real-world e-commerce marketplace with three distinct user roles — **Administrator**, **Employee**, and **Customer** — each with a tailored interface and access level.

The system covers the complete lifecycle of a marketplace operation: product catalogue management, customer self-service shopping, cart and checkout, order tracking, payment recording, return handling, employee management with fine-grained permissions, and an audit trail for product changes. The application follows the classic **MVC (Model-View-Controller)** architectural pattern, with a clear separation between domain model classes, data-access objects (DAOs), and JavaFX FXML controllers.

Key characteristics of the project include:

- **Role-Based Access Control (RBAC):** Three roles with runtime permission enforcement — Admin has full access, Employees have configurable permission sets, and Customers see only their own shopping experience.
- **Layered Architecture:** Model → DAO → Controller, with utility helpers for cross-cutting concerns such as session state, alerts, and permission checks.
- **Comprehensive Domain Model:** 22 Java model classes, each mapped one-to-one with a database table, covering every entity from `Status` and `Permission` to `Cart`, `Order`, `Return`, and `Audit`.
- **JavaFX UI with FXML:** All views are defined in FXML files and styled with a unified CSS stylesheet, providing a clean, native-feeling desktop interface.

---

## 2. Objectives and Requirements

### 2.1 Functional Requirements

| ID | Requirement |
|----|-------------|
| FR-01 | The system shall support three user roles: Administrator, Employee, and Customer. |
| FR-02 | Employees and Admins shall authenticate via email and password. Customers shall authenticate via email and phone number. |
| FR-03 | The Admin shall be able to create, update, deactivate, fire, or ban employees. |
| FR-04 | The Admin shall be able to assign fine-grained module permissions to individual employees. |
| FR-05 | Authorized users shall be able to perform full CRUD operations on Products, Categories, and Customers. |
| FR-06 | Customers shall be able to browse products by category, view product details, add items to a persistent cart, and complete a checkout with shipping and payment method selection. |
| FR-07 | The system shall record all orders, order details, and associated payments. |
| FR-08 | The system shall support product return requests, recording refund amount and reason. |
| FR-09 | Customers shall be able to post reviews with a rating for products they have purchased. |
| FR-10 | The system shall maintain an audit log of all product state changes, recording the old and new state, employee, and timestamp. |
| FR-11 | In-app notifications shall be supported for both employees and customers. |
| FR-12 | Each user role shall see only the dashboard modules for which they have permission. |

### 2.2 Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR-01 | The application shall run as a cross-platform JavaFX desktop app on Windows, macOS, and Linux. |
| NFR-02 | All database communication shall use parameterized `PreparedStatement` queries to prevent SQL injection. |
| NFR-03 | Session state shall be maintained in a stateless singleton (`SessionManager`) across scene transitions. |
| NFR-04 | The UI shall provide consistent feedback via dialog alerts for all error, warning, and confirmation scenarios. |
| NFR-05 | The codebase shall follow standard Java naming conventions and the OOP principles of encapsulation, abstraction, and polymorphism. |

---

## 3. System Architecture

The application is organized into a classic three-tier architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION TIER                        │
│   JavaFX Controllers  ←→  FXML Views  ←→  CSS Stylesheet   │
└──────────────────────────────┬──────────────────────────────┘
                               │  calls
┌──────────────────────────────▼──────────────────────────────┐
│                     BUSINESS / DATA TIER                    │
│   Model Classes (POJOs)  ←→  DAO Layer (GenericDAO<T>)      │
│   Utility Helpers: SessionManager, PermissionHelper,        │
│   AlertHelper, DatabaseConnection                           │
└──────────────────────────────┬──────────────────────────────┘
                               │  JDBC
┌──────────────────────────────▼──────────────────────────────┐
│                       DATA TIER                             │
│               MySQL 8.0 Database (InnoDB)                   │
│               22 tables, fully normalized                   │
└─────────────────────────────────────────────────────────────┘
```

### 3.1 Package Structure

```
com.marketplace.shop/
├── App.java                         ← JavaFX entry point; manages scene switching
│
├── model/                           ← Domain model (22 POJO classes)
│   ├── Status.java
│   ├── Permission.java
│   ├── Department.java
│   ├── DepartmentPermission.java
│   ├── User.java                    ← Employee entity
│   ├── UserCredential.java
│   ├── Customer.java
│   ├── Category.java
│   ├── Discount.java
│   ├── ProductStatus.java
│   ├── Product.java
│   ├── ProductImage.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── OrderStatus.java
│   ├── Order.java
│   ├── OrderDetail.java
│   ├── Payment.java
│   ├── Return.java
│   ├── Review.java
│   ├── Notification.java
│   └── Audit.java
│
├── dao/                             ← Data Access Objects (CRUD)
│   ├── GenericDAO.java              ← Generic interface: insert/update/delete/findById/findAll
│   ├── UserDAO.java
│   ├── UserCredentialDAO.java
│   ├── UserPermissionDAO.java
│   ├── CustomerDAO.java
│   ├── ProductDAO.java
│   ├── ProductImageDAO.java
│   ├── ProductStatusDAO.java
│   ├── CategoryDAO.java
│   ├── CartDAO.java
│   ├── OrderDAO.java
│   ├── PaymentDAO.java
│   ├── ReturnDAO.java
│   ├── ReviewDAO.java
│   ├── NotificationDAO.java
│   ├── AuditDAO.java
│   ├── DepartmentDAO.java
│   ├── DiscountDAO.java
│   ├── PermissionDAO.java
│   └── StatusDAO.java
│
├── controller/                      ← JavaFX MVC controllers (20 classes)
│   ├── RoleSelectController.java
│   ├── LoginController.java
│   ├── RegisterController.java
│   ├── DashboardController.java
│   ├── ProductsController.java
│   ├── ProductDetailController.java
│   ├── CategoryProductsController.java
│   ├── CategoriesController.java
│   ├── CustomersController.java
│   ├── CustomerHomeController.java
│   ├── CustomerProfileController.java
│   ├── CustomerOrdersController.java
│   ├── CartController.java
│   ├── OrdersController.java
│   ├── PaymentsController.java
│   ├── ReturnsController.java
│   ├── ReviewsController.java
│   ├── EmployeesController.java
│   ├── AdminProfileController.java
│   └── EmployeeProfileController.java
│
└── util/                            ← Cross-cutting utilities
    ├── DatabaseConnection.java      ← Singleton JDBC connection manager
    ├── SessionManager.java          ← Application-wide session state
    ├── PermissionHelper.java        ← RBAC enforcement helpers
    └── AlertHelper.java             ← Centralized JavaFX dialog factory
```

### 3.2 Technology Stack

| Component | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| UI Framework | JavaFX | 17.0.2 |
| Database | MySQL | 8.0+ |
| DB Driver | MySQL Connector/J | 8.0.33 |
| Build Tool | Apache Maven | 3.8+ |
| Module System | Java Platform Module System (JPMS) | — |

---

## 4. UML Class Diagram

![UML Class Diagram](Untitled%20Diagram.webp)

*Figure 4.1 – Full UML Class Diagram of the Marketplace Shop Management System. The diagram depicts all 22 model classes, their attributes, methods, and inter-class associations.*

### 4.1 Diagram Explanation

The diagram is organized around three major domain clusters:

#### 4.1.1 User & Permission Cluster

The `Status` class acts as a shared lookup table referenced by many entities (`User`, `Customer`, `Department`, `Permission`, `Category`). The `User` (Employee) entity associates with `Department` via a foreign key, and credentials are stored separately in `UserCredential` to enforce the single-responsibility principle. Individual permissions are stored in `Permission` and can be granted to employees via the `user_permissions` join table (managed by `UserPermissionDAO`). At the department level, `DepartmentPermission` links a `Department` to a `Permission`.

#### 4.1.2 Product & Catalogue Cluster

`Product` is the central entity. It references `Category` (many products per category), `ProductStatus`, and optionally `Discount`. Product images are stored separately in `ProductImage`, allowing multiple images per product with one designated as primary. `ProductStatus` and `Discount` are lookup/reference entities.

#### 4.1.3 Shopping & Order Cluster

A `Customer` may have one active `Cart`, which contains multiple `CartItem` records. Upon checkout, a `CartItem` set is converted into an `Order` containing multiple `OrderDetail` records. Each `Order` references a `Customer`, a `User` (the employee who processed it), and an `OrderStatus`. A `Payment` is linked to an `Order`. Product returns are captured in the `Return` entity, and customer feedback is stored in `Review`.

#### 4.1.4 Cross-Cutting Entities

`Audit` records who changed a product's state, capturing both old and new values. `Notification` supports messaging to both `User` and `Customer` recipients.

---

## 5. User Interface

> **Note:** The following section describes the application screens based on the defined FXML layouts and controller logic. Placeholder markers are provided for each screenshot location.

### 5.1 Role Selection Screen

**Screen:** `RoleSelect.fxml` | **Controller:** `RoleSelectController`

The entry point of the application. The user selects their role before proceeding to login. Three options are presented: **Admin**, **Employee**, and **Customer**. Selecting a role saves it to `SessionManager` and navigates to the Login screen.

*Figure 5.1 – Role Selection Screen. The user chooses between Admin, Employee, or Customer to initiate the appropriate login flow.*

---

### 5.2 Login Screen

**Screen:** `Login.fxml` | **Controller:** `LoginController`

Displays a role-appropriate title (e.g., "Admin Login", "Employee Login", "Customer Login"). Employees and Admins provide an email and password; Customers use their email and phone number as a password. Failed logins, suspended accounts, and privilege mismatches result in informative dialog messages.

*Figure 5.2 – Login Screen. The header dynamically reflects the selected role. Accounts with FIRED or SUSPENDED status are blocked with a descriptive message.*

---

### 5.3 Customer Registration Screen

**Screen:** `Register.fxml` | **Controller:** `RegisterController`

New customers self-register by providing their name, email, phone number, and optional address details. Upon successful registration, a `Cart` is automatically created for the new customer and the session is initialized.

*Figure 5.3 – Customer Registration Screen. Required fields (Name, Email, Phone) are validated before submission.*

---

### 5.4 Admin / Employee Dashboard

**Screen:** `Dashboard.fxml` | **Controller:** `DashboardController`

The central hub for staff users. The dashboard renders module cards (Products, Orders, Customers, Employees, Payments, Categories, Reviews, Returns) and dynamically hides those the current user is not authorized to access. The role badge ("ADMIN" or "EMPLOYEE") is shown next to the welcome message.

*Figure 5.4 – Admin Dashboard. All module cards are visible. The role badge confirms the current session type.*

*Figure 5.5 – Employee Dashboard. Cards are hidden based on the employee's assigned permissions. An employee without MANAGE_ORDERS permission will not see the Orders card.*

---

### 5.5 Products Management Screen

**Screen:** `Products.fxml` | **Controller:** `ProductsController`

A full-featured CRUD screen for the product catalogue. A `TableView` displays all products with columns for Code, Name, Price, Quantity, Category, and Status. Users can search by name or code, add new products, update existing ones, delete products, and upload a product image using a file chooser. Add/Update/Delete buttons are disabled when the user lacks `MANAGE_PRODUCTS` permission.

*Figure 5.6 – Products Management Screen. The TableView shows all products; the form panel on the right allows create/update operations. The image preview panel displays the uploaded product photo.*

---

### 5.6 Customer Home Screen

**Screen:** `CustomerHome.fxml` | **Controller:** `CustomerHomeController`

The customer's landing page after login. It displays a category grid rendered as interactive cards with contextual emoji icons (📱 for Electronics, 👕 for Clothing, etc.). Navigation buttons provide access to the cart, orders, and profile screens.

*Figure 5.7 – Customer Home Screen. Category cards are generated dynamically from the database. Clicking a card navigates to the filtered product listing for that category.*

---

### 5.7 Category Products & Product Detail

**Screens:** `CategoryProducts.fxml`, `ProductDetail.fxml` | **Controllers:** `CategoryProductsController`, `ProductDetailController`

The category products screen lists all products under the selected category. Clicking a product opens the Product Detail screen, which shows full product information, an image, price, and an "Add to Cart" button. Customers can also submit a review from this screen.

*Figure 5.8 – Category Products Screen. Products are listed in a scrollable view with name, price, and status.*

*Figure 5.9 – Product Detail Screen. Displays the full product card with image, description, price, and an Add to Cart action with quantity selector.*

---

### 5.8 Shopping Cart & Checkout

**Screen:** `Cart.fxml` | **Controller:** `CartController`

The cart screen shows all items in the current customer's cart with their unit prices, quantities, and subtotals. The customer can adjust quantities, remove items, and proceed to checkout. The checkout form captures shipping address (pre-filled from the customer's profile) and payment method selection (Cash on Delivery, Credit Card, Debit Card, Bank Transfer). On confirmation, an `Order`, `OrderDetail`, and `Payment` record are created atomically.

*Figure 5.10 – Shopping Cart Screen. Each line item shows product name, quantity, unit price, and subtotal. The total is calculated dynamically.*

---

### 5.9 Orders Management Screen

**Screen:** `Orders.fxml` | **Controller:** `OrdersController`

Lists all orders with customer name, employee name, and status. Admin/Employee users can update the order status (e.g., from "New" to "Shipped"). The screen uses a `TableView` with inline status updates via a `ComboBox`.

*Figure 5.11 – Orders Management Screen. Tabular view of all orders. The status column allows inline state transitions.*

---

### 5.10 Employees Management Screen

**Screen:** `Employees.fxml` | **Controller:** `EmployeesController`

Exclusively accessible to Admin users. Displays all employees (excluding the admin account) in a `TableView`. The Admin can add new employees, fire or ban existing ones, delete records, and manage per-employee permissions through a pop-up permissions dialog. The Manage Permissions dialog shows all available permissions as checkboxes; changes are saved transactionally.

*Figure 5.12 – Employees Management Screen. Table shows employee ID, name, email, department, and status. Action buttons for Fire, Ban, and Manage Permissions are only available to the Admin.*

---

### 5.11 Returns & Reviews Screens

**Screens:** `Returns.fxml`, `Reviews.fxml` | **Controllers:** `ReturnsController`, `ReviewsController`

The Returns screen lists all product return requests with refund amount, reason, product, and order reference. The Reviews screen shows customer-submitted ratings and review topics for products.

*Figure 5.13 – Returns Screen. Tabular view of all return records.*

*Figure 5.14 – Reviews Screen. Customer reviews with star ratings and product references.*

---

## 6. Detailed Class Documentation

### 6.1 Model Layer

#### 6.1.1 `Product`

**Package:** `com.marketplace.shop.model`

**Purpose:** Represents a single product in the marketplace catalogue. This is the central domain entity of the system.

**Key Attributes:**

| Visibility | Type | Name | Description |
|---|---|---|---|
| private | `String` | `productCode` | Primary key; unique product identifier (e.g., "P001") |
| private | `String` | `productName` | Display name of the product |
| private | `BigDecimal` | `unitPrice` | Selling price; uses `BigDecimal` for monetary precision |
| private | `int` | `quantity` | Available stock count |
| private | `int` | `categoryIdFk` | Foreign key to `categories` table |
| private | `int` | `productStatuseIdFk` | Foreign key to `products_statuse` table |
| private | `String` | `discountsCodeFk` | Optional foreign key to `discounts` table |
| private | `String` | `categoryName` | Denormalized: joined from `Category` for display |
| private | `String` | `statusState` | Denormalized: joined from `ProductStatus` for display |
| private | `String` | `imageUrl` | URL/path of the primary product image |
| private | `String` | `description` | Optional description; auto-generates from fields if null |

**Important Methods:**

```java
// Auto-generates a readable description from denormalized fields
public String getDescription() {
    if (description != null) return description;
    StringBuilder sb = new StringBuilder();
    if (productName != null) sb.append(productName);
    if (categoryName != null) sb.append(" • ").append(categoryName);
    if (statusState != null)  sb.append(" • ").append(statusState);
    return sb.toString();
}
```

**Relationships:** Many-to-one with `Category` and `ProductStatus`; one-to-many with `ProductImage`, `CartItem`, `OrderDetail`, `Review`, `Return`, and `Audit`.

---

#### 6.1.2 `User` (Employee)

**Package:** `com.marketplace.shop.model`

**Purpose:** Represents an employee of the organization. The same entity serves as both regular employee and administrator; the role distinction is managed at session/login time.

**Key Attributes:**

| Visibility | Type | Name | Description |
|---|---|---|---|
| private | `int` | `userId` | Auto-generated primary key |
| private | `String` | `userName` | Full name of the employee |
| private | `String` | `userEmail` | Unique login email; `admin@shop.com` has admin privileges |
| private | `String` | `statuseCodeFk` | `ACTIVE`, `FIRED`, or `BANNED` |
| private | `int` | `departmentIdFk` | Links employee to their department |
| private | `BigDecimal` | `userSalary` | Monthly salary (optional) |
| private | `String` | `departmentName` | Denormalized join from `Department` |

**Relationships:** Many-to-one with `Department` and `Status`; one-to-one with `UserCredential`; many-to-many with `Permission` via `user_permissions`.

---

#### 6.1.3 `Customer`

**Package:** `com.marketplace.shop.model`

**Purpose:** Represents a registered customer who can browse products and place orders. Customer authentication is deliberately lightweight: the phone number serves as the password.

**Key Attributes:**

| Visibility | Type | Name | Description |
|---|---|---|---|
| private | `int` | `customerId` | Auto-generated primary key |
| private | `String` | `customerName` | Display name |
| private | `String` | `customerEmail` | Unique login email |
| private | `String` | `phoneNo` | Used as password for customer login |
| private | `String` | `statuseCodeFk` | `ACTIVE` or `SUSPENDED` |
| private | `String` | `city` | Used for shipping address pre-fill |

**Relationships:** One-to-one with `Cart`; one-to-many with `Order`, `Review`, and `Notification`.

---

#### 6.1.4 `Order`

**Package:** `com.marketplace.shop.model`

**Purpose:** Represents a customer purchase order. An order ties a customer, a processing employee, and a status together. The line items are stored in `OrderDetail`.

**Key Attributes:**

| Visibility | Type | Name | Description |
|---|---|---|---|
| private | `int` | `orderId` | Auto-generated primary key |
| private | `int` | `customerIdFk` | The purchasing customer |
| private | `int` | `userIdFk` | The employee who processed the order |
| private | `int` | `orderStatuseIdFk` | Current order lifecycle status |
| private | `String` | `customerName` | Denormalized for display |
| private | `String` | `employeeName` | Denormalized for display |
| private | `String` | `statusState` | Denormalized status label |

**Relationships:** Many-to-one with `Customer`, `User`, and `OrderStatus`; one-to-many with `OrderDetail` and `Payment`.

---

#### 6.1.5 `CartItem`

**Package:** `com.marketplace.shop.model`

**Purpose:** Represents a single line item within a shopping cart, holding the product reference and quantity.

**Key Attributes:**

| Visibility | Type | Name | Description |
|---|---|---|---|
| private | `int` | `cartItemId` | Auto-generated primary key |
| private | `int` | `cartIdFk` | Parent cart reference |
| private | `String` | `productCodeFk` | Product being added |
| private | `int` | `quantity` | Number of units |
| private | `String` | `productName` | Denormalized for display |
| private | `BigDecimal` | `unitPrice` | Denormalized from product at time of cart load |

**Important Methods:**

```java
// Computes the line subtotal; returns ZERO if price is null
public BigDecimal getSubtotal() {
    if (unitPrice == null) return BigDecimal.ZERO;
    return unitPrice.multiply(BigDecimal.valueOf(quantity));
}
```

**Relationships:** Many-to-one with `Cart` and `Product`.

---

#### 6.1.6 `Audit`

**Package:** `com.marketplace.shop.model`

**Purpose:** An immutable record of a change to a product's state, used for traceability and compliance. Records who made the change, what changed, and when.

**Key Attributes:**

| Visibility | Type | Name | Description |
|---|---|---|---|
| private | `String` | `auditCode` | Unique audit record identifier |
| private | `String` | `actionType` | E.g., "UPDATE", "DELETE" |
| private | `String` | `oldState` | Previous value |
| private | `String` | `newState` | Updated value |
| private | `LocalDateTime` | `date` | Timestamp of the change |
| private | `int` | `userIdFk` | Employee who made the change |
| private | `String` | `productCodeFk` | Product affected |

**Relationships:** Many-to-one with `User` and `Product`.

---

#### 6.1.7 `Notification`

**Purpose:** A flexible notification entity that can be addressed to either an employee (`userIdFk`) or a customer (`customerIdFk`), one of which is expected to be null.

**Key Attributes:** `notificationId`, `userIdFk` (nullable `Integer`), `customerIdFk` (nullable `Integer`), `message`, `isRead`, `createdAt`.

---

### 6.2 DAO Layer

#### 6.2.1 `GenericDAO<T>` Interface

**Package:** `com.marketplace.shop.dao`

**Purpose:** Establishes a uniform contract for all data access objects. Every DAO in the system implements this interface, ensuring consistent API surface across all entities.

```java
public interface GenericDAO<T> {
    void insert(T entity)   throws SQLException;
    void update(T entity)   throws SQLException;
    void delete(Object id)  throws SQLException;
    T    findById(Object id) throws SQLException;
    List<T> findAll()        throws SQLException;
}
```

Every DAO class (e.g., `ProductDAO`, `UserDAO`, `OrderDAO`) implements `GenericDAO<T>` and may additionally define entity-specific query methods (e.g., `search(String keyword)`, `findByCategory(int id)`, `findByEmail(String email)`).

---

#### 6.2.2 `ProductDAO`

**Purpose:** Full CRUD and search operations for the `Product` entity. Uses a composite `SELECT_BASE` constant with JOINs to enrich results with category name, status state, and primary image URL in a single query.

**Key Method — `search`:**
```java
public List<Product> search(String keyword) throws SQLException {
    List<Product> list = new ArrayList<>();
    try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
            SELECT_BASE + "WHERE p.product_name LIKE ? OR p.product_code LIKE ?")) {
        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) list.add(mapRow(rs));
    }
    return list;
}
```

**Key Method — `findByCategory`:** Returns all products belonging to a given `categoryId`, used by the customer-facing category browser.

---

#### 6.2.3 `CartDAO`

**Purpose:** Manages the shopping cart lifecycle. Notably does not implement `GenericDAO` as it provides a more specialized API suited to cart semantics.

**Key Method — `getOrCreateCart`:** Retrieves the most recent cart for a customer, or inserts a new one if none exists. This ensures every customer always has a valid cart.

```java
public Cart getOrCreateCart(int customerId) throws SQLException {
    // Attempt to retrieve existing cart
    String sql = "SELECT * FROM cart WHERE customer_id_fk = ? ORDER BY created_at DESC LIMIT 1";
    try (PreparedStatement ps = ...) {
        ResultSet rs = ps.executeQuery();
        if (rs.next()) { /* return existing cart */ }
    }
    // Create a new cart if none found
    String ins = "INSERT INTO cart (customer_id_fk) VALUES (?)";
    // ...
}
```

**Key Method — `addItem`:** First checks whether the product already exists in the cart; if it does, it increments the quantity rather than creating a duplicate row (upsert semantics).

---

#### 6.2.4 `CustomerDAO`

**Purpose:** CRUD and search for `Customer`. The `delete` method implements a cascaded deletion — it removes dependent `cart_items` and `cart` records before deleting the customer, honoring referential integrity without relying solely on database cascades.

**Key Method — `setStatus`:** Allows targeted status updates (e.g., suspend a customer) without loading the full entity.

---

#### 6.2.5 `UserPermissionDAO`

**Purpose:** Manages the many-to-many relationship between users and permissions. The `replaceForUser` method performs a transactional delete-then-insert to atomically replace an employee's full permission set.

```java
public void replaceForUser(int userId, Set<Integer> permissionIds) throws SQLException {
    Connection con = DatabaseConnection.getConnection();
    con.setAutoCommit(false);
    try {
        // Delete all existing permissions for the user
        // Batch-insert the new permission set
        con.commit();
    } catch (SQLException e) {
        con.rollback();
        throw e;
    } finally {
        con.setAutoCommit(prevAuto);
    }
}
```

---

#### 6.2.6 `AuditDAO`

**Purpose:** Write-mostly DAO for the audit trail. The `update` method is intentionally a no-op — audit records are immutable by design.

---

### 6.3 Utility Layer

#### 6.3.1 `DatabaseConnection`

**Purpose:** Provides a single, lazily initialized JDBC `Connection` object shared across the entire application. Implements a simple singleton pattern.

```java
public static Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
    return connection;
}
```

**Configuration:** `jdbc:mysql://localhost:3306/online_shopping1` | User: `root`

---

#### 6.3.2 `SessionManager`

**Purpose:** Acts as an application-scoped state container (static singleton). Holds references to the currently authenticated `User` (employee/admin) or `Customer`, the selected role string, the active cart ID, and the set of permission codes held by the current employee.

**Key Fields:**

| Field | Type | Purpose |
|---|---|---|
| `currentUser` | `User` | Authenticated employee/admin |
| `currentCustomer` | `Customer` | Authenticated customer |
| `selectedRole` | `String` | `"ADMIN"`, `"EMPLOYEE"`, or `"CUSTOMER"` |
| `currentCartId` | `int` | Active shopping cart ID for current customer session |
| `currentPermissions` | `Set<String>` | Permission codes for the current employee |

**Key Methods:** `setCurrentUser`, `setCurrentCustomer`, `logout()` (clears all state), `isLoggedIn()`.

---

#### 6.3.3 `PermissionHelper`

**Purpose:** A utility class that centralizes all authorization logic. Controllers call methods on `PermissionHelper` rather than querying the session directly, keeping access-control code out of business logic.

**Permission Constants:**

```java
public static final String MANAGE_PRODUCTS   = "MANAGE_PRODUCTS";
public static final String MANAGE_ORDERS     = "MANAGE_ORDERS";
public static final String MANAGE_CUSTOMERS  = "MANAGE_CUSTOMERS";
public static final String MANAGE_PAYMENTS   = "MANAGE_PAYMENTS";
public static final String MANAGE_CATEGORIES = "MANAGE_CATEGORIES";
public static final String MANAGE_REVIEWS    = "MANAGE_REVIEWS";
public static final String MANAGE_EMPLOYEES  = "MANAGE_EMPLOYEES";
```

**Key Method — `canPerform`:**

```java
public static boolean canPerform(String permissionCode) {
    if (isAdmin()) return true;  // Admin always has all permissions
    return employeeHas(permissionCode);
}
```

**Key Method — `requirePermission`:** Returns `true` if the action is allowed; otherwise shows a permission-denied dialog and returns `false`. Controllers use this in a guard pattern:

```java
if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_PRODUCTS, "edit products")) {
    return; // abort the action
}
```

---

#### 6.3.4 `AlertHelper`

**Purpose:** Centralizes all JavaFX dialog creation, providing a consistent look and feel for informational, error, warning, and confirmation dialogs.

**Methods:**

| Method | Type | Description |
|---|---|---|
| `showInfo(title, message)` | `void` | Information dialog |
| `showError(title, message)` | `void` | Error dialog |
| `showPermissionDenied(reason)` | `void` | Warning dialog for access control violations |
| `showConfirmation(title, message)` | `boolean` | Returns `true` if the user confirms |

---

### 6.4 Controller Layer

#### 6.4.1 `LoginController`

**Purpose:** Handles authentication for all three roles. Applies different validation rules depending on the role stored in `SessionManager`:

- **Customer:** Looks up by email, compares `phoneNo` as password, checks for `SUSPENDED`/`INACTIVE` status.
- **Employee/Admin:** Looks up by email, retrieves `UserCredential`, checks password hash, validates account status (`FIRED`, `BANNED`, `ACTIVE`), and enforces that only `admin@shop.com` may log in as Admin.
- After successful employee login, loads permission codes via `UserPermissionDAO` into the session.

---

#### 6.4.2 `DashboardController`

**Purpose:** The central navigation hub. On initialization, calls `applyRolePermissions()` which hides module cards based on the current role and permissions. Navigation methods use `App.setRoot(String)` to transition between FXML scenes.

```java
private void applyRolePermissions(String role) {
    if ("ADMIN".equals(role)) {
        hide(cardProducts, cardCategories); // Admin manages via other means
    } else if ("EMPLOYEE".equals(role)) {
        if (!PermissionHelper.employeeHas(MANAGE_PRODUCTS))   hide(cardProducts);
        if (!PermissionHelper.employeeHas(MANAGE_ORDERS))     hide(cardOrders);
        // ... etc.
        hide(cardEmployees, cardReturns); // Always hidden for employee
    }
}
```

---

#### 6.4.3 `CartController`

**Purpose:** The most complex customer-facing controller. Handles the full shopping cart experience:

1. **Initialization:** Loads current cart items, pre-fills the shipping form from the customer's profile, and populates the payment method combo box.
2. **Cart Rendering:** Dynamically builds `HBox` rows for each `CartItem` with quantity spinners and remove buttons.
3. **Checkout:** On confirmation, creates an `Order` record, iterates over cart items to insert `OrderDetail` records, creates a `Payment` record, and clears the cart — all within a single database interaction sequence.

---

#### 6.4.4 `EmployeesController`

**Purpose:** Admin-only screen for full employee lifecycle management. Key operations include:

- **Add Employee:** Opens a custom modal `Stage` built programmatically with a `GridPane` form.
- **Fire/Ban:** Updates the employee's `statuseCodeFk` field.
- **Manage Permissions:** Opens a permissions dialog listing all available permissions as checkboxes, then calls `UserPermissionDAO.replaceForUser()` to atomically update the permission set.

---

## 7. Design Patterns Implemented

### 7.1 Data Access Object (DAO) Pattern

The DAO pattern is the backbone of the data layer. All database interactions are encapsulated within dedicated DAO classes, completely decoupling the business logic in controllers from SQL and JDBC concerns.

The `GenericDAO<T>` interface defines the contract, and each concrete DAO (e.g., `ProductDAO implements GenericDAO<Product>`) fulfills it:

```java
// Interface defines contract
public interface GenericDAO<T> {
    void insert(T entity)    throws SQLException;
    void update(T entity)    throws SQLException;
    void delete(Object id)   throws SQLException;
    T    findById(Object id) throws SQLException;
    List<T> findAll()        throws SQLException;
}

// Concrete implementation
public class ProductDAO implements GenericDAO<Product> {
    @Override
    public void insert(Product p) throws SQLException { /* JDBC logic */ }
    // ...
}
```

### 7.2 Singleton Pattern

`DatabaseConnection` implements the Singleton pattern to ensure only one JDBC `Connection` is active at any time. The connection is lazily initialized on the first call to `getConnection()`, and the same instance is reused for subsequent calls.

### 7.3 Model-View-Controller (MVC)

The entire UI architecture follows MVC:
- **Model:** POJO classes in the `model` package.
- **View:** FXML files in `src/main/resources/fxml/`, styled via `style.css`.
- **Controller:** Java controller classes annotated with `@FXML` in the `controller` package.

### 7.4 Session (Application State) Pattern

`SessionManager` implements a static application-state singleton that holds all cross-screen state. This avoids the need to pass data through constructor parameters or method signatures between scenes, acting as a lightweight application context.

### 7.5 Guard Clause / Authorization Pattern

`PermissionHelper.requirePermission()` implements a consistent guard clause pattern. Controllers call it at the top of any write operation to terminate early if authorization fails, centralizing access control and eliminating repetitive if-else permission checks in business logic.

### 7.6 Factory Method (Alert Creation)

`AlertHelper` acts as a simple factory for JavaFX `Alert` dialogs. It abstracts the construction of different alert types, providing a clean, consistent API to all callers.

---

## 8. Key Code Structure & Organization

### 8.1 Application Entry Point

The `App` class extends `javafx.application.Application` and serves as the JavaFX bootstrap. It exposes a static `setRoot(String fxml)` helper method that all controllers use to navigate between screens, loading FXML files from the classpath.

```java
// Navigation pattern used by all controllers
App.setRoot("Dashboard");    // Loads Dashboard.fxml
App.setRoot("RoleSelect");   // Returns to the role selection screen
```

### 8.2 FXML + CSS Architecture

All 20 screens are defined as FXML files in `src/main/resources/fxml/`. A single `style.css` in `src/main/resources/css/` is applied globally to the root scene. CSS class names such as `.category-card`, `.muted-label`, `.order-row` are referenced from controller code using `getStyleClass().add(...)`.

### 8.3 Database Schema Organization

The schema defines 22 InnoDB tables with full foreign key constraints. Key design decisions include:

- `SET FOREIGN_KEY_CHECKS = 0` is used during schema creation to handle circular dependency ordering.
- String-coded primary keys are used for status lookups (`statuse_code VARCHAR(20)`) to improve readability in the database.
- `ON DELETE CASCADE` is used on `user_permissions` to cleanly remove permissions when a user is deleted.
- Monetary values use `DECIMAL(10,2)` throughout.

### 8.4 Module System (JPMS)

The project uses the Java Platform Module System with a `module-info.java` that declares:

```java
module com.marketplace.shop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.marketplace.shop to javafx.fxml;
    opens com.marketplace.shop.controller to javafx.fxml;
    opens com.marketplace.shop.model to javafx.base;  // Required for PropertyValueFactory reflection
    exports com.marketplace.shop;
}
```

The `opens ... to javafx.base` directive on the model package is required for `PropertyValueFactory` in `TableView` to work via reflection.

### 8.5 Parameterized Queries (SQL Injection Prevention)

All DAO operations use `PreparedStatement` exclusively. No string concatenation is used to build SQL with user-supplied input:

```java
// All queries use parameterized inputs
try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
        "WHERE p.product_name LIKE ? OR p.product_code LIKE ?")) {
    ps.setString(1, "%" + keyword + "%");
    ps.setString(2, "%" + keyword + "%");
    // ...
}
```

---

## 9. Installation and Running Instructions

### 9.1 Prerequisites

| Requirement | Minimum Version |
|---|---|
| JDK | 17 |
| Apache Maven | 3.8+ |
| MySQL Server | 8.0+ |

### 9.2 Database Setup

**Step 1:** Start your MySQL server and open a client (MySQL CLI, MySQL Workbench, DBeaver, etc.).

**Step 2:** Run the main schema script to create all tables:

```sql
-- Run from the project root:
source sql/schema.sql
```

**Step 3:** Run migration scripts in order:

```sql
source sql/migration_v2_permissions.sql
source sql/migration_v3_employee_management.sql
source sql/migration_v4_fixes.sql
```

**Step 4:** Seed initial reference data:

```sql
INSERT INTO statuses (statuse_code, statuse_name) VALUES ('ACTIVE', 'Active');
INSERT INTO statuses (statuse_code, statuse_name) VALUES ('INACTIVE', 'Inactive');

INSERT INTO department (department_name, statuse_code_fk, city)
    VALUES ('IT', 'ACTIVE', 'Main City');

INSERT INTO users (user_name, user_email, statuse_code_fk, department_id_fk)
    VALUES ('Admin', 'admin@shop.com', 'ACTIVE', 1);

INSERT INTO user_credentials (user_id_fk, password_hash)
    VALUES (1, 'admin123');

INSERT INTO products_statuse (product_statuse_code, state) VALUES ('AVL', 'Available');
INSERT INTO order_statuse    (order_statuse_code, state)   VALUES ('NEW', 'New');
```

Alternatively, use the provided `sql/test_data.sql` script for a fuller seed dataset.

### 9.3 Database Connection Configuration

Open `src/main/java/com/marketplace/shop/util/DatabaseConnection.java` and update the constants to match your MySQL installation:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/online_shopping1";
private static final String USER     = "root";
private static final String PASSWORD = "your_password_here";
```

### 9.4 Building and Running

Clone or extract the project, then from the `cleaned_project/` directory:

```bash
# Clean build and launch the JavaFX application
mvn clean javafx:run
```

The Maven JavaFX plugin (`javafx-maven-plugin 0.0.8`) handles the module path configuration automatically.

### 9.5 Default Admin Credentials

```
Email:    admin@shop.com
Password: admin123
Role:     Admin
```

---

## 10. Usage Guide

### 10.1 Admin Workflow

1. **Launch** the application; the Role Selection screen appears.
2. **Select** *Admin* and log in with `admin@shop.com` / `admin123`.
3. The **Dashboard** opens with full module access.
4. Navigate to **Employees** to:
   - Add new employees (click *Add Employee* → fill form → confirm).
   - Manage permissions (select employee → *Manage Permissions* → check applicable boxes → Save).
   - Fire or Ban employees as needed.
5. Navigate to **Categories** to add/edit product categories.
6. Navigate to **Products** (or grant permission to an employee for this) to manage the catalogue.
7. Navigate to **Orders** / **Payments** / **Returns** for operational oversight.

### 10.2 Employee Workflow

1. Select *Employee* on the Role Selection screen.
2. Log in with assigned credentials.
3. The Dashboard shows only the modules for which the employee has permissions.
4. Perform permitted operations (e.g., update product stock, view orders, manage customers).

### 10.3 Customer Workflow

1. Select *Customer* on the Role Selection screen.
2. **New customer:** Click *Register* → fill in Name, Email, Phone, and optional address → submit. The system creates the account and a cart automatically.
3. **Returning customer:** Log in with email and phone number.
4. Browse categories on the **Customer Home** screen.
5. Click a category card → browse products → click a product → view detail → click *Add to Cart*.
6. Navigate to **Cart** → adjust quantities if needed → fill/confirm shipping address → select payment method → click *Checkout*.
7. View placed orders via the **My Orders** link.
8. Update profile information via **My Profile**.

---

## 11. Extensibility and Possible Improvements

### 11.1 Security Enhancements

The most critical improvement area. Current implementation stores passwords in plain text (employees) and uses phone numbers as customer passwords. Recommended changes:

- Integrate a proper hashing library (e.g., `BCrypt` via `spring-security-crypto`) to hash and verify passwords.
- Add a salt column to `user_credentials`.
- Implement session expiry (time-based token invalidation in `SessionManager`).

### 11.2 Connection Pooling

The current `DatabaseConnection` singleton manages a single `Connection` object. Under concurrent load (if the application were adapted to a multi-user server scenario), this would be a bottleneck. Replace with **HikariCP** or **Apache DBCP2** for production-grade connection pooling.

### 11.3 Transaction Management

Checkout in `CartController` performs multiple sequential inserts without wrapping them in a single JDBC transaction. If any insert fails partway through, the database is left in an inconsistent state. This should be wrapped in `connection.setAutoCommit(false)` / `commit()` / `rollback()` blocks, similar to the approach already used in `UserPermissionDAO.replaceForUser()`.

### 11.4 Input Validation

Form inputs (product price, quantity, email format) are only loosely validated. A dedicated `ValidationUtils` class using regular expressions or a validation library would improve data integrity.

### 11.5 Report Generation

Add a reporting module that produces PDF or CSV exports for orders, payments, and inventory using libraries such as **iText** or **Apache POI**.

### 11.6 REST API Exposure

Extracting the DAO and service layer into a separate module would enable exposing the same business logic as a REST API (e.g., using **Spring Boot**), allowing a web front-end or mobile application to use the same backend.

### 11.7 Image Storage

Product images are currently stored as file paths on the local filesystem. A more robust approach would store images in a dedicated object storage service (e.g., AWS S3, MinIO) or encode small images as BLOBs in the database.

### 11.8 Notification Delivery

The `Notification` model and `NotificationDAO` are implemented but not wired to any in-app notification UI. Adding a polling mechanism or JavaFX task to display real-time notifications would complete this feature.

### 11.9 Discount Engine

The `Discount` model and `DiscountDAO` are in place but the discount calculation is not yet reflected in the cart total or order detail prices. Implementing a discount application service would enable promotional pricing.

---

## 12. Conclusion

The **Marketplace Shop Management System** is a well-structured, full-featured desktop application that demonstrates solid application of Object-Oriented Programming principles in a real-world context. The project successfully implements:

- **Encapsulation** throughout the model layer via private fields and public accessors.
- **Abstraction** via the `GenericDAO<T>` interface, which defines a clean data-access contract independent of any specific entity.
- **Polymorphism** at the DAO layer, where all 20 DAOs share the same interface but provide entity-specific implementations.
- **Separation of Concerns** across model, DAO, controller, and utility packages.
- **Role-Based Access Control** enforced consistently through `PermissionHelper` and `SessionManager`, providing a production-realistic authorization model.
- **MVC Architecture** via JavaFX FXML, with clear boundaries between view definitions and controller logic.

The layered architecture makes each component independently testable and maintainable. The 22 model classes provide a clean, normalized domain representation, and the corresponding DAO classes ensure all persistence logic remains in one place. The utility layer provides reusable, centralized solutions to cross-cutting concerns such as session management, alerting, and permission checking.

With the security, transactional, and feature improvements identified in Section 11, the system would be well-positioned for deployment in a real small-to-medium business environment.

---

*End of Documentation — Marketplace Shop Management System v1.0*
