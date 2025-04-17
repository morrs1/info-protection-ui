package org.example.infoprotectionui.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.infoprotectionui.entities.GraphKeyUser;
import org.example.infoprotectionui.entities.GraphKeyUsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RegisterGraphKeyController {


    @FXML
    private Label labelForAnswer;

    @FXML
    private TextField textFieldForUsername;

    private final ArrayList<ArrayList<Integer>> password = new ArrayList<>();
    private final String URL_FILE_USERS = "src/main/java/org/example/infoprotectionui/JSON/graphKeyUsers.json";

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
    private void onRegButtonClicked(ActionEvent event) throws IOException {
        if (!textFieldForUsername.getText().isEmpty() && !password.isEmpty()) {
            var mapper = new ObjectMapper();
            var newUsers = new GraphKeyUser(textFieldForUsername.getText(), password);
            var users = mapper.readValue(new File(URL_FILE_USERS), GraphKeyUsers.class);
            users.users().add(newUsers);
            mapper.writeValue(new File(URL_FILE_USERS), users);
            System.out.println("Успешная регистрация");
        }
    }
}
