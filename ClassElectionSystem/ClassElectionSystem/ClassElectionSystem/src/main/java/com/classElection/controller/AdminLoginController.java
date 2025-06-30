package com.classElection.controller;

import com.classElection.util.DBUtil;
import com.classElection.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    private Label errorLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                System.out.println("admin verified");
                SceneUtil.switchScene(stage, "/admin_panel.fxml");
            } else {
                errorLabel.setText("Invalid username or password");
            }

        } catch (Exception e) {
            errorLabel.setText("Error connecting to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            SceneUtil.switchScene(stage, "/role_selection.fxml");
        } catch (Exception e) {
            errorLabel.setText("Failed to go back: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
