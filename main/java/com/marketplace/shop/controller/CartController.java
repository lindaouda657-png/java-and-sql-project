package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.CartDAO;
import com.marketplace.shop.dao.OrderDAO;
import com.marketplace.shop.dao.PaymentDAO;
import com.marketplace.shop.model.Cart;
import com.marketplace.shop.model.CartItem;
import com.marketplace.shop.model.Customer;
import com.marketplace.shop.model.Order;
import com.marketplace.shop.model.OrderDetail;
import com.marketplace.shop.model.Payment;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.DatabaseConnection;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    @FXML private VBox cartItemsList;
    @FXML private Label totalLabel;
    @FXML private TextField shipCity;
    @FXML private TextField shipStreet;
    @FXML private TextField shipBuilding;
    @FXML private TextField shipApartment;
    @FXML private ComboBox<String> paymentCombo;
    @FXML private Button checkoutButton;

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    private List<CartItem> items;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (SessionManager.getCurrentCustomer() == null) { goHome(); return; }

        paymentCombo.getItems().setAll("Cash on Delivery", "Credit Card", "Debit Card", "Bank Transfer");
        paymentCombo.getSelectionModel().selectFirst();

        Customer c = SessionManager.getCurrentCustomer();
        shipCity.setText(c.getCity() == null ? "" : c.getCity());
        shipStreet.setText(c.getStreetName() == null ? "" : c.getStreetName());
        shipBuilding.setText(c.getBuildingNo() == null ? "" : c.getBuildingNo());
        shipApartment.setText(c.getApartmentNo() == null ? "" : c.getApartmentNo());

        loadCart();
    }

    private void loadCart() {
        cartItemsList.getChildren().clear();
        try {
            int cartId = SessionManager.getCurrentCartId();
            if (cartId == 0) {
                Cart cart = cartDAO.getOrCreateCart(SessionManager.getCurrentCustomer().getCustomerId());
                SessionManager.setCurrentCartId(cart.getCartId());
                cartId = cart.getCartId();
            }
            items = cartDAO.getCartItems(cartId);
            if (items.isEmpty()) {
                Label empty = new Label("Your cart is empty. Go browse some products!");
                empty.getStyleClass().add("muted-label");
                cartItemsList.getChildren().add(empty);
                totalLabel.setText("Total: EGP 0.00");
                checkoutButton.setDisable(true);
                return;
            }
            checkoutButton.setDisable(false);
            BigDecimal total = BigDecimal.ZERO;
            for (CartItem item : items) {
                cartItemsList.getChildren().add(buildItemRow(item));
                total = total.add(item.getSubtotal());
            }
            totalLabel.setText("Total: EGP " + total.toPlainString());
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load cart: " + e.getMessage());
        }
    }

    private HBox buildItemRow(CartItem item) {
        HBox row = new HBox(12);
        row.getStyleClass().add("order-row");
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox info = new VBox(3);
        Label name = new Label(item.getProductName());
        name.getStyleClass().add("product-name");
        Label price = new Label("EGP " + (item.getUnitPrice() != null ? item.getUnitPrice().toPlainString() : "0") + " each");
        price.getStyleClass().add("muted-label");
        info.getChildren().addAll(name, price);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Spinner<Integer> spinner = new Spinner<>(1, 99, item.getQuantity());
        spinner.setPrefWidth(80);
        spinner.setEditable(false);
        spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                cartDAO.updateItemQuantity(item.getCartItemId(), newVal);
                loadCart();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not update quantity.");
            }
        });

        Label subtotal = new Label("EGP " + item.getSubtotal().toPlainString());
        subtotal.getStyleClass().add("product-price");
        subtotal.setPrefWidth(110);

        Button removeBtn = new Button("✕");
        removeBtn.getStyleClass().add("danger-button");
        removeBtn.setStyle("-fx-padding: 5 10;");
        removeBtn.setOnAction(e -> {
            try {
                cartDAO.removeItem(item.getCartItemId());
                loadCart();
            } catch (Exception ex) {
                AlertHelper.showError("Error", "Could not remove item.");
            }
        });

        row.getChildren().addAll(info, spacer, spinner, subtotal, removeBtn);
        return row;
    }

    @FXML
    private void handleCheckout() {
        if (items == null || items.isEmpty()) {
            AlertHelper.showError("Empty Cart", "Add some products before checking out.");
            return;
        }
        String city = shipCity.getText().trim();
        String street = shipStreet.getText().trim();
        if (city.isEmpty() || street.isEmpty()) {
            AlertHelper.showError("Shipping Address", "Please fill in at least City and Street for the shipping address.");
            return;
        }
        String payMethod = paymentCombo.getValue();
        if (payMethod == null) {
            AlertHelper.showError("Payment", "Please select a payment method.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirmation("Confirm Order",
            "Place order with " + items.size() + " item(s)?\n" +
            "Ship to: " + city + ", " + street + "\n" +
            "Payment: " + payMethod);
        if (!confirmed) return;

        try {

            int handlerUserId = getDefaultEmployeeId();

            int newStatusId = getOrderStatusId("NEW");

            Order order = new Order();
            order.setCustomerIdFk(SessionManager.getCurrentCustomer().getCustomerId());
            order.setUserIdFk(handlerUserId);
            order.setOrderStatuseIdFk(newStatusId);
            orderDAO.insert(order);

            int orderId = order.getOrderId();

            BigDecimal grandTotal = BigDecimal.ZERO;
            for (CartItem item : items) {
                BigDecimal lineTotal = item.getSubtotal();
                grandTotal = grandTotal.add(lineTotal);
                String detailCode = "OD" + orderId + "_" + item.getProductCodeFk();
                String sqlDetail = "INSERT INTO order_details (order_details_code, order_id_pfk, product_code_pfk, total_amount_of_product, date) VALUES (?,?,?,?,?)";
                try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlDetail)) {
                    ps.setString(1, detailCode);
                    ps.setInt(2, orderId);
                    ps.setString(3, item.getProductCodeFk());
                    ps.setBigDecimal(4, lineTotal);
                    ps.setDate(5, Date.valueOf(LocalDate.now()));
                    ps.executeUpdate();
                }
            }

            Payment payment = new Payment();
            payment.setOrderIdFk(orderId);
            payment.setTotal(grandTotal);
            payment.setPaymentMethod(payMethod);
            payment.setCustomerId(SessionManager.getCurrentCustomer().getCustomerId());
            paymentDAO.insert(payment);

            cartDAO.clearCart(SessionManager.getCurrentCartId());

            AlertHelper.showInfo("Order Placed!",
                "Your order #" + orderId + " has been placed successfully!\n" +
                "Total: EGP " + grandTotal.toPlainString() + "\n" +
                "An employee will process your order shortly.");

            App.setRoot("CustomerOrders");

        } catch (Exception e) {
            AlertHelper.showError("Checkout Error", "Could not place order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int getDefaultEmployeeId() {
        try {
            String sql = "SELECT user_id FROM users LIMIT 1";
            try (java.sql.Statement st = DatabaseConnection.getConnection().createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) return rs.getInt("user_id");
            }
        } catch (Exception e) {  }
        return 1;
    }

    private int getOrderStatusId(String code) {
        try {
            String sql = "SELECT order_statuse_id FROM order_statuse WHERE order_statuse_code=?";
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
                ps.setString(1, code);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getInt("order_statuse_id");
            }
        } catch (Exception e) {  }
        return 1;
    }

    @FXML
    private void handleBack() { goHome(); }

    private void goHome() {
        try { App.setRoot("CustomerHome"); } catch (Exception e) { e.printStackTrace(); }
    }
}
