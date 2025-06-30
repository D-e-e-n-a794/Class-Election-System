package com.classElection.controller;

import com.classElection.model.CandidateResult;
import com.classElection.util.DBUtil;
import com.classElection.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class PresidentResultController {

    @FXML private TableView<CandidateResult> resultsTable;
    @FXML private TableColumn<CandidateResult, String> nameColumn;
    @FXML private TableColumn<CandidateResult, String> regNoColumn;
    @FXML private TableColumn<CandidateResult, Integer> votesColumn;

    private final ObservableList<CandidateResult> resultsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        regNoColumn.setCellValueFactory(data -> data.getValue().regNoProperty());
        votesColumn.setCellValueFactory(data -> data.getValue().votesProperty().asObject());

        resultsTable.setItems(resultsList);
        loadResults();
    }

    @FXML
    private void handleRefresh() {
        loadResults();
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) resultsTable.getScene().getWindow();
            SceneUtil.switchScene(stage, "/admin_panel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResetVotes() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "DELETE FROM votes WHERE election_type = 'President'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
                loadResults();
                showAlert("Votes Reset", "All President votes have been cleared.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to reset votes.");
        }
    }

    @FXML
    private void handleShowWinner() {
        String sql = """
            SELECT c.name, c.registration_number, COUNT(v.id) AS votes_count
            FROM candidate c
            LEFT JOIN votes v ON c.id = v.candidate_id AND v.election_type = 'President'
            WHERE c.election_type = 'President'
            GROUP BY c.id, c.name, c.registration_number
            ORDER BY votes_count DESC
            LIMIT 1
            """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String name = rs.getString("name");
                String regNo = rs.getString("registration_number");
                int votes = rs.getInt("votes_count");

                showAlert("🎉 President Winner", "Winner: " + name +
                        "\nReg No: " + regNo +
                        "\nVotes: " + votes);
            } else {
                showAlert("No Winner", "No votes have been cast yet.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not fetch winner.");
        }
    }

    private void loadResults() {
        resultsList.clear();

        String sql = """
            SELECT c.name, c.registration_number, COUNT(v.id) AS votes_count
            FROM candidate c
            LEFT JOIN votes v ON c.id = v.candidate_id AND v.election_type = 'President'
            WHERE c.election_type = 'President'
            GROUP BY c.id, c.name, c.registration_number
            ORDER BY votes_count DESC
            """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultsList.add(new CandidateResult(
                        rs.getString("name"),
                        rs.getString("registration_number"),
                        rs.getInt("votes_count")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
