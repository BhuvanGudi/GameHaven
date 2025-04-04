package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.gamehaven.auth.UserSession;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.core.GameMode;
import org.example.gamehaven.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {
    @FXML private RadioButton singlePlayerRadio;
    @FXML private RadioButton multiplayerRadio;

    public static GameMode selectedGameMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup modeGroup = new ToggleGroup();
        singlePlayerRadio.setToggleGroup(modeGroup);
        multiplayerRadio.setToggleGroup(modeGroup);
        singlePlayerRadio.setSelected(true);
    }

    @FXML
    private void handleTicTacToe() {
        selectedGameMode = singlePlayerRadio.isSelected() ? GameMode.SINGLE_PLAYER : GameMode.MULTIPLAYER;
        SceneManager.loadScene("games/tictactoe.fxml");
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.setVolume(0.1);
    }

    @FXML
    private void handleConnectFour() {
        selectedGameMode = singlePlayerRadio.isSelected() ? GameMode.SINGLE_PLAYER : GameMode.MULTIPLAYER;
        SceneManager.loadScene("games/connectfour.fxml");
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.setVolume(0.1);
    }

    @FXML
    private void handleCheckers() {
        selectedGameMode = singlePlayerRadio.isSelected() ? GameMode.SINGLE_PLAYER : GameMode.MULTIPLAYER;
        SceneManager.loadScene("games/checkers.fxml");
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.setVolume(0.1);
    }

    @FXML
    private void handleProfile() {
        SceneManager.loadScene("profile/profile.fxml");
    }

    @FXML
    private void handleLeaderboard() {
        SceneManager.loadScene("sidebar/leaderboard.fxml");
    }

    @FXML
    private void handleSettings() {
        SceneManager.loadScene("sidebar/settings.fxml");
    }

    @FXML
    private void handleLogout() {
        UserSession.clearSession();
        SceneManager.loadScene("auth/login.fxml");
    }
}