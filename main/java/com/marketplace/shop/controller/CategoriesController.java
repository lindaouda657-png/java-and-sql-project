package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.CategoryDAO;
import com.marketplace.shop.model.Category;
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

public class CategoriesController implements Initializable {

    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, Integer> colId;
    @FXML private TableColumn<Category, String> colName;
    @FXML private TextField nameField;

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        loadCategories();

        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) nameField.setText(newVal.getCategoryName());
        });
    }

    private void loadCategories() {
        try {
            categoryList.setAll(categoryDAO.findAll());
            categoryTable.setItems(categoryList);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load categories: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_CATEGORIES, "add a category")) return;
        try {
            Category c = new Category();
            c.setCategoryName(nameField.getText().trim());
            c.setStatuseCodeFk("ACTIVE");
            categoryDAO.insert(c);
            AlertHelper.showInfo("Success", "Category added.");
            loadCategories();
            nameField.clear();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not add: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (!PermissionHelper.requirePermission(PermissionHelper.MANAGE_CATEGORIES, "delete a category")) return;
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("Error", "Select a category first."); return; }
        if (AlertHelper.showConfirmation("Delete", "Delete category " + selected.getCategoryName() + "?")) {
            try {
                categoryDAO.delete(selected.getCategoryId());
                loadCategories();
                nameField.clear();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete: " + e.getMessage());
            }
        }
    }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }
}
