package com.classElection.controller;

import com.classElection.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CandidateEntryController {

    @FXML
    private TextField candidateNameField;

    @FXML
    private TextField partyField;

    @FXML
    private ListView<String> candidateListView;

    @FXML
    private Button addCandidateBtn;

    @FXML
    private Button proceedBtn;

    private ObservableList<String> candidateList = FXCollections.observableArrayList();
    private int candidateIdCounter = 1;

    @FXML
    public void initialize() {
        candidateListView.setItems(candidateList);
    }

    @FXML
    void handleAddCandidate(ActionEvent event) {
        String name = candidateNameField.getText();
        String party = partyField.getText();
        if (name.isEmpty() || party.isEmpty()) {
            showAlert("Validation Error", "Please enter both candidate name and party.");
            return;
        }
        String candidateInfo = candidateIdCounter++ + ". " + name + " (" + party + ")";
        candidateList.add(candidateInfo);
        candidateNameField.clear();
        partyField.clear();
    }

    @FXML
    void handleProceed(ActionEvent event) {
        if (candidateList.isEmpty()) {
            showAlert("Validation Error", "Add at least one candidate to proceed.");
            return;
        }
        Stage stage = (Stage) proceedBtn.getScene().getWindow();
        SceneUtil.switchScene(stage, "/voting.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
