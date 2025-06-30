package com.classElection.controller;

import com.classElection.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class VotingController {

    @FXML
    private ListView<String> candidatesListView;

    @FXML
    private Button voteBtn;

    private ObservableList<String> candidates = FXCollections.observableArrayList(
            "1. Alice (Party A)", "2. Bob (Party B)", "3. Charlie (Party C)"
    );

    @FXML
    public void initialize() {
        candidatesListView.setItems(candidates);
    }

    @FXML
    void handleVote(ActionEvent event) {
        String selected = candidatesListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a candidate before voting.");
            return;
        }
        // Save vote logic would go here

        Stage stage = (Stage) voteBtn.getScene().getWindow();
//        SceneUtil.switchScene(stage, "/fxml/result.fxml");
        SceneUtil.switchScene(stage, "/result.fxml");

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
