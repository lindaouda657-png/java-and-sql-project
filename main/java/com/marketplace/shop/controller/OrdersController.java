package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.OrderDAO;
import com.marketplace.shop.model.Order;
import com.marketplace.shop.model.OrderStatus;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.PermissionHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colId;
    @FXML private TableColumn<Order, String> colCustomer, colEmployee, colStatus;
    @FXML private ComboBox<OrderStatus> statusCombo;
    @FXML private Button updateStatusButton;
    @FXML private Button takeOrderButton;
    @FXML private Label modeLabel;

    private final OrderDAO orderDAO = new OrderDAO();
    private final ObservableList<Order> orderList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusState"));

        loadOrders();
        loadStatuses();

        boolean canManage = PermissionHelper.canPerform(PermissionHelper.MANAGE_ORDERS);
        if (statusCombo != null)       statusCombo.setDisable(!canManage);
        if (updateStatusButton != null) updateStatusButton.setDisable(!canManage);
        if (takeOrderButton != null)    takeOrderButton.setDisable(!canManage);

        if (modeLabel != null) {
            if (PermissionHelper.isAdmin()) {
                modeLabel.setText("Manager view — order history (read-only)");
            } else if (canManage) {
                modeLabel.setText("You can update order status and take orders.");
            } else {
                modeLabel.setText("Read-only — you don't have MANAGE_ORDERS permission.");
            }
        }
    }

    private void loadOrders() {
        try {
            orderList.setAll(orderDAO.findAll());
            orderTable.setItems(orderList);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load orders: " + e.getMessage());
        }
    }

    private void loadStatuses() {
        if (statusCombo == null) return;
        try {
            statusCombo.setItems(FXCollections.observableArrayList(orderDAO.findAllStatuses()));
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load order statuses: " + e.getMessage());
        }
    }

    @FXML private void handleRefresh() { loadOrders(); }

    @FXML
    private void handleUpdateStatus() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_ORDERS, "update an order's status")) return;
        Order sel = orderTable.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertHelper.showError("Error", "Select an order first."); return; }
        OrderStatus newStatus = statusCombo.getValue();
        if (newStatus == null) { AlertHelper.showError("Error", "Pick a new status."); return; }
        try {
            orderDAO.updateStatus(sel.getOrderId(), newStatus.getOrderStatuseId());
            AlertHelper.showInfo("Updated", "Order #" + sel.getOrderId() + " is now " + newStatus.getState() + ".");
            loadOrders();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not update status: " + e.getMessage());
        }
    }

    @FXML
    private void handleTakeOrder() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_ORDERS, "take ownership of an order")) return;
        Order sel = orderTable.getSelectionModel().getSelectedItem();
        if (sel == null) { AlertHelper.showError("Error", "Select an order first."); return; }
        if (SessionManager.getCurrentUser() == null) {
            AlertHelper.showError("Error", "No employee session found.");
            return;
        }
        try {
            sel.setUserIdFk(SessionManager.getCurrentUser().getUserId());
            orderDAO.update(sel);
            AlertHelper.showInfo("Taken", "You are now the handler for order #" + sel.getOrderId() + ".");
            loadOrders();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not take order: " + e.getMessage());
        }
    }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }
}
