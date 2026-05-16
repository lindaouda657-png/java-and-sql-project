package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.OrderDAO;
import com.marketplace.shop.model.Order;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerOrdersController implements Initializable {

    @FXML private VBox ordersList;

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (SessionManager.getCurrentCustomer() == null) { handleBack(); return; }
        try {
            List<Order> orders = orderDAO.findByCustomer(SessionManager.getCurrentCustomer().getCustomerId());
            ordersList.getChildren().clear();
            if (orders.isEmpty()) {
                Label empty = new Label("You haven't placed any orders yet.");
                empty.getStyleClass().add("muted-label");
                ordersList.getChildren().add(empty);
                return;
            }
            for (Order o : orders) ordersList.getChildren().add(card(o));
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load your orders: " + e.getMessage());
        }
    }

    private HBox card(Order o) {
        HBox row = new HBox(16);
        row.getStyleClass().add("order-row");
        row.setPadding(new Insets(14));

        VBox left = new VBox(4);
        Label id = new Label("Order #" + o.getOrderId());
        id.getStyleClass().add("product-name");
        Label handler = new Label("Handled by: " + (o.getEmployeeName() == null ? "—" : o.getEmployeeName()));
        handler.getStyleClass().add("muted-label");
        left.getChildren().addAll(id, handler);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label status = new Label(o.getStatusState());
        status.getStyleClass().addAll("status-pill", pillClass(o.getStatusState()));

        row.getChildren().addAll(left, spacer, status);
        return row;
    }

    private String pillClass(String state) {
        if (state == null) return "pill-default";
        switch (state.toLowerCase()) {
            case "delivered": return "pill-success";
            case "shipped":   return "pill-info";
            case "pending":   return "pill-warn";
            case "cancelled": return "pill-danger";
            case "confirmed": return "pill-info";
            default:          return "pill-default";
        }
    }

    @FXML private void handleBack() {
        try { App.setRoot("CustomerHome"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void goToCart() {
        try { App.setRoot("Cart"); } catch (Exception e) { e.printStackTrace(); }
    }
}
