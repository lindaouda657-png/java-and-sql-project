package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.ReturnDAO;
import com.marketplace.shop.model.Return;
import com.marketplace.shop.util.AlertHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnsController implements Initializable {

    @FXML private TableView<Return> returnsTable;
    @FXML private TableColumn<Return, Integer> colId, colOrder;
    @FXML private TableColumn<Return, String> colProduct, colReason;
    @FXML private TableColumn<Return, String> colRefund, colDate;

    private final ReturnDAO returnDAO = new ReturnDAO();
    private final ObservableList<Return> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("returnId"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colOrder.setCellValueFactory(new PropertyValueFactory<>("orderIdFk"));
        colRefund.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getRefund() != null ? "EGP " + cd.getValue().getRefund().toPlainString() : "—"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colDate.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getDate() != null ? cd.getValue().getDate().toString() : "—"));
        load();
    }

    private void load() {
        try {
            data.setAll(returnDAO.findAll());
            returnsTable.setItems(data);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load returns: " + e.getMessage());
        }
    }

    @FXML private void handleRefresh() { load(); }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }
}
