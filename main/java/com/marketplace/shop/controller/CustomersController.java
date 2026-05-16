package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.CustomerDAO;
import com.marketplace.shop.model.Customer;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.PermissionHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colName, colEmail, colPhone, colAddress, colStatus;
    @FXML private TextField nameField, emailField, phoneField, cityField, streetField, buildingField, apartmentField, searchField;
    @FXML private Button banButton, unbanButton, deleteButton;

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colAddress.setCellValueFactory(cd -> {
            Customer c = cd.getValue();
            return new javafx.beans.property.SimpleStringProperty(formatAddress(c));
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statuseCodeFk"));

        boolean canWrite = PermissionHelper.canPerform(PermissionHelper.MANAGE_CUSTOMERS);
        if (banButton != null)    banButton.setDisable(!canWrite);
        if (unbanButton != null)  unbanButton.setDisable(!canWrite);
        if (deleteButton != null) deleteButton.setDisable(!canWrite);

        loadCustomers();

        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateFields(newVal);
        });
    }

    private String formatAddress(Customer c) {
        StringBuilder sb = new StringBuilder();
        if (c.getStreetName() != null && !c.getStreetName().isEmpty()) sb.append(c.getStreetName());
        if (c.getBuildingNo() != null && !c.getBuildingNo().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Bldg ").append(c.getBuildingNo());
        }
        if (c.getApartmentNo() != null && !c.getApartmentNo().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Apt ").append(c.getApartmentNo());
        }
        if (c.getCity() != null && !c.getCity().isEmpty()) {
            if (sb.length() > 0) sb.append(" — ");
            sb.append(c.getCity());
        }
        return sb.length() == 0 ? "—" : sb.toString();
    }

    private void loadCustomers() {
        try {
            customerList.setAll(customerDAO.findAll());
            customerTable.setItems(customerList);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load customers: " + e.getMessage());
        }
    }

    private void populateFields(Customer c) {
        nameField.setText(n(c.getCustomerName()));
        emailField.setText(n(c.getCustomerEmail()));
        phoneField.setText(n(c.getPhoneNo()));
        cityField.setText(n(c.getCity()));
        streetField.setText(n(c.getStreetName()));
        buildingField.setText(n(c.getBuildingNo()));
        apartmentField.setText(n(c.getApartmentNo()));
    }
    private String n(String s) { return s == null ? "" : s; }

    @FXML
    private void handleAdd() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_CUSTOMERS, "add customers")) return;
        try {
            Customer c = new Customer();
            apply(c);
            c.setStatuseCodeFk("ACTIVE");
            customerDAO.insert(c);
            AlertHelper.showInfo("Success", "Customer added.");
            loadCustomers();
            clearFields();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not add customer: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_CUSTOMERS, "update customers")) return;
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("Error", "Select a customer first."); return; }
        try {
            apply(selected);
            customerDAO.update(selected);
            AlertHelper.showInfo("Success", "Customer updated.");
            loadCustomers();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not update: " + e.getMessage());
        }
    }

    private void apply(Customer c) {
        c.setCustomerName(nameField.getText().trim());
        c.setCustomerEmail(emailField.getText().trim());
        c.setPhoneNo(phoneField.getText().trim());
        c.setCity(cityField.getText().trim());
        c.setStreetName(streetField.getText().trim());
        c.setBuildingNo(buildingField.getText().trim());
        c.setApartmentNo(apartmentField.getText().trim());
    }

    @FXML
    private void handleDelete() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_CUSTOMERS, "delete a customer account")) return;
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("Error", "Select a customer first."); return; }
        if (AlertHelper.showConfirmation("Delete", "Delete customer " + selected.getCustomerName() + "?")) {
            try {
                customerDAO.delete(selected.getCustomerId());
                loadCustomers();
                clearFields();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBan() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_CUSTOMERS, "ban a customer")) return;
        Customer sel = customerTable.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertHelper.showError("Error", "Select a customer to ban."); return; }
        if (!AlertHelper.showConfirmation("Ban customer",
                "Suspend " + sel.getCustomerName() + "? They will no longer be able to sign in.")) return;
        try {
            customerDAO.setStatus(sel.getCustomerId(), "BANNED");
            AlertHelper.showInfo("Banned", sel.getCustomerName() + " has been suspended.");
            loadCustomers();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not ban customer: " + e.getMessage());
        }
    }

    @FXML
    private void handleUnban() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_CUSTOMERS, "unban a customer")) return;
        Customer sel = customerTable.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertHelper.showError("Error", "Select a customer to unban."); return; }
        try {
            customerDAO.setStatus(sel.getCustomerId(), "ACTIVE");
            AlertHelper.showInfo("Restored", sel.getCustomerName() + " can sign in again.");
            loadCustomers();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not unban customer: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        try {
            if (keyword.isEmpty()) loadCustomers();
            else customerList.setAll(customerDAO.search(keyword));
        } catch (Exception e) {
            AlertHelper.showError("Error", "Search failed: " + e.getMessage());
        }
    }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearFields() {
        nameField.clear(); emailField.clear(); phoneField.clear();
        cityField.clear(); streetField.clear(); buildingField.clear(); apartmentField.clear();
    }
}
