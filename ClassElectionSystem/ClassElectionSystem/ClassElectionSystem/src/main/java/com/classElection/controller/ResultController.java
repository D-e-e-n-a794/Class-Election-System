package com.classElection.controller;

import com.classElection.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ResultController {

    @FXML
    private Label resultsLabel;

    @FXML
    private Button finishBtn;

    @FXML
    public void initialize() {
        // Dummy results for demo
        resultsLabel.setText(
                "Election Results:\n" +
                "Alice: 10 votes\n" +
                "Bob: 7 votes\n" +
                "Charlie: 3 votes\n"
        );
    }

    @FXML
    void handleFinish(ActionEvent event) {
        Stage stage = (Stage) finishBtn.getScene().getWindow();
//        SceneUtil.switchScene(stage, "/fxml/thank_you.fxml");
        SceneUtil.switchScene(stage, "/thank_you.fxml");

    }
}
