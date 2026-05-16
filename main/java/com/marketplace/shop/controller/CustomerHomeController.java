package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.CategoryDAO;
import com.marketplace.shop.model.Category;
import com.marketplace.shop.model.Customer;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerHomeController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private FlowPane categoriesPane;

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Customer c = SessionManager.getCurrentCustomer();
        welcomeLabel.setText("Hello, " + (c == null ? "Guest" : c.getCustomerName()));
        loadCategories();
    }

    private void loadCategories() {
        try {
            List<Category> cats = categoryDAO.findAll();
            categoriesPane.getChildren().clear();
            for (Category cat : cats) {
                Button card = new Button();
                card.getStyleClass().add("category-card");
                card.setPrefSize(200, 110);
                VBox box = new VBox(6);
                box.setStyle("-fx-alignment: center;");
                Label icon = new Label(emojiFor(cat.getCategoryName()));
                icon.getStyleClass().add("card-icon");
                Label name = new Label(cat.getCategoryName());
                name.getStyleClass().add("card-title");
                box.getChildren().addAll(icon, name);
                card.setGraphic(box);
                card.setOnAction(e -> openCategory(cat));
                categoriesPane.getChildren().add(card);
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load categories: " + e.getMessage());
        }
    }

    private String emojiFor(String name) {
        if (name == null) return "🛍";
        String n = name.toLowerCase();
        if (n.contains("electron")) return "📱";
        if (n.contains("cloth"))    return "👕";
        if (n.contains("home"))     return "🏠";
        if (n.contains("book"))     return "📚";
        if (n.contains("sport"))    return "⚽";
        return "🛍";
    }

    private void openCategory(Category cat) {
        SessionManager.setSelectedCategoryId(cat.getCategoryId());
        SessionManager.setSelectedCategoryName(cat.getCategoryName());
        try { App.setRoot("CategoryProducts"); }
        catch (Exception ex) { AlertHelper.showError("Navigation", ex.getMessage()); }
    }

    @FXML private void goToProfile()  { nav("CustomerProfile"); }
    @FXML private void goToOrders()   { nav("CustomerOrders"); }
    @FXML private void goToCart()     { nav("Cart"); }
    @FXML private void goToAllProducts() {
        SessionManager.setSelectedCategoryId(0);
        SessionManager.setSelectedCategoryName("All Products");
        nav("CategoryProducts");
    }

    @FXML
    private void handleLogout() {
        if (AlertHelper.showConfirmation("Logout", "Are you sure?")) {
            SessionManager.logout();
            nav("RoleSelect");
        }
    }

    private void nav(String fxml) {
        try { App.setRoot(fxml); } catch (Exception e) { AlertHelper.showError("Navigation", e.getMessage()); }
    }
}
