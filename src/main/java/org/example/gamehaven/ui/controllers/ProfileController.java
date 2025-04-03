package org.example.gamehaven.ui.controllers;

import com.google.firebase.database.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.gamehaven.auth.UserSession;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.auth.AuthService;

public class ProfileController {
    public TabPane gameStatsTabPane;
    @FXML private Label usernameLabel;
    @FXML private Label gamesPlayedLabel;
    @FXML private Label winsLabel;
    @FXML private Label lossesLabel;
    @FXML private Label winRateLabel;
    @FXML private ImageView avatarImage;
    @FXML private Label errorLabel;

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
    private final DatabaseReference database = FirebaseDatabase.getInstance().getReference(); // Added database reference

    @FXML
    public void initialize() {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser != null) {
            loadUserData(currentUser);
        }
        updateTabWidths();
        gameStatsTabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateTabWidths();
        });
        setupRealTimeUpdates();
    }

    private void updateTabWidths() {
        double tabWidth = gameStatsTabPane.getWidth() / gameStatsTabPane.getTabs().size();
        for (Tab tab : gameStatsTabPane.getTabs()) {
            double finalWidth = Math.max(tabWidth, 100);
            tab.setStyle("-fx-pref-width: " + finalWidth + "px;");
        }
    }

    private void loadUserData(User user) {
        usernameLabel.setText("Username: " + user.getUsername());
        gamesPlayedLabel.setText("Games Played: " + user.getGamesPlayed());
        winsLabel.setText("Wins: " + user.getWins());
        lossesLabel.setText("Losses: " + user.getLosses());
        winRateLabel.setText("Win Rate: " + calculateWinRate(user) + "%");

        avatarImage.setImage(new Image(String.valueOf(getClass().getResource("/org/example/gamehaven/images/avatars/default_male.png"))));

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

    private void setupRealTimeUpdates() {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser != null) {
            database.child("users").child(currentUser.getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User updatedUser = snapshot.getValue(User.class);
                            Platform.runLater(() -> loadUserData(updatedUser));
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            Platform.runLater(() -> errorLabel.setText("Database error: " + error.getMessage()));
                        }
                    });
        }
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