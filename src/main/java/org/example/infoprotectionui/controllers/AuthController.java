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
import java.util.stream.Collectors;

public class AuthController {

    private final String USERS_FILE = "src/main/java/org/example/infoprotectionui/JSON/users.json";
    private final String PHRASES_FILE = "src/main/java/org/example/infoprotectionui/JSON/phrases.json";
    private final long EPS = 200;

    private long sumOfTime = 0;
    private long previousTime = 0;
    private ArrayList<Long> times = new ArrayList<>();
    private long sumOfTimeRej = 0;
    private User currentUser;
    private final SecretKeySpec AES_KEY = new SecretKeySpec(
            "16-byte-secret-k".getBytes(StandardCharsets.UTF_8),
            "AES"
    );
    @FXML
    private TextField textFieldForAuth;

    @FXML
    private Label labelForAuth;

    @FXML
    private Button buttonForAuth;

    @FXML
    private TextField textFieldForPhrase;

    @FXML
    private Button buttonForAuth2;

    @FXML
    public void initialize() throws Exception {
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

    public void onButtonForAuthClicked(ActionEvent event) throws Exception {
        var users = new ObjectMapper().readValue(new File(USERS_FILE), Users.class);
        var name = textFieldForAuth.getText();
        if (users.users().stream().noneMatch(user -> user.name().equals(name))) {
            labelForAuth.setText("Пользователь не найден");
        } else {
            currentUser = null;
            for (var user : users.users()) {
                if (user.name().equals(name)) {
                    currentUser = user;
                }
            }
            labelForAuth.setText("Пользователь найден");
            System.out.println(currentUser);
            textFieldForPhrase.setStyle("visibility: true");
            buttonForAuth2.setStyle("visibility: true");
            var phrase = decryptPhrases().phrases().get(currentUser.phraseId());
            labelForAuth.setText("Введите фразу: " + phrase);

        }
    }

    @FXML
    private void auth(ActionEvent event) throws Exception {
        var phrase = decryptPhrases().phrases().get(currentUser.phraseId());
        if (!phrase.equals(textFieldForPhrase.getText())) {
            labelForAuth.setText("Фраза введена неверно: " + phrase);
            previousTime = 0;
            sumOfTime = 0;
            sumOfTimeRej = 0;
            times = new ArrayList<>();
            textFieldForPhrase.setText("");
        } else {
            var idealTime = (long) sumOfTime / 24;
            times.forEach(time -> sumOfTimeRej += Math.abs(time - idealTime));
            var rejTime = (long) sumOfTimeRej / 24;
            System.out.println(idealTime + " " + rejTime);
            System.out.println(currentUser.idealTime() + " " + currentUser.rejTime());
            if (Math.abs(idealTime - currentUser.idealTime()) < EPS && Math.abs(rejTime - currentUser.rejTime()) < EPS) {
                labelForAuth.setText("Вход прошел успешно");
            } else {
                sumOfTime = 0;
                previousTime = 0;
                sumOfTimeRej = 0;
                times = new ArrayList<>();
                textFieldForPhrase.setText("");
                labelForAuth.setText("Вход не удался " + phrase);
            }
        }
    }

    private String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, AES_KEY);
            byte[] encrypted = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    private void encryptPhrases() throws Exception {
        Phrases phrases = new ObjectMapper().readValue(new File(PHRASES_FILE), Phrases.class);

        List<String> encryptedPhrases = phrases.phrases().stream()
                .map(this::encrypt)
                .collect(Collectors.toList());

        new ObjectMapper().writeValue(new File(PHRASES_FILE), new Phrases((ArrayList<String>) encryptedPhrases));
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
