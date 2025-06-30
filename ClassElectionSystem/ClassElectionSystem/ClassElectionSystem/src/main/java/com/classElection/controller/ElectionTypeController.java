package com.classElection.controller;

import com.classElection.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ElectionTypeController {

    @FXML
    private Button classRepBtn;

    @FXML
    private Button presidentBtn;

    @FXML
    private Button backBtn;

    @FXML
    void handleClassRep(ActionEvent event) {
        Stage stage = (Stage) classRepBtn.getScene().getWindow();
        SceneUtil.switchScene(stage, "/class_rep_vote.fxml");
    }

    @FXML
    void handlePresident(ActionEvent event) {
        Stage stage = (Stage) presidentBtn.getScene().getWindow();
        SceneUtil.switchScene(stage, "/vote_president.fxml");
    }

    @FXML
    void handleBack(ActionEvent event) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        SceneUtil.switchScene(stage, "/role_selection.fxml");  // Change path if your welcome page is elsewhere
    }
}

