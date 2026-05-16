package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.ProductDAO;
import com.marketplace.shop.model.Product;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryProductsController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private FlowPane productsPane;
    @FXML private TextField searchField;

    private final ProductDAO productDAO = new ProductDAO();
    private int categoryId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String name = SessionManager.getSelectedCategoryName();
        categoryId = SessionManager.getSelectedCategoryId();
        titleLabel.setText(name == null ? "Products" : name);
        loadProducts(null);
    }

    private void loadProducts(String keyword) {
        try {
            List<Product> products;
            if (keyword != null && !keyword.isEmpty()) {
                products = productDAO.search(keyword);
            } else {
                products = (categoryId <= 0) ? productDAO.findAll() : productDAO.findByCategory(categoryId);
            }
            productsPane.getChildren().clear();
            if (products.isEmpty()) {
                Label empty = new Label("No products found.");
                empty.getStyleClass().add("muted-label");
                productsPane.getChildren().add(empty);
                return;
            }
            for (Product p : products) productsPane.getChildren().add(buildCard(p));
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load products: " + e.getMessage());
        }
    }

    @FXML private void handleSearch() {
        String kw = searchField != null ? searchField.getText().trim() : "";
        loadProducts(kw.isEmpty() ? null : kw);
    }

    private VBox buildCard(Product p) {
        VBox card = new VBox(8);
        card.getStyleClass().add("product-card");
        card.setPrefWidth(230);
        card.setPadding(new Insets(12));

        ImageView iv = new ImageView();
        iv.setFitWidth(206); iv.setFitHeight(150); iv.setPreserveRatio(true);
        try {
            if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
                iv.setImage(new Image(p.getImageUrl(), true));
            }
        } catch (Exception ignore) {}

        StackPane imageBox = new StackPane(iv);
        imageBox.getStyleClass().add("product-image-box");
        imageBox.setPrefHeight(160);

        Label name = new Label(p.getProductName());
        name.getStyleClass().add("product-name");
        name.setWrapText(true);

        Label desc = new Label(p.getCategoryName() + " • " + p.getStatusState());
        desc.getStyleClass().add("muted-label");

        Label price = new Label("EGP " + p.getUnitPrice().toPlainString());
        price.getStyleClass().add("product-price");

        Label qty = new Label(p.getQuantity() > 0 ? "In stock: " + p.getQuantity() : "Out of stock");
        qty.getStyleClass().add("muted-label");

        Button view = new Button("View details");
        view.getStyleClass().add("primary-button");
        view.setMaxWidth(Double.MAX_VALUE);
        view.setOnAction(e -> openDetails(p));

        card.getChildren().addAll(imageBox, name, desc, price, qty, view);
        return card;
    }

    private void openDetails(Product p) {
        ProductDetailController.setSelectedProduct(p);
        try { App.setRoot("ProductDetail"); } catch (Exception e) { AlertHelper.showError("Navigation", e.getMessage()); }
    }

    @FXML private void handleBack() {
        try { App.setRoot("CustomerHome"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void goToCart() {
        try { App.setRoot("Cart"); } catch (Exception e) { e.printStackTrace(); }
    }
}
