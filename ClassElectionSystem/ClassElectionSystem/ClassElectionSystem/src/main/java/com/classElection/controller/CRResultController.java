package com.classElection.controller;

import com.classElection.model.CandidateResult;
import com.classElection.util.DBUtil;
import com.classElection.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class CRResultController {

    @FXML private TableView<CandidateResult> resultsTable;
    @FXML private TableColumn<CandidateResult, String> nameColumn;
    @FXML private TableColumn<CandidateResult, String> regNoColumn;
    @FXML private TableColumn<CandidateResult, Integer> votesColumn;

    private final ObservableList<CandidateResult> resultsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        regNoColumn.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        votesColumn.setCellValueFactory(new PropertyValueFactory<>("votes"));
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
            String sql = "DELETE FROM votes WHERE candidate_id IN " +
                         "(SELECT id FROM candidate WHERE election_type = 'CR')";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
                loadResults();
                showAlert("Votes Reset", "All CR votes have been cleared.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to reset votes.");
        }
    }

    @FXML
    private void handleShowWinner() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT c.name, c.registration_number, COUNT(v.id) AS vote_count " +
                         "FROM candidate c " +
                         "LEFT JOIN votes v ON c.id = v.candidate_id " +
                         "WHERE c.election_type = 'CR' " +
                         "GROUP BY c.id, c.name, c.registration_number " +
                         "ORDER BY vote_count DESC " +
                         "LIMIT 1";

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String winnerName = rs.getString("name");
                    String regNo = rs.getString("registration_number");
                    int votes = rs.getInt("vote_count");

                    showAlert("🎉 CR Winner", "Winner: " + winnerName +
                            "\nReg No: " + regNo +
                            "\nVotes: " + votes);
                } else {
                    showAlert("No Winner", "No votes have been cast yet.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve winner.");
        }
    }

    @FXML
    private void handleDeleteCandidate() {
        CandidateResult selected = resultsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a candidate to delete.");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            
            String checkSql = "SELECT COUNT(*) FROM votes v " +
                              "JOIN candidate c ON v.candidate_id = c.id " +
                              "WHERE c.registration_number = ? AND c.election_type = 'CR'";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, selected.getRegNo());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    showAlert("No Votes", "This candidate has no votes to delete.");
                    return;
                }
            }

            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to delete this candidate and their votes?");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

            
            String deleteVotesSql = "DELETE FROM votes WHERE candidate_id = (SELECT id FROM candidate WHERE registration_number = ? AND election_type = 'CR')";
            try (PreparedStatement deleteVotes = conn.prepareStatement(deleteVotesSql)) {
                deleteVotes.setString(1, selected.getRegNo());
                deleteVotes.executeUpdate();
            }

            
            String deleteCandidateSql = "DELETE FROM candidate WHERE registration_number = ? AND election_type = 'CR'";
            try (PreparedStatement deleteCandidate = conn.prepareStatement(deleteCandidateSql)) {
                deleteCandidate.setString(1, selected.getRegNo());
                deleteCandidate.executeUpdate();
            }

            showAlert("Candidate Deleted", "The candidate and their votes have been successfully deleted.");
            loadResults();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete candidate.");
        }
    }

    private void loadResults() {
        resultsList.clear();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT c.name, c.registration_number, COUNT(v.id) as vote_count " +
                         "FROM candidate c " +
                         "LEFT JOIN votes v ON c.id = v.candidate_id " +
                         "WHERE c.election_type = 'CR' " +
                         "GROUP BY c.id, c.name, c.registration_number " +
                         "ORDER BY vote_count DESC";

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    CandidateResult result = new CandidateResult(
                        rs.getString("name"),
                        rs.getString("registration_number"),
                        rs.getInt("vote_count")
                    );
                    resultsList.add(result);
                }
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


