package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.PermissionHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label roleBadge;
    @FXML private Button cardProducts;
    @FXML private Button cardOrders;
    @FXML private Button cardCustomers;
    @FXML private Button cardEmployees;
    @FXML private Button cardPayments;
    @FXML private Button cardCategories;
    @FXML private Button cardReviews;
    @FXML private Button cardReturns;
    @FXML private Button cardProfile;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String role = SessionManager.getSelectedRole();
        String name;
        if ("CUSTOMER".equals(role) && SessionManager.getCurrentCustomer() != null) {
            name = SessionManager.getCurrentCustomer().getCustomerName();
        } else if (SessionManager.getCurrentUser() != null) {
            name = SessionManager.getCurrentUser().getUserName();
        } else {
            name = "Guest";
        }
        welcomeLabel.setText("Welcome, " + name);
        if (roleBadge != null) roleBadge.setText(role == null ? "" : role);
        applyRolePermissions(role);
    }

    private void applyRolePermissions(String role) {
        if ("ADMIN".equals(role)) {
            
            hide(cardProducts, cardCategories);
        } else if ("EMPLOYEE".equals(role)) {
            if (!PermissionHelper.employeeHas(PermissionHelper.MANAGE_PRODUCTS))   hide(cardProducts);
            if (!PermissionHelper.employeeHas(PermissionHelper.MANAGE_ORDERS))     hide(cardOrders);
            if (!PermissionHelper.employeeHas(PermissionHelper.MANAGE_CUSTOMERS))  hide(cardCustomers);
            if (!PermissionHelper.employeeHas(PermissionHelper.MANAGE_PAYMENTS))   hide(cardPayments);
            if (!PermissionHelper.employeeHas(PermissionHelper.MANAGE_CATEGORIES)) hide(cardCategories);
            if (!PermissionHelper.employeeHas(PermissionHelper.MANAGE_REVIEWS))    hide(cardReviews);
            hide(cardEmployees, cardReturns);
        } else {
            
            hide(cardEmployees, cardProfile, cardReturns);
        }
    }

    private void hide(Node... nodes) {
        for (Node n : nodes) {
            if (n != null) { n.setVisible(false); n.setManaged(false); }
        }
    }

    @FXML private void goToProducts()   { navigateTo("Products"); }
    @FXML private void goToOrders()     { navigateTo("Orders"); }
    @FXML private void goToCustomers()  { navigateTo("Customers"); }
    @FXML private void goToEmployees()  { navigateTo("Employees"); }
    @FXML private void goToPayments()   { navigateTo("Payments"); }
    @FXML private void goToCategories() { navigateTo("Categories"); }
    @FXML private void goToReviews()    { navigateTo("Reviews"); }
    @FXML private void goToReturns()    { navigateTo("Returns"); }

    @FXML
    private void goToProfile() {
        String role = SessionManager.getSelectedRole();
        if ("ADMIN".equals(role)) {
            navigateTo("AdminProfile");
        } else if ("EMPLOYEE".equals(role)) {
            navigateTo("EmployeeProfile");
        }
    }

    @FXML
    private void handleLogout() {
        if (AlertHelper.showConfirmation("Logout", "Are you sure you want to logout?")) {
            SessionManager.logout();
            try { App.setRoot("RoleSelect"); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void navigateTo(String page) {
        try { App.setRoot(page); } catch (Exception e) {
            AlertHelper.showError("Navigation Error", "Could not open " + page + ": " + e.getMessage());
        }
    }
}
