package com.marketplace.shop.controller;

import com.marketplace.shop.App;
import com.marketplace.shop.dao.DepartmentDAO;
import com.marketplace.shop.dao.PermissionDAO;
import com.marketplace.shop.dao.UserCredentialDAO;
import com.marketplace.shop.dao.UserDAO;
import com.marketplace.shop.dao.UserPermissionDAO;
import com.marketplace.shop.model.Department;
import com.marketplace.shop.model.Permission;
import com.marketplace.shop.model.User;
import com.marketplace.shop.model.UserCredential;
import com.marketplace.shop.util.AlertHelper;
import com.marketplace.shop.util.DatabaseConnection;
import com.marketplace.shop.util.PermissionHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class EmployeesController implements Initializable {

    @FXML private TableView<User> employeeTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String>  colName, colEmail, colDept, colStatus;
    @FXML private Button managePermsButton, addButton, fireButton, banButton,
                         deleteButton, viewProfileButton;

    private final UserDAO           userDAO           = new UserDAO();
    private final UserCredentialDAO credentialDAO     = new UserCredentialDAO();
    private final UserPermissionDAO userPermissionDAO = new UserPermissionDAO();
    private final PermissionDAO     permissionDAO     = new PermissionDAO();
    private final DepartmentDAO     departmentDAO     = new DepartmentDAO();
    private final ObservableList<User> employeeList   = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        colDept.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getStatusName()));

        boolean admin = PermissionHelper.isAdmin();
        if (managePermsButton  != null) managePermsButton.setDisable(!admin);
        if (addButton          != null) addButton.setDisable(!admin);
        if (fireButton         != null) fireButton.setDisable(!admin);
        if (banButton          != null) banButton.setDisable(!admin);
        if (deleteButton       != null) deleteButton.setDisable(!admin);
        if (viewProfileButton  != null) viewProfileButton.setDisable(!admin);

        loadEmployees();
    }

    private void loadEmployees() {
        try {
            List<User> all = userDAO.findAll();
            employeeList.setAll(all.stream()
                    .filter(u -> !("admin@shop.com".equalsIgnoreCase(u.getUserEmail())
                               || "admin@gmail.com".equalsIgnoreCase(u.getUserEmail())))
                    .toList());
            employeeTable.setItems(employeeList);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not load employees: " + e.getMessage());
        }
    }

    @FXML private void handleRefresh() { loadEmployees(); }

    @FXML
    private void handleViewProfile() {
        if (!PermissionHelper.requireAdmin("view employee profiles")) return;
        User sel = selected(); if (sel == null) return;
        openProfileDialog(sel);
    }

    private void openProfileDialog(User emp) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(24));
        box.getChildren().add(bold("Employee Profile"));
        box.getChildren().add(infoRow("ID",         String.valueOf(emp.getUserId())));
        box.getChildren().add(infoRow("Name",        emp.getUserName()));
        box.getChildren().add(infoRow("Email",       emp.getUserEmail()));
        box.getChildren().add(infoRow("Phone",       nvl(emp.getPhoneNo())));
        box.getChildren().add(infoRow("Department",  emp.getDepartmentName()));
        box.getChildren().add(infoRow("Status",      emp.getStatusName()));
        box.getChildren().add(infoRow("Salary",      emp.getUserSalary() != null ? "EGP " + emp.getUserSalary().toPlainString() : "—"));
        box.getChildren().add(new Separator());
        box.getChildren().add(bold("Address"));
        box.getChildren().add(infoRow("City",      nvl(emp.getCity())));
        box.getChildren().add(infoRow("Street",    nvl(emp.getStreetName())));
        box.getChildren().add(infoRow("Building",  nvl(emp.getBuildingNo())));
        box.getChildren().add(infoRow("Apartment", nvl(emp.getApartmentNo())));
        Button close = new Button("Close"); close.getStyleClass().add("secondary-button");
        box.getChildren().add(close);
        Stage dlg = mkDialog("Profile — " + emp.getUserName(), box, 400, 500);
        close.setOnAction(e -> dlg.close());
        dlg.showAndWait();
    }

    @FXML
    private void handleAddEmployee() {
        if (!PermissionHelper.requireAdmin("add employees")) return;
        try { openAddDialog(); }
        catch (Exception e) { AlertHelper.showError("Error", "Could not open form: " + e.getMessage()); }
    }

    private void openAddDialog() throws Exception {
        List<Department> depts = departmentDAO.findAll();

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10); grid.setPadding(new Insets(24));

        TextField nameF     = new TextField(); nameF.setPromptText("Full name");
        TextField emailF    = new TextField(); emailF.setPromptText("employee@shop.com");
        TextField phoneF    = new TextField(); phoneF.setPromptText("01XXXXXXXXX");
        TextField passwordF = new TextField(); passwordF.setPromptText("initial password");
        TextField salaryF   = new TextField(); salaryF.setPromptText("0.00");
        TextField cityF     = new TextField();
        TextField streetF   = new TextField();
        TextField buildingF = new TextField();
        TextField aptF      = new TextField();
        ComboBox<Department> deptBox = new ComboBox<>(FXCollections.observableArrayList(depts));
        deptBox.setMaxWidth(Double.MAX_VALUE);

        int r = 0;
        grid.addRow(r++, lbl("Full Name *"), nameF);
        grid.addRow(r++, lbl("Email *"),     emailF);
        grid.addRow(r++, lbl("Phone"),       phoneF);
        grid.addRow(r++, lbl("Password *"),  passwordF);
        grid.addRow(r++, lbl("Department *"),deptBox);
        grid.addRow(r++, lbl("Salary (EGP)"),salaryF);
        grid.addRow(r++, lbl("City"),        cityF);
        grid.addRow(r++, lbl("Street"),      streetF);
        grid.addRow(r++, lbl("Building #"),  buildingF);
        grid.addRow(r++, lbl("Apartment #"), aptF);

        Button save   = new Button("Add Employee"); save.getStyleClass().add("primary-button");
        Button cancel = new Button("Cancel");       cancel.getStyleClass().add("secondary-button");
        VBox box = new VBox(12, grid, new HBox(10, save, cancel));
        box.setPadding(new Insets(10, 24, 24, 24));

        Stage dlg = mkDialog("Add New Employee", box, 500, 530);
        cancel.setOnAction(e -> dlg.close());
        save.setOnAction(e -> {
            String name  = nameF.getText().trim();
            String email = emailF.getText().trim();
            String pwd   = passwordF.getText().trim();
            Department dept = deptBox.getValue();
            if (name.isEmpty() || email.isEmpty() || pwd.isEmpty() || dept == null) {
                AlertHelper.showError("Validation", "Name, email, password and department are required.");
                return;
            }
            BigDecimal salary = BigDecimal.ZERO;
            try {
                if (!salaryF.getText().trim().isEmpty())
                    salary = new BigDecimal(salaryF.getText().trim());
            } catch (NumberFormatException ex) {
                AlertHelper.showError("Validation", "Salary must be a valid number.");
                return;
            }
            try {
                User u = new User();
                u.setUserName(name);
                u.setUserEmail(email);
                u.setPhoneNo(phoneF.getText().trim());
                u.setStatuseCodeFk("ACTIVE");
                u.setDepartmentIdFk(dept.getDepartmentId());
                u.setUserSalary(salary);
                u.setCity(cityF.getText().trim());
                u.setStreetName(streetF.getText().trim());
                u.setBuildingNo(buildingF.getText().trim());
                u.setApartmentNo(aptF.getText().trim());
                userDAO.insert(u);

                UserCredential cred = new UserCredential();
                cred.setUserIdFk(u.getUserId());
                cred.setPasswordHash(pwd);
                credentialDAO.insert(cred);

                AlertHelper.showInfo("Success", "Employee '" + name + "' added successfully.\nThey can now log in with:\n  Email: " + email + "\n  Password: " + pwd);
                dlg.close();
                loadEmployees();
            } catch (Exception ex) {
                AlertHelper.showError("Error", "Could not add employee: " + ex.getMessage());
            }
        });
        dlg.showAndWait();
    }

    @FXML
    private void handleFireEmployee() {
        if (!PermissionHelper.requireAdmin("fire employees")) return;
        User sel = selected(); if (sel == null) return;
        if ("FIRED".equalsIgnoreCase(sel.getStatuseCodeFk())) {
            AlertHelper.showError("Already Fired", sel.getUserName() + " is already fired.");
            return;
        }
        if (!AlertHelper.showConfirmation("Fire Employee",
                "Fire " + sel.getUserName() + "?\n\nThey will be unable to log in until you re-activate their account."))
            return;
        try {
            sel.setStatuseCodeFk("FIRED");
            userDAO.update(sel);
            AlertHelper.showInfo("Fired", sel.getUserName() + " has been fired and can no longer log in.");
            loadEmployees();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not fire employee: " + e.getMessage());
        }
    }

    @FXML
    private void handleBanEmployee() {
        if (!PermissionHelper.requireAdmin("ban employees")) return;
        User sel = selected(); if (sel == null) return;
        if ("BANNED".equalsIgnoreCase(sel.getStatuseCodeFk())) {
            AlertHelper.showError("Already Banned", sel.getUserName() + " is already banned.");
            return;
        }
        if (!AlertHelper.showConfirmation("Ban Employee",
                "Ban " + sel.getUserName() + "?\n\nAccess will be denied until you explicitly lift the ban."))
            return;
        try {
            sel.setStatuseCodeFk("BANNED");
            userDAO.update(sel);
            AlertHelper.showInfo("Banned", sel.getUserName() + " has been banned.");
            loadEmployees();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not ban employee: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteEmployee() {
        if (!PermissionHelper.requireAdmin("delete employee accounts")) return;
        User sel = selected(); if (sel == null) return;
        if (!AlertHelper.showConfirmation("Delete Account",
                "Permanently delete the account of " + sel.getUserName() + "?\n\n" +
                "This cannot be undone. Order and payment history will be preserved."))
            return;
        try {
            try (java.sql.PreparedStatement ps =
                    DatabaseConnection.getConnection()
                        .prepareStatement("DELETE FROM user_credentials WHERE user_id_fk=?")) {
                ps.setInt(1, sel.getUserId());
                ps.executeUpdate();
            }
            userDAO.delete(sel.getUserId());
            AlertHelper.showInfo("Deleted", sel.getUserName() + "'s account has been permanently deleted.");
            loadEmployees();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Could not delete account: " + e.getMessage());
        }
    }

    @FXML
    private void handleManagePermissions() {
        if (!PermissionHelper.requireAdmin("assign permissions")) return;
        User sel = selected(); if (sel == null) return;
        try { openPermissionsDialog(sel); }
        catch (Exception e) { AlertHelper.showError("Error", "Could not open permissions: " + e.getMessage()); }
    }

    @FXML
    private void handleSetSalary() {
        if (!PermissionHelper.requireAdmin("set employee salary")) return;
        User sel = selected(); if (sel == null) return;

        VBox box = new VBox(14);
        box.setPadding(new Insets(24));
        box.getChildren().add(bold("Set Salary — " + sel.getUserName()));

        String current = sel.getUserSalary() != null ? sel.getUserSalary().toPlainString() : "0.00";
        box.getChildren().add(new Label("Current salary: EGP " + current));

        TextField salaryField = new TextField(current);
        salaryField.setPromptText("Enter new salary");
        box.getChildren().add(new HBox(8, lbl("New Salary (EGP)"), salaryField));

        Button save   = new Button("Save Salary"); save.getStyleClass().add("primary-button");
        Button cancel = new Button("Cancel");      cancel.getStyleClass().add("secondary-button");
        box.getChildren().add(new HBox(10, save, cancel));

        Stage dlg = mkDialog("Set Salary — " + sel.getUserName(), box, 380, 200);
        cancel.setOnAction(ev -> dlg.close());
        save.setOnAction(ev -> {
            try {
                BigDecimal newSalary = new BigDecimal(salaryField.getText().trim());
                sel.setUserSalary(newSalary);
                userDAO.update(sel);
                AlertHelper.showInfo("Saved", sel.getUserName() + "'s salary updated to EGP " + newSalary.toPlainString());
                dlg.close();
                loadEmployees();
            } catch (NumberFormatException ex) {
                AlertHelper.showError("Validation", "Please enter a valid number for salary.");
            } catch (Exception ex) {
                AlertHelper.showError("Error", "Could not update salary: " + ex.getMessage());
            }
        });
        dlg.showAndWait();
    }

    private void openPermissionsDialog(User employee) throws Exception {
        List<Permission> all = permissionDAO.findAll();
        Set<Integer> currentlyGranted = userPermissionDAO.findPermissionIdsForUser(employee.getUserId());

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.getChildren().add(bold("Permissions for: " + employee.getUserName()));
        box.getChildren().add(new Label("Tick what this employee is allowed to do in the shop."));

        Set<Integer> selectedPerms = new HashSet<>(currentlyGranted);
        for (Permission p : all) {
            if (PermissionHelper.MANAGE_EMPLOYEES.equals(p.getPermissionCode())) continue;
            CheckBox cb = new CheckBox(prettyLabel(p.getPermissionCode()));
            cb.setSelected(currentlyGranted.contains(p.getPermissionId()));
            cb.selectedProperty().addListener((obs, was, is) -> {
                if (Boolean.TRUE.equals(is)) selectedPerms.add(p.getPermissionId());
                else selectedPerms.remove(p.getPermissionId());
            });
            box.getChildren().add(cb);
        }

        Button save   = new Button("Save");   save.getStyleClass().add("primary-button");
        Button cancel = new Button("Cancel"); cancel.getStyleClass().add("secondary-button");
        box.getChildren().add(new HBox(10, save, cancel));

        Stage dlg = mkDialog("Manage Permissions — " + employee.getUserName(), box, 460, 360);
        cancel.setOnAction(ev -> dlg.close());
        save.setOnAction(ev -> {
            try {
                userPermissionDAO.replaceForUser(employee.getUserId(), selectedPerms);
                AlertHelper.showInfo("Saved", "Permissions updated for " + employee.getUserName() + ".");
                dlg.close();
                employeeTable.refresh();
            } catch (Exception ex) {
                AlertHelper.showError("Error", "Could not save permissions: " + ex.getMessage());
            }
        });
        dlg.showAndWait();
    }

    private User selected() {
        User sel = employeeTable.getSelectionModel().getSelectedItem();
        if (sel == null) AlertHelper.showError("No Selection", "Please select an employee from the table first.");
        return sel;
    }

    private Stage mkDialog(String title, javafx.scene.Parent content, double w, double h) {
        Stage dlg = new Stage();
        dlg.initOwner(App.getPrimaryStage());
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle(title);
        Scene sc = new Scene(content, w, h);
        try { sc.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm()); }
        catch (Exception ignore) {}
        dlg.setScene(sc);
        return dlg;
    }

    private Label lbl(String t)  { Label l = new Label(t); l.setMinWidth(110); return l; }
    private Label bold(String t) { Label l = new Label(t); l.setStyle("-fx-font-weight:bold;-fx-font-size:13;"); return l; }
    private String nvl(String s) { return (s == null || s.isBlank()) ? "—" : s; }

    private HBox infoRow(String key, String val) {
        Label k = new Label(key + ":"); k.setMinWidth(90); k.setStyle("-fx-font-weight:bold;");
        return new HBox(8, k, new Label(val));
    }

    private String prettyLabel(String code) {
        return switch (code) {
            case "MANAGE_PRODUCTS"   -> "Manage products  (add / update / delete)";
            case "MANAGE_ORDERS"     -> "Manage orders  (take orders, change status)";
            case "MANAGE_CUSTOMERS"  -> "Manage customers  (add / edit / ban / delete)";
            case "MANAGE_PAYMENTS"   -> "Manage payments  (record / refund)";
            case "MANAGE_CATEGORIES" -> "Manage categories  (add / delete)";
            case "MANAGE_REVIEWS"    -> "Moderate reviews";
            default -> code;
        };
    }

    @FXML private void handleBack() {
        try { App.setRoot("Dashboard"); } catch (Exception e) { e.printStackTrace(); }
    }
}
