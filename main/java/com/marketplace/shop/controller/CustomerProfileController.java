package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.CustomerDAO;
import com.marketplace.shop.model.Customer;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerProfileController implements Initializable {

    @FXML private TextField nameField, emailField, phoneField, cityField, streetField, buildingField, apartmentField;
    private final CustomerDAO dao = new CustomerDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Customer c = SessionManager.getCurrentCustomer();
        if (c == null) { handleBack(); return; }
        nameField.setText(s(c.getCustomerName()));
        emailField.setText(s(c.getCustomerEmail()));
        phoneField.setText(s(c.getPhoneNo()));
        cityField.setText(s(c.getCity()));
        streetField.setText(s(c.getStreetName()));
        buildingField.setText(s(c.getBuildingNo()));
        apartmentField.setText(s(c.getApartmentNo()));
    }
    private String s(String x) { return x == null ? "" : x; }

    @FXML
    private void handleSave() {
        Customer c = SessionManager.getCurrentCustomer();
        if (c == null) return;
        try {
            c.setCustomerName(nameField.getText().trim());
            c.setCustomerEmail(emailField.getText().trim());
            c.setPhoneNo(phoneField.getText().trim());
            c.setCity(cityField.getText().trim());
            c.setStreetName(streetField.getText().trim());
            c.setBuildingNo(buildingField.getText().trim());
            c.setApartmentNo(apartmentField.getText().trim());
            dao.update(c);
            AlertHelper.showInfo("Saved", "Your profile has been updated.");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not save profile: " + e.getMessage());
        }
    }

    @FXML private void handleBack() {
        try { App.setRoot("CustomerHome"); } catch (Exception e) { e.printStackTrace(); }
    }
}
