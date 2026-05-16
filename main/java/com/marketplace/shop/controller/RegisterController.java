package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.CartDAO;
import com.marketplace.shop.dao.CustomerDAO;
import com.marketplace.shop.model.Cart;
import com.marketplace.shop.model.Customer;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField streetField;
    @FXML private TextField buildingField;
    @FXML private TextField apartmentField;

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final CartDAO cartDAO = new CartDAO();

    @FXML
    private void handleRegister() {
        String name     = nameField.getText().trim();
        String email    = emailField.getText().trim();
        String phone    = phoneField.getText().trim();
        String city     = cityField.getText().trim();
        String street   = streetField.getText().trim();
        String building = buildingField.getText().trim();
        String apartment = apartmentField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            AlertHelper.showError("Validation", "Name, Email, and Phone are required.");
            return;
        }

        try {
            
            if (customerDAO.findByEmail(email) != null) {
                AlertHelper.showError("Registration Failed", "An account with this email already exists.");
                return;
            }

            Customer c = new Customer();
            c.setCustomerName(name);
            c.setCustomerEmail(email);
            c.setPhoneNo(phone);       
            c.setStatuseCodeFk("ACTIVE");
            c.setCity(city.isEmpty() ? null : city);
            c.setStreetName(street.isEmpty() ? null : street);
            c.setBuildingNo(building.isEmpty() ? null : building);
            c.setApartmentNo(apartment.isEmpty() ? null : apartment);

            customerDAO.insert(c);

            Cart cart = cartDAO.getOrCreateCart(c.getCustomerId());

            SessionManager.setCurrentCustomer(c);
            SessionManager.setCurrentCartId(cart.getCartId());
            SessionManager.setSelectedRole("CUSTOMER");

            AlertHelper.showInfo("Welcome!", "Account created successfully. Welcome, " + name + "!");
            App.setRoot("CustomerHome");

        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not create account: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            App.setRoot("RoleSelect");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
