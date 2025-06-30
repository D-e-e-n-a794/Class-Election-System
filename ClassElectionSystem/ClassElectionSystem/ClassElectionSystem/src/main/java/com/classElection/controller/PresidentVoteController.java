package com.classElection.controller;

import com.classElection.util.DBUtil;
import com.classElection.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class PresidentVoteController {

    @FXML
    private ComboBox<String> candidateComboBox;

    @FXML
    private TextField regNoField;

    @FXML
    private Label statusLabel;

    private final ObservableList<String> candidateList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadCandidates();
    }

    private void loadCandidates() {
        candidateList.clear();
        String sql = "SELECT name FROM candidate WHERE election_type = 'President'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                candidateList.add(rs.getString("name"));
            }

            candidateComboBox.setItems(candidateList);

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error loading candidates.");
        }
    }

    @FXML
    private void handleVote() {
        String selectedCandidate = candidateComboBox.getValue();
        String regNo = regNoField.getText().trim();

        if (selectedCandidate == null || regNo.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            // 1. Check if voter already voted
            String checkSql = "SELECT * FROM votes WHERE voter_reg_no = ? AND election_type = 'President'";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, regNo);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setText("You have already voted.");
                    return;
                }
            }

            // 2. Get candidate ID
            int candidateId = -1;
            String candidateSql = "SELECT id FROM candidate WHERE name = ? AND election_type = 'President'";
            try (PreparedStatement candidateStmt = conn.prepareStatement(candidateSql)) {
                candidateStmt.setString(1, selectedCandidate);
                ResultSet rs = candidateStmt.executeQuery();
                if (rs.next()) {
                    candidateId = rs.getInt("id");
                } else {
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setText("Candidate not found.");
                    return;
                }
            }

            // 3. Insert vote
            String insertSql = "INSERT INTO votes (candidate_id, voter_reg_no, election_type) VALUES (?, ?, 'President')";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, candidateId);
                insertStmt.setString(2, regNo);
                insertStmt.executeUpdate();
            }

            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Vote cast successfully!");
            candidateComboBox.getSelectionModel().clearSelection();
            regNoField.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error submitting vote: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) regNoField.getScene().getWindow();
        SceneUtil.switchScene(stage, "/election_type.fxml");
    }

    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thank You");
        alert.setHeaderText(null);
        alert.setContentText("Thanks for voting!");

        alert.setOnHidden(event -> {
            Stage stage = (Stage) regNoField.getScene().getWindow();
            stage.close(); // Close the app window
        });

        alert.show();
    }
}

