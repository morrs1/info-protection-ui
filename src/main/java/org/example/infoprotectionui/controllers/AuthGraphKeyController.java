package org.example.infoprotectionui.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.infoprotectionui.entities.GraphKeyUser;
import org.example.infoprotectionui.entities.GraphKeyUsers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AuthGraphKeyController implements Initializable {
    @FXML
    private Label labelForAnswer;

    @FXML
    private TextField textFieldForUsername;

    private ArrayList<GraphKeyUser> graphKeyUsers;


    private final ArrayList<ArrayList<Integer>> password = new ArrayList<>();

    private Integer amountOfTries = 0;

    private Boolean timeout = false;
    private Long startOfTimeout = 0L;

    @FXML
    private void onKeyClicked(ActionEvent event) {
        var coordinate = Arrays.stream(((RadioButton) event.getSource()).getId().replaceFirst("_", "").split("_")).map(Integer::parseInt).toList();
        System.out.println("Нажата кнопка с координатой:" + coordinate);
        labelForAnswer.setText("");
        if (password.isEmpty()) {
            password.add(new ArrayList<>(coordinate));
        } else if (password.getLast().equals(coordinate)) {
            password.removeLast();
        } else {
            int lastX = password.getLast().get(0);
            int lastY = password.getLast().get(1);
            int newX = coordinate.get(0);
            int newY = coordinate.get(1);
            int dx = Math.abs(lastX - newX);
            int dy = Math.abs(lastY - newY);
            if (dx <= 1 && dy <= 1) {
                password.add(new ArrayList<>(coordinate));
            } else {
                labelForAnswer.setText("Точки не рядом");
                ((AnchorPane) ((RadioButton) event.getSource()).getParent())
                        .getChildren()
                        .stream()
                        .filter(rb -> rb.getId().equals(String.format("_%d_%d", newX, newY)))
                        .findFirst()
                        .ifPresent(rb -> ((RadioButton) rb).setSelected(false));
            }
        }
        System.out.println(password);
    }


    @FXML
    private void onAuthButtonClicked(ActionEvent event) {
        if (timeout) {
            var diff = System.currentTimeMillis() - startOfTimeout;
            if (diff < 15_000) {
                labelForAnswer.setText("Вход заблокирован еще на " + (15_000 - diff) / 1000 + " секунд");
            } else {
                timeout = false;
                startOfTimeout = 0L;
            }
        } else {
            labelForAnswer.setText("");
            if (textFieldForUsername.getText().isEmpty()) {
                labelForAnswer.setText("Введите имя");
            } else if (graphKeyUsers.stream().noneMatch(user -> user.name().equals(textFieldForUsername.getText()))) {
                labelForAnswer.setText(labelForAnswer.getText() + " Имя пользователя не найдено");
            } else if (password.isEmpty()) {
                labelForAnswer.setText(labelForAnswer.getText() + " Пароль не должен быть пустым");
            } else {
                if (password.equals(graphKeyUsers
                        .stream().
                        filter(u -> u.name().equals(textFieldForUsername.getText()))
                        .findFirst()
                        .get()
                        .password())
                ) {
                    labelForAnswer.setText("Вход успешен");
                    amountOfTries = 0;
                } else {
                    amountOfTries++;
                    labelForAnswer.setText("Осталось попыток для входа: " + (3 - amountOfTries));
                    if (amountOfTries == 3) {
                        timeout = true;
                        startOfTimeout = System.currentTimeMillis();
                        amountOfTries = 0;
                    }
                }
            }
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            graphKeyUsers = new ObjectMapper().readValue(new File("src/main/java/org/example/infoprotectionui/JSON/graphKeyUsers.json"), GraphKeyUsers.class).users();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
