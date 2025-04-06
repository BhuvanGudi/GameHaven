package org.example.gamehaven.ui.components;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatBox implements Initializable {
    @FXML private ListView<String> messageList;
    @FXML private TextField messageInput;
    @FXML private Button sendButton;

    private final ObservableList<String> messages = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageList.setItems(messages);
        sendButton.setOnAction(event -> sendMessage());
        messageInput.setOnAction(event -> sendMessage());
        messages.add("AI: Let's play! I'll go easy on you... maybe.");
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            messages.add("You: " + message);
            messageInput.clear();
            generateAIResponse(message);
        }
    }

    private void generateAIResponse(String playerMessage) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                String response;
                if (playerMessage.toLowerCase().contains("hi") || playerMessage.toLowerCase().contains("hello")) {
                    response = "AI: Hello human! Prepare to lose!";
                } else if (playerMessage.toLowerCase().contains("win")) {
                    response = "AI: Keep dreaming!";
                } else {
                    String[] taunts = {
                            "AI: My circuits are laughing at you",
                            "AI: *beep boop* Error: Skill not found",
                            "AI: This is too easy!"
                    };
                    response = taunts[(int)(Math.random() * taunts.length)];
                }
                Platform.runLater(() -> messages.add(response));
            } catch (InterruptedException ignored) {}
        }).start();
    }

    public void addGameMessage(String message) {
        Platform.runLater(() -> messages.add("Game: " + message));
    }
}