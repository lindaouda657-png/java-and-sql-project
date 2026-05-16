package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.PaymentDAO;
import com.marketplace.shop.model.Payment;
import com.marketplace.shop.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentsController implements Initializable {

    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> colId, colOrderId;
    @FXML private TableColumn<Payment, BigDecimal> colTotal;
    @FXML private TableColumn<Payment, String> colMethod, colEmployee;

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private ObservableList<Payment> paymentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderIdFk"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        loadPayments();
    }

    private void loadPayments() {
        try {
            paymentList.setAll(paymentDAO.findAll());
            paymentTable.setItems(paymentList);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load payments: " + e.getMessage());
        }
    }

    @FXML private void handleRefresh() { loadPayments(); }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }
}
