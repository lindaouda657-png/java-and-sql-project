package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;

public class RoleSelectController {

    @FXML
    private void selectEmployee() {
        try {
            SessionManager.setSelectedRole("EMPLOYEE");
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void selectAdmin() {
        try {
            SessionManager.setSelectedRole("ADMIN");
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void selectCustomer() {
        try {
            SessionManager.setSelectedRole("CUSTOMER");
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createAccount() {
        try {
            App.setRoot("Register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
