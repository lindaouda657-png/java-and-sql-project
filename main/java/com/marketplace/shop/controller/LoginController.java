package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.CartDAO;
import com.marketplace.shop.dao.CustomerDAO;
import com.marketplace.shop.dao.UserDAO;
import com.marketplace.shop.dao.UserCredentialDAO;
import com.marketplace.shop.dao.UserPermissionDAO;
import com.marketplace.shop.model.Cart;
import com.marketplace.shop.model.Customer;
import com.marketplace.shop.model.User;
import com.marketplace.shop.model.UserCredential;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label roleLabel;

    private final UserDAO userDAO = new UserDAO();
    private final UserCredentialDAO credentialDAO = new UserCredentialDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final UserPermissionDAO userPermissionDAO = new UserPermissionDAO();

    @FXML
    public void initialize() {
        String role = SessionManager.getSelectedRole();
        if (role != null && roleLabel != null) {
            switch (role) {
                case "ADMIN"    -> roleLabel.setText("Admin Login");
                case "EMPLOYEE" -> roleLabel.setText("Employee Login");
                case "CUSTOMER" -> roleLabel.setText("Customer Login");
            }
        }
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String role = SessionManager.getSelectedRole();

        if (email.isEmpty() || password.isEmpty()) {
            AlertHelper.showError("Login Error", "Please enter both email and password.");
            return;
        }

        try {
            if ("CUSTOMER".equals(role)) {
                Customer customer = customerDAO.findByEmail(email);
                if (customer == null || customer.getPhoneNo() == null
                        || !password.equals(customer.getPhoneNo())) {
                    AlertHelper.showError("Login Error", "Invalid email or password.");
                    return;
                }
                if ("SUSPENDED".equalsIgnoreCase(customer.getStatuseCodeFk())
                        || "INACTIVE".equalsIgnoreCase(customer.getStatuseCodeFk())) {
                    AlertHelper.showPermissionDenied(
                        "Your account has been suspended by an Administrator. " +
                        "Please contact support to restore access.");
                    return;
                }
                SessionManager.setCurrentCustomer(customer);

                try {
                    Cart cart = cartDAO.getOrCreateCart(customer.getCustomerId());
                    SessionManager.setCurrentCartId(cart.getCartId());
                } catch (Exception cartEx) {  }
                App.setRoot("CustomerHome");
            } else {
                User user = userDAO.findByEmail(email);
                if (user == null) {
                    AlertHelper.showError("Login Error", "Invalid email or password.");
                    return;
                }
                UserCredential credential = credentialDAO.findByUserId(user.getUserId());
                if (credential == null || !credential.getPasswordHash().equals(password)) {
                    AlertHelper.showError("Login Error", "Invalid email or password.");
                    return;
                }
                String userStatus = user.getStatuseCodeFk() == null ? "" : user.getStatuseCodeFk().toUpperCase();
                if ("FIRED".equals(userStatus)) {
                    AlertHelper.showPermissionDenied(
                        "Your account has been deactivated (fired). " +
                        "You cannot access the system. Contact the Administrator for reinstatement.");
                    return;
                }
                if ("BANNED".equals(userStatus)) {
                    AlertHelper.showPermissionDenied(
                        "Your account has been banned. " +
                        "Access is permanently denied. Contact the Administrator.");
                    return;
                }
                if (!"ACTIVE".equalsIgnoreCase(userStatus)) {
                    AlertHelper.showPermissionDenied(
                        "Your employee account is not active. Please contact your Administrator.");
                    return;
                }

                if ("ADMIN".equals(role) && !"admin@shop.com".equalsIgnoreCase(user.getUserEmail())) {
                    AlertHelper.showPermissionDenied(
                        "This account does not have Administrator privileges. " +
                        "Please choose 'Continue as Employee' instead.");
                    return;
                }
                SessionManager.setCurrentUser(user);

                if ("EMPLOYEE".equals(role)) {
                    try {
                        SessionManager.setCurrentPermissions(
                            userPermissionDAO.findCodesForUser(user.getUserId()));
                    } catch (Exception permEx) {
                        SessionManager.setCurrentPermissions(java.util.Collections.emptySet());
                    }
                } else {
                    SessionManager.setCurrentPermissions(java.util.Collections.emptySet());
                }
                App.setRoot("Dashboard");
            }
        } catch (Exception e) {
            AlertHelper.showError("Connection Error", "Could not connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            SessionManager.setSelectedRole(null);
            App.setRoot("RoleSelect");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
