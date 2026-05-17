package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.ProductDAO;
import com.marketplace.shop.dao.ProductImageDAO;
import com.marketplace.shop.dao.CategoryDAO;
import com.marketplace.shop.dao.ProductStatusDAO;
import com.marketplace.shop.model.Product;
import com.marketplace.shop.model.ProductImage;
import com.marketplace.shop.model.Category;
import com.marketplace.shop.model.ProductStatus;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.PermissionHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colCode, colName, colCategory, colStatus;
    @FXML private TableColumn<Product, BigDecimal> colPrice;
    @FXML private TableColumn<Product, Integer> colQty;
    @FXML private TextField searchField;
    @FXML private TextField codeField, nameField, priceField, qtyField;
    @FXML private ComboBox<Category> categoryCombo;
    @FXML private ComboBox<ProductStatus> statusCombo;
    @FXML private ImageView previewImage;
    @FXML private Label imagePathLabel;
    @FXML private Button uploadImageButton, addButton, updateButton, deleteButton;

    private final ProductDAO productDAO = new ProductDAO();
    private final ProductImageDAO productImageDAO = new ProductImageDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductStatusDAO productStatusDAO = new ProductStatusDAO();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private String pendingImageUrl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusState"));
        loadProducts();
        loadCategories();
        loadStatuses();

        boolean canWrite = PermissionHelper.canPerform(PermissionHelper.MANAGE_PRODUCTS);
        if (addButton != null) addButton.setDisable(!canWrite);
        if (updateButton != null) updateButton.setDisable(!canWrite);
        if (deleteButton != null) deleteButton.setDisable(!canWrite);
        if (uploadImageButton != null) uploadImageButton.setDisable(!canWrite);

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateFields(newVal);
        });
    }

    private void loadProducts() {
        try {
            productList.setAll(productDAO.findAll());
            productTable.setItems(productList);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load products: " + e.getMessage());
        }
    }

    private void loadCategories() {
        try {
            categoryCombo.setItems(FXCollections.observableArrayList(categoryDAO.findAll()));
        } catch (Exception e) { AlertHelper.showError("Error", "Could not load categories: " + e.getMessage()); }
    }

    private void loadStatuses() {
        try {
            statusCombo.setItems(FXCollections.observableArrayList(productStatusDAO.findAll()));
            if (!statusCombo.getItems().isEmpty()) statusCombo.getSelectionModel().selectFirst();
        } catch (Exception e) { AlertHelper.showError("Error", "Could not load statuses: " + e.getMessage()); }
    }

    private void populateFields(Product p) {
        codeField.setText(p.getProductCode());
        nameField.setText(p.getProductName());
        priceField.setText(p.getUnitPrice().toString());
        qtyField.setText(String.valueOf(p.getQuantity()));
        statusCombo.getItems().stream()
            .filter(s -> s.getProductStatuseId() == p.getProductStatuseIdFk())
            .findFirst().ifPresent(statusCombo.getSelectionModel()::select);
        categoryCombo.getItems().stream()
            .filter(c -> c.getCategoryId() == p.getCategoryIdFk())
            .findFirst().ifPresent(categoryCombo.getSelectionModel()::select);
        pendingImageUrl = null;
        showPreview(p.getImageUrl());
        if (imagePathLabel != null) imagePathLabel.setText(p.getImageUrl() == null ? "No image" : "Saved image");
    }

    private void showPreview(String url) {
        if (previewImage == null) return;
        try {
            if (url != null && !url.isEmpty()) previewImage.setImage(new Image(url, true));
            else previewImage.setImage(null);
        } catch (Exception ignore) { previewImage.setImage(null); }
    }

    @FXML
    private void handleUploadImage() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_PRODUCTS, "upload a product photo")) return;
        FileChooser fc = new FileChooser();
        fc.setTitle("Select product photo");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selected = fc.showOpenDialog(App.getPrimaryStage());
        if (selected == null) return;
        try {

            Path targetDir = Paths.get(System.getProperty("user.home"), ".marketplace-shop", "product_images");
            Files.createDirectories(targetDir);
            String name = System.currentTimeMillis() + "_" + selected.getName().replaceAll("\\s+", "_");
            Path target = targetDir.resolve(name);
            Files.copy(selected.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            pendingImageUrl = target.toUri().toString();
            showPreview(pendingImageUrl);
            if (imagePathLabel != null) imagePathLabel.setText("Ready to save: " + name);
        } catch (IOException ex) {
            AlertHelper.showError("Upload failed", "Could not copy image: " + ex.getMessage());
        }
    }

    private void persistPendingImage(String productCode) {
        if (pendingImageUrl == null) return;
        try {
            productImageDAO.clearPrimary(productCode);
            ProductImage img = new ProductImage();
            img.setProductCodeFk(productCode);
            img.setImageUrl(pendingImageUrl);
            img.setPrimary(true);
            productImageDAO.insert(img);
            pendingImageUrl = null;
        } catch (Exception e) {
            AlertHelper.showError("Image error", "Product saved but image not stored: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_PRODUCTS, "add a product")) return;
        try {
            if (codeField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()
                    || priceField.getText().trim().isEmpty() || qtyField.getText().trim().isEmpty()) {
                AlertHelper.showError("Error", "Please fill in all fields.");
                return;
            }
            if (categoryCombo.getValue() == null) { AlertHelper.showError("Error", "Please select a category."); return; }
            if (statusCombo.getValue() == null) { AlertHelper.showError("Error", "Please select a status."); return; }
            Product p = new Product();
            p.setProductCode(codeField.getText().trim());
            p.setProductName(nameField.getText().trim());
            p.setUnitPrice(new BigDecimal(priceField.getText().trim()));
            p.setQuantity(Integer.parseInt(qtyField.getText().trim()));
            p.setCategoryIdFk(categoryCombo.getValue().getCategoryId());
            p.setProductStatuseIdFk(statusCombo.getValue().getProductStatuseId());
            productDAO.insert(p);
            persistPendingImage(p.getProductCode());
            AlertHelper.showInfo("Success", "Product added successfully.");
            loadProducts();
            clearFields();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not add product: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_PRODUCTS, "update a product")) return;
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("Error", "Select a product first."); return; }
        try {
            selected.setProductName(nameField.getText().trim());
            selected.setUnitPrice(new BigDecimal(priceField.getText().trim()));
            selected.setQuantity(Integer.parseInt(qtyField.getText().trim()));
            if (categoryCombo.getValue() != null) selected.setCategoryIdFk(categoryCombo.getValue().getCategoryId());
            if (statusCombo.getValue() != null) selected.setProductStatuseIdFk(statusCombo.getValue().getProductStatuseId());
            productDAO.update(selected);
            persistPendingImage(selected.getProductCode());
            AlertHelper.showInfo("Success", "Product updated.");
            loadProducts();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not update: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_PRODUCTS, "delete a product")) return;
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("Error", "Select a product first."); return; }
        if (AlertHelper.showConfirmation("Delete", "Delete product " + selected.getProductName() + "?")) {
            try {
                productDAO.delete(selected.getProductCode());
                loadProducts();
                clearFields();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        try {
            if (keyword.isEmpty()) loadProducts();
            else productList.setAll(productDAO.search(keyword));
        } catch (Exception e) {
            AlertHelper.showError("Error", "Search failed: " + e.getMessage());
        }
    }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearFields() {
        codeField.clear(); nameField.clear(); priceField.clear(); qtyField.clear();
        categoryCombo.getSelectionModel().clearSelection();
        statusCombo.getSelectionModel().selectFirst();
        pendingImageUrl = null;
        if (previewImage != null) previewImage.setImage(null);
        if (imagePathLabel != null) imagePathLabel.setText("No image");
    }
}
