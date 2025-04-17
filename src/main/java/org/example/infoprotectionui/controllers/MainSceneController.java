package org.example.infoprotectionui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.infoprotectionui.utils.SceneLoader;
import org.example.infoprotectionui.utils.SceneSwitcher;

import java.io.IOException;

public class MainSceneController {

    @FXML
    private Button fifthLabButton;

    @FXML
    private Button buttonForGraphKeyScene;

    public MainSceneController() throws IOException {
    }


    @FXML
    public void onFifthLabButtonClicked(ActionEvent actionEvent) {
        SceneSwitcher.getInstance().switchScene(SceneLoader.getInstance().getScenes().get("fourth-lab.fxml"));
    }

    @FXML
    public void onButtonForGraphKeySceneClicked(ActionEvent actionEvent) {
        SceneSwitcher.getInstance().switchScene(SceneLoader.getInstance().getScenes().get("graph-key-scene.fxml"));
    }
}