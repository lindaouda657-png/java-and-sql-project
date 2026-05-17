package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.ReviewDAO;
import com.marketplace.shop.model.Review;
import com.marketplace.shop.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ReviewsController implements Initializable {

    @FXML private TableView<Review> reviewsTable;
    @FXML private TableColumn<Review, String> colProduct, colCustomer, colTopic;
    @FXML private TableColumn<Review, Integer> colRate;

    private final ReviewDAO dao = new ReviewDAO();
    private final ObservableList<Review> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        colTopic.setCellValueFactory(new PropertyValueFactory<>("theTopic"));
        try {
            data.setAll(dao.findAll());
            reviewsTable.setItems(data);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load reviews: " + e.getMessage());
        }
    }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }
}
