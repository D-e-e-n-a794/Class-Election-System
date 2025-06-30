package com.classElection.controller;

import com.classElection.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RoleSelectionController {

    @FXML
    private Button adminBtn;

    @FXML
    private Button voterBtn;

    @FXML
    private Button backBtn;

    @FXML
    void handleAdmin(ActionEvent event) {
        Stage stage = (Stage) adminBtn.getScene().getWindow();
        SceneUtil.switchScene(stage, "/admin_login.fxml");
    }

    @FXML
    void handleVoter(ActionEvent event) {
        Stage stage = (Stage) voterBtn.getScene().getWindow();
        SceneUtil.switchScene(stage, "/election_type.fxml");
    }

    @FXML
    void handleBack(ActionEvent event) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        SceneUtil.switchScene(stage, "/welcome.fxml"); // Adjust this path to your actual previous scene
    }
}

