package org.example.infoprotectionui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.infoprotectionui.utils.SceneLoader;
import org.example.infoprotectionui.utils.SceneSwitcher;

public class GraphKeyMainController {
    @FXML
    private Button registerGraphKeyButton;

    @FXML
    private Button authGraphKeyButton;


    @FXML
    private void onRegisterGraphKeyButtonClicked(ActionEvent event) {
        SceneSwitcher.getInstance().switchScene(SceneLoader.getInstance().getScenes().get("register-graphKey-scene.fxml"));
    }

    @FXML
    private void onAuthGraphKeyButtonActionClicked(ActionEvent event) {
        SceneSwitcher.getInstance().switchScene(SceneLoader.getInstance().getScenes().get("auth-graphKey-scene.fxml"));
    }

}
