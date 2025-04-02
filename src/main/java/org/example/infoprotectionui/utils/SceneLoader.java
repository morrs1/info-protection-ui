package org.example.infoprotectionui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import lombok.Getter;
import org.example.infoprotectionui.HelloApplication;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneLoader {

    private static SceneLoader instance;
    @Getter
    private final Map<String, Scene> scenes;

    public static SceneLoader getInstance()  {
        if (instance == null) {
            try {
                instance = new SceneLoader();
            } catch (IOException e) {
                throw new RuntimeException("Что-то не так с загрузкой сцен");
            }
        }
        return instance;
    }

    private SceneLoader() throws IOException {
        scenes = new HashMap<>();
        File directory = new File("src/main/resources/org/example/infoprotectionui");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(file.getName()));
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        scenes.put(file.getName(), scene);
                    }
                }
            }
        } else {
            System.out.println("Указанный путь не существует или не является директорией.");
        }
    }
}
