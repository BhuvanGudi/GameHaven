package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import org.example.gamehaven.core.SceneManager;

public class SettingsController {
    @FXML
    private void handleBack() {
        SceneManager.loadScene("lobby/main.fxml");
    }
}