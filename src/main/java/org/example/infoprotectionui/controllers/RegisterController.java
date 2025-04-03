package org.example.infoprotectionui.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.infoprotectionui.entities.Phrases;
import org.example.infoprotectionui.entities.User;
import org.example.infoprotectionui.entities.Users;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RegisterController {
    private static final SecretKeySpec AES_KEY = new SecretKeySpec(
            "16-byte-secret-k".getBytes(StandardCharsets.UTF_8),
            "AES"
    );
    private final String PHRASES_FILE = "src/main/java/org/example/infoprotectionui/JSON/phrases.json";
    private final String USERS_FILE = "src/main/java/org/example/infoprotectionui/JSON/users.json";

    private long sumOfTime = 0;
    private long previousTime = 0;
    private ArrayList<Long> times = new ArrayList<>();
    private long sumOfTimeRej = 0;
    private int numOfPhrase;
    private int numOfTries = 0;
    private long sumOfIdealTimes = 0;
    private long sumOfRejTimes = 0;

    @FXML
    private TextField textFieldForName;

    @FXML
    private TextField textFieldForPhrase;

    @FXML
    private Label labelForPhrase;

    @FXML
    private Label labelForResponse;

    @FXML
    private Button buttonForRegister;

    @FXML
    public void initialize() throws Exception {
        var phrases = decryptPhrases();
        numOfPhrase = new Random().nextInt(phrases.phrases().size());
        var phrase = phrases.phrases().get(numOfPhrase);
        labelForPhrase.setText(labelForPhrase.getText() + phrase);
        textFieldForPhrase.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (previousTime != 0) {
                        var defTime = System.currentTimeMillis() - previousTime;
                        sumOfTime += defTime;
                        times.add(defTime);
                        System.out.println(defTime);
                    }
                    previousTime = System.currentTimeMillis();

                }
        );
    }

    @FXML
    public void onButtonForRegisterClicked(ActionEvent event) throws Exception {
        var phrases = decryptPhrases();
        if (!phrases.phrases().get(numOfPhrase).equals(textFieldForPhrase.getText())) {
            labelForResponse.setText("Фраза введена неверно");
            sumOfTime = 0;
            sumOfTimeRej = 0;
            times = new ArrayList<>();
            textFieldForPhrase.setText("");
        } else {
            var idealTime = (long) sumOfTime / 24;
            times.forEach(time -> sumOfTimeRej += Math.abs(time - idealTime));
            var rejTime = (long) sumOfTimeRej / 24;
            System.out.println(idealTime + " " + rejTime);
            if (numOfTries == 3) {
                sumOfIdealTimes += (int) idealTime;
                sumOfRejTimes += (int) rejTime;
                User newUser = new User(textFieldForName.getText(), numOfPhrase, (int) sumOfIdealTimes / 4, (int) sumOfRejTimes / 4);
                var users = new ObjectMapper().readValue(new File(USERS_FILE), Users.class);
                users.users().add(newUser);
                new ObjectMapper().writeValue(new File(USERS_FILE), users);
                labelForResponse.setText("Регистрация успешна");
            } else {
                sumOfIdealTimes += (int) idealTime;
                sumOfRejTimes += (int) rejTime;
                labelForResponse.setText("Введите фразу еще " + (3 - numOfTries) + " раз");
                numOfTries++;
                sumOfTime = 0;
                sumOfTimeRej = 0;
                times = new ArrayList<>();
                textFieldForPhrase.setText("");
            }
        }
    }

    private Phrases decryptPhrases() throws Exception {
        Phrases encryptedPhrases = new ObjectMapper().readValue(new File(PHRASES_FILE), Phrases.class);

        List<String> decryptedPhrases = encryptedPhrases.phrases().stream()
                .map(this::decrypt)
                .collect(Collectors.toList());

        return new Phrases((ArrayList<String>) decryptedPhrases);
    }

    private String decrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, AES_KEY);
            byte[] decoded = Base64.getDecoder().decode(input);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }

}
