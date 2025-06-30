package com.classElection.controller;

import com.classElection.util.DBUtil;
import com.classElection.util.SceneUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminPanelController {

    @FXML
    private ComboBox<String> electionTypeCombo;

    @FXML
    private TextField nameField;

    @FXML
    private TextField regNoField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button showCandidatesBtn;

    @FXML
    public void initialize() {
        // Optional: initialize components if needed
    }

    @FXML
    void handleAddCandidate() {
        String electionType = electionTypeCombo.getValue();
        String name = nameField.getText().trim();
        String regNo = regNoField.getText().trim();

        if (electionType == null || name.isEmpty() || regNo.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Election type, name, and registration number are required.");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO candidate (name, registration_number, election_type) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, regNo);
            stmt.setString(3, electionType);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Candidate added successfully.");
                electionTypeCombo.setValue(null);
                nameField.clear();
                regNoField.clear();
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Failed to add candidate.");
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleShowCRResults() {
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            SceneUtil.switchScene(stage, "/cr_candidates.fxml");
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error showing CR results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleShowPresidentResults() {
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            SceneUtil.switchScene(stage, "/PresidentResult.fxml");
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error showing President results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowCandidates() {
        try {
            Stage stage = (Stage) showCandidatesBtn.getScene().getWindow();
            SceneUtil.switchScene(stage, "/candidate_list.fxml");
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error showing candidates: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToMain() {
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            SceneUtil.switchScene(stage, "/role_selection.fxml");
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error returning to main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit, or Cancel to stay.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            System.exit(0);
        }
    }
}
