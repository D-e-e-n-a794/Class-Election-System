package com.classElection.controller;

import com.classElection.model.Candidate;
import com.classElection.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class CRCandidateListController {

    @FXML
    private TableView<Candidate> tableView;

    @FXML
    private TableColumn<Candidate, String> nameColumn;

    @FXML
    private TableColumn<Candidate, String> regNoColumn;

    @FXML
    private TableColumn<Candidate, String> descColumn;

    @FXML
    private TextField voterRegNoField;

    @FXML
    private Label voteStatusLabel;

    private ObservableList<Candidate> candidates = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        regNoColumn.setCellValueFactory(cellData -> cellData.getValue().regNoProperty());
        descColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        loadCandidates();
    }

    private void loadCandidates() {
        candidates.clear();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM candidate WHERE election_type = 'CR'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                candidates.add(new Candidate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("registration_number"),
                        rs.getString("description")
                ));
            }

            tableView.setItems(candidates);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVote() {
        Candidate selected = tableView.getSelectionModel().getSelectedItem();
        String voterRegNo = voterRegNoField.getText().trim();

        if (selected == null) {
            voteStatusLabel.setText("Select a candidate.");
            return;
        }

        if (voterRegNo.isEmpty()) {
            voteStatusLabel.setText("Enter your reg no.");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            // Check if already voted
            String checkSql = "SELECT * FROM votes WHERE student_reg_no = ? AND election_type = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, voterRegNo);
            checkStmt.setString(2, "CR");
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                voteStatusLabel.setText("You already voted for CR.");
                return;
            }

            // Insert vote
            String insertSql = "INSERT INTO votes (student_reg_no, candidate_id, election_type) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, voterRegNo);
            insertStmt.setInt(2, selected.getId());
            insertStmt.setString(3, "CR");

            int rows = insertStmt.executeUpdate();
            if (rows > 0) {
                voteStatusLabel.setText("Vote submitted successfully!");
            } else {
                voteStatusLabel.setText("Vote failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            voteStatusLabel.setText("Error: " + e.getMessage());
        }
    }
}
