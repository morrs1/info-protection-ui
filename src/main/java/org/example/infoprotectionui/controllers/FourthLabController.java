package org.example.infoprotectionui.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.infoprotectionui.utils.SceneLoader;
import org.example.infoprotectionui.utils.SceneSwitcher;

public class FourthLabController {

    @FXML
    private Button buttonForAuth;

    @FXML
    private Button buttonForRegister;


    @FXML
    public void onButtonForAuthClicked(ActionEvent event) {
        SceneSwitcher.getInstance().switchScene(SceneLoader.getInstance().getScenes().get("auth-scene.fxml"));
    }

    @FXML
    public void onButtonForRegisterClicked(ActionEvent event) {
        SceneSwitcher.getInstance().switchScene(SceneLoader.getInstance().getScenes().get("register-scene.fxml"));
    }
}
