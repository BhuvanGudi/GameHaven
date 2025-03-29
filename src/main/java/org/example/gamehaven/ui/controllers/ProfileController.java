package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.gamehaven.auth.UserSession;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.auth.AuthService;

public class ProfileController {
    @FXML private Label usernameLabel;
    @FXML private Label gamesPlayedLabel;
    @FXML private Label winsLabel;
    @FXML private Label lossesLabel;
    @FXML private Label winRateLabel;
    @FXML private ImageView avatarImage;

    @FXML private Label tttWinsLabel;
    @FXML private Label tttLossesLabel;
    @FXML private Label tttDrawsLabel;

    @FXML private Label c4WinsLabel;
    @FXML private Label c4LossesLabel;
    @FXML private Label c4DrawsLabel;

    @FXML private Label checkersWinsLabel;
    @FXML private Label checkersLossesLabel;
    @FXML private Label checkersDrawsLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser != null) {
            loadUserData(currentUser);
        }
    }

    private void loadUserData(User user) {
        usernameLabel.setText("Username: " + user.getUsername());
        gamesPlayedLabel.setText("Games Played: " + user.getGamesPlayed());
        winsLabel.setText("Wins: " + user.getWins());
        lossesLabel.setText("Losses: " + user.getLosses());
        winRateLabel.setText("Win Rate: " + calculateWinRate(user) + "%");

        avatarImage.setImage(new Image(getClass().getResource("/images/avatars/default.png").toExternalForm()));

        // Game specific stats
        tttWinsLabel.setText("Tic Tac Toe Wins: " + user.getTttWins());
        tttLossesLabel.setText("Tic Tac Toe Losses: " + user.getTttLosses());
        tttDrawsLabel.setText("Tic Tac Toe Draws: " + user.getTttDraws());

        c4WinsLabel.setText("Connect Four Wins: " + user.getC4Wins());
        c4LossesLabel.setText("Connect Four Losses: " + user.getC4Losses());
        c4DrawsLabel.setText("Connect Four Draws: " + user.getC4Draws());

        checkersWinsLabel.setText("Checkers Wins: " + user.getCheckersWins());
        checkersLossesLabel.setText("Checkers Losses: " + user.getCheckersLosses());
        checkersDrawsLabel.setText("Checkers Draws: " + user.getCheckersDraws());
    }

    private double calculateWinRate(User user) {
        if (user.getGamesPlayed() == 0) return 0;
        return Math.round(((double) user.getWins() / user.getGamesPlayed()) * 100);
    }

    @FXML
    private void handleChangeAvatar() {
        // Implement avatar change functionality
    }

    @FXML
    private void handleBackToLobby() {
        SceneManager.loadScene("lobby/main.fxml");
    }
}