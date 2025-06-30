package com.classElection.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SceneUtil {
    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            URL fxmlResource = SceneUtil.class.getResource(fxmlPath);
            if (fxmlResource == null) {
                throw new IllegalArgumentException("FXML file not found at path: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fxmlResource);
            Scene scene = new Scene(loader.load());

            URL cssResource = SceneUtil.class.getResource("/style/theme.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
            }

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
