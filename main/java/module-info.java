module com.marketplace.shop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.marketplace.shop to javafx.fxml;
    opens com.marketplace.shop.controller to javafx.fxml;
    opens com.marketplace.shop.model to javafx.base;

    exports com.marketplace.shop;
}
