package com.classElection.controller;

import com.classElection.util.SceneUtil;
import com.classElection.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CandidateListController {

    @FXML
    private TableView<Candidate> candidateTable;
    @FXML
    private TableColumn<Candidate, String> nameColumn;
    @FXML
    private TableColumn<Candidate, String> regNoColumn;
    @FXML
    private TableColumn<Candidate, String> descriptionColumn;
    @FXML
    private TableColumn<Candidate, String> electionTypeColumn;

    private final ObservableList<Candidate> candidates = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        regNoColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        electionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("electionType"));

        // Load initial data
        loadCandidates();
    }

    private void loadCandidates() {
        candidates.clear();

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM candidate ORDER BY election_type, name";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Candidate candidate = new Candidate(
                            rs.getString("name"),
                            rs.getString("registration_number"),
                            rs.getString("description"),
                            rs.getString("election_type")
                    );
                    candidates.add(candidate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // You might want to show an error dialog here
        }

        candidateTable.setItems(candidates);
    }

    @FXML
    private void handleRefresh() {
        loadCandidates();
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) candidateTable.getScene().getWindow();
            SceneUtil.switchScene(stage, "/admin_panel.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Candidate selectedCandidate = candidateTable.getSelectionModel().getSelectedItem();
        if (selectedCandidate == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Candidate Selected");
            alert.setContentText("Please select a candidate to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this candidate?");
        confirmAlert.setContentText("Candidate: " + selectedCandidate.getName());

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection conn = DBUtil.getConnection()) {
                    String sql = "DELETE FROM candidate WHERE name = ? AND registration_number = ? AND election_type = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, selectedCandidate.getName());
                        pstmt.setString(2, selectedCandidate.getRegistrationNumber());
                        pstmt.setString(3, selectedCandidate.getElectionType());
                        int rows = pstmt.executeUpdate();
                        if (rows > 0) {
                            candidates.remove(selectedCandidate);
                            System.out.println("Candidate deleted successfully.");
                        } else {
                            System.out.println("Deletion failed.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Inner class to represent a candidate
    public static class Candidate {
        private final String name;
        private final String registrationNumber;
        private final String description;
        private final String electionType;

        public Candidate(String name, String registrationNumber, String description, String electionType) {
            this.name = name;
            this.registrationNumber = registrationNumber;
            this.description = description;
            this.electionType = electionType;
        }

        public String getName() { return name; }
        public String getRegistrationNumber() { return registrationNumber; }
        public String getDescription() { return description; }
        public String getElectionType() { return electionType; }
    }
}
