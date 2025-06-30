package com.classElection.controller;

import com.classElection.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WelcomeController {
    @FXML
    private Button getStartedBtn;

    @FXML
    void handleGetStarted(ActionEvent event) {
        Stage stage = (Stage) getStartedBtn.getScene().getWindow();
//        SceneUtil.switchScene(stage, "/fxml/role_selection.fxml");
                SceneUtil.switchScene(stage, "/role_selection.fxml");

    }
}
