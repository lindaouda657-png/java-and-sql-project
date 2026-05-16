package com.marketplace.shop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/RoleSelect.fxml"));
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle("Marketplace Shop");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(620);
        stage.show();
    }

    public static Stage getPrimaryStage() { return primaryStage; }

    public static void setRoot(String fxml) throws Exception {
        Parent root = FXMLLoader.load(App.class.getResource("/fxml/" + fxml + ".fxml"));
        primaryStage.getScene().setRoot(root);
    }

    public static void main(String[] args) { launch(args); }
}
