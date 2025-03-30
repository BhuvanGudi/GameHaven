package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.example.gamehaven.core.SceneManager;

public class LeaderboardController {
    @FXML private TableView<?> leaderboardTable;

    @FXML
    private void handleBack() {
        SceneManager.loadScene("lobby/main.fxml");
    }
}