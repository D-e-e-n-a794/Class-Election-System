package com.classElection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;
            primaryStage.setTitle("Class Election System");
            loadScene("welcome.fxml");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadScene(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/" + fxmlFileName));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Failed to load FXML: " + fxmlFileName);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
