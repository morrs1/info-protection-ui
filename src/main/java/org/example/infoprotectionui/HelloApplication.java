package org.example.infoprotectionui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.infoprotectionui.utils.SceneLoader;
import org.example.infoprotectionui.utils.SceneSwitcher;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        SceneSwitcher.init(stage);
        SceneLoader.getInstance();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}