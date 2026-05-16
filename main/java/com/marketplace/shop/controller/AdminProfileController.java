package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.UserDAO;
import com.marketplace.shop.dao.UserCredentialDAO;
import com.marketplace.shop.model.User;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminProfileController implements Initializable {

    @FXML private TextField nameField, emailField, roleField,
                            cityField, streetField, buildingField, apartmentField;
    @FXML private PasswordField currentPassField, newPassField, confirmPassField;

    private final UserDAO userDAO = new UserDAO();
    private final UserCredentialDAO credentialDAO = new UserCredentialDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User u = SessionManager.getCurrentUser();
        if (u == null) { handleBack(); return; }
        nameField.setText(s(u.getUserName()));
        emailField.setText(s(u.getUserEmail()));
        roleField.setText("Administrator");
        cityField.setText(s(u.getCity()));
        streetField.setText(s(u.getStreetName()));
        buildingField.setText(s(u.getBuildingNo()));
        apartmentField.setText(s(u.getApartmentNo()));
    }

    private String s(String x) { return x == null ? "" : x; }

    @FXML
    private void handleSave() {
        User u = SessionManager.getCurrentUser();
        if (u == null) return;
        try {
            u.setUserName(nameField.getText().trim());
            u.setCity(cityField.getText().trim());
            u.setStreetName(streetField.getText().trim());
            u.setBuildingNo(buildingField.getText().trim());
            u.setApartmentNo(apartmentField.getText().trim());
            userDAO.update(u);
            SessionManager.setCurrentUser(u);
            AlertHelper.showInfo("Saved", "Admin profile updated.");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not save profile: " + e.getMessage());
        }
    }

    @FXML
    private void handleChangePassword() {
        User u = SessionManager.getCurrentUser();
        if (u == null) return;
        String current = currentPassField.getText();
        String newPass = newPassField.getText();
        String confirm = confirmPassField.getText();
        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            AlertHelper.showError("Validation", "Please fill in all password fields.");
            return;
        }
        if (!newPass.equals(confirm)) {
            AlertHelper.showError("Mismatch", "New password and confirmation do not match.");
            return;
        }
        if (newPass.length() < 4) {
            AlertHelper.showError("Too Short", "New password must be at least 4 characters.");
            return;
        }
        try {
            var cred = credentialDAO.findByUserId(u.getUserId());
            if (cred == null || !cred.getPasswordHash().equals(current)) {
                AlertHelper.showError("Wrong Password", "Current password is incorrect.");
                return;
            }
            credentialDAO.updatePassword(u.getUserId(), newPass);
            currentPassField.clear(); newPassField.clear(); confirmPassField.clear();
            AlertHelper.showInfo("Done", "Password changed successfully.");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not change password: " + e.getMessage());
        }
    }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }
}
