package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.utils.SoundManager;

public class SettingsController {
    @FXML
    private Slider volumeSlider; // Add this to your FXML

    @FXML
    public void initialize() {
        // Set initial volume value
        volumeSlider.setValue(SoundManager.getInstance().getVolume() * 100);

        // Add listener for volume changes
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100.0;
            SoundManager.getInstance().setVolume(volume);
        });
    }

    @FXML
    private void handleBack() {
        SceneManager.loadScene("lobby/main.fxml");
    }
}