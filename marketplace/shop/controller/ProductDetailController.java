package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.CartDAO;
import com.marketplace.shop.dao.ReviewDAO;
import com.marketplace.shop.model.Product;
import com.marketplace.shop.model.Review;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductDetailController implements Initializable {

    private static Product selected;
    public static void setSelectedProduct(Product p) { selected = p; }

    @FXML private ImageView image;
    @FXML private Label nameLabel, priceLabel, qtyLabel, categoryLabel, statusLabel, descriptionLabel;
    @FXML private VBox reviewsBox;
    @FXML private TextArea reviewText;
    @FXML private ComboBox<Integer> ratingCombo;
    @FXML private Button submitReviewButton;
    @FXML private ComboBox<Integer> qtyCombo;
    @FXML private Button addToCartButton;
    @FXML private HBox cartRow;

    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final CartDAO cartDAO = new CartDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (selected == null) { handleBack(); return; }
        nameLabel.setText(selected.getProductName());
        priceLabel.setText("EGP " + selected.getUnitPrice().toPlainString());
        qtyLabel.setText("Available: " + selected.getQuantity());
        categoryLabel.setText("Category: " + selected.getCategoryName());
        statusLabel.setText("Status: " + selected.getStatusState());
        descriptionLabel.setText("Code: " + selected.getProductCode()
                + "\n\nA quality " + selected.getCategoryName().toLowerCase()
                + " item available now in our marketplace. "
                + "Fast shipping and easy returns.");
        try {
            if (selected.getImageUrl() != null && !selected.getImageUrl().isEmpty())
                image.setImage(new Image(selected.getImageUrl(), true));
        } catch (Exception ignore) {}

        ratingCombo.getItems().setAll(1, 2, 3, 4, 5);
        ratingCombo.getSelectionModel().select(Integer.valueOf(5));

        int maxQty = Math.max(1, Math.min(10, selected.getQuantity()));
        for (int i = 1; i <= maxQty; i++) qtyCombo.getItems().add(i);
        qtyCombo.getSelectionModel().selectFirst();

        boolean isCustomer = SessionManager.getCurrentCustomer() != null;
        boolean inStock = selected.getQuantity() > 0 &&
                          !"Out of Stock".equalsIgnoreCase(selected.getStatusState()) &&
                          !"Discontinued".equalsIgnoreCase(selected.getStatusState());
        if (cartRow != null) {
            cartRow.setVisible(isCustomer && inStock);
            cartRow.setManaged(isCustomer && inStock);
        }

        loadReviews();
    }

    private void loadReviews() {
        reviewsBox.getChildren().clear();
        try {
            for (Review r : reviewDAO.findByProduct(selected.getProductCode())) {
                VBox row = new VBox(2);
                row.getStyleClass().add("review-row");
                Label head = new Label("★".repeat(r.getRate()) + "☆".repeat(5 - r.getRate()) + "   " + r.getCustomerName());
                head.getStyleClass().add("review-head");
                Label body = new Label(r.getTheTopic() == null ? "" : r.getTheTopic());
                body.setWrapText(true);
                row.getChildren().addAll(head, body);
                reviewsBox.getChildren().add(row);
            }
            if (reviewsBox.getChildren().isEmpty()) {
                Label empty = new Label("No reviews yet. Be the first!");
                empty.getStyleClass().add("muted-label");
                reviewsBox.getChildren().add(empty);
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load reviews: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddToCart() {
        if (SessionManager.getCurrentCustomer() == null) {
            AlertHelper.showPermissionDenied("You must be signed in as a Customer to add items to the cart.");
            return;
        }
        Integer qty = qtyCombo.getValue();
        if (qty == null) qty = 1;
        try {
            int cartId = SessionManager.getCurrentCartId();
            if (cartId == 0) {
                cartId = cartDAO.getOrCreateCart(SessionManager.getCurrentCustomer().getCustomerId()).getCartId();
                SessionManager.setCurrentCartId(cartId);
            }
            cartDAO.addItem(cartId, selected.getProductCode(), qty);
            AlertHelper.showInfo("Added to Cart", "\"" + selected.getProductName() + "\" (x" + qty + ") has been added to your cart.");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not add to cart: " + e.getMessage());
        }
    }

    @FXML
    private void handleSubmitReview() {
        if (SessionManager.getCurrentCustomer() == null) {
            AlertHelper.showPermissionDenied("You must be signed in as a Customer to leave a review.");
            return;
        }
        String text = reviewText.getText() == null ? "" : reviewText.getText().trim();
        if (text.isEmpty()) { AlertHelper.showError("Error", "Please write something."); return; }
        Integer rating = ratingCombo.getValue();
        if (rating == null) rating = 5;
        try {
            Review r = new Review();
            r.setReviewCode("RV" + System.currentTimeMillis());
            r.setTheTopic(text);
            r.setRate(rating);
            r.setProductCodeFkU(selected.getProductCode());
            r.setCustomerIdFkU(SessionManager.getCurrentCustomer().getCustomerId());
            reviewDAO.insert(r);
            reviewText.clear();
            loadReviews();
            AlertHelper.showInfo("Thank you!", "Your review has been posted.");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not submit review: " + e.getMessage());
        }
    }

    @FXML
    private void handleGoToCart() {
        try { App.setRoot("Cart"); } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleBack() {
        try { App.setRoot("CategoryProducts"); } catch (Exception e) { e.printStackTrace(); }
    }
}
