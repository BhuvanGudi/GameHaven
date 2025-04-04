package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.utils.SoundManager;

public class SettingsController {
    @FXML
    private Slider volumeSlider;

    @FXML
    public void initialize() {
        volumeSlider.setValue(SoundManager.getInstance().getVolume() * 100);

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