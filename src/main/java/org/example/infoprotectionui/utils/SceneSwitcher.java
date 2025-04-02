package org.example.infoprotectionui.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

public class SceneSwitcher {

    @Getter
    private static SceneSwitcher instance;
    private final Stage stage;

    public static void init(Stage stage) {
        instance = new SceneSwitcher(stage);
    }

    private SceneSwitcher(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(Scene scene) {
        stage.setScene(scene);
    }
}
