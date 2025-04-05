package org.example.gamehaven.ui.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.auth.UserSession;
import org.example.gamehaven.core.GameMode;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.games.connect4.ConnectFourGame;
import org.example.gamehaven.games.connect4.ConnectFourAI;
import org.example.gamehaven.utils.SoundManager;

public class ConnectFourController {
    public Button rulesButton;
    @FXML private GridPane gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;
    @FXML private Button restartButton;
    @FXML private Button quitButton;
    @FXML private Button col0, col1, col2, col3, col4, col5, col6;

    private ConnectFourGame game;
    private ConnectFourAI ai;
    private boolean vsAI;
    private SoundManager soundManager;

    @FXML
    public void initialize() {
        game = new ConnectFourGame();
        ai = new ConnectFourAI();
        vsAI = LobbyController.selectedGameMode == GameMode.SINGLE_PLAYER;
        initializeBoard();
        updateUI();
    }

    private void initializeBoard() {
        gameBoard.getChildren().clear();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Circle circle = new Circle(25);
                circle.setFill(Color.TRANSPARENT);
                circle.setStroke(Color.BLACK);
                circle.getStyleClass().add("connect-four-cell");
                gameBoard.add(circle, col, row);
            }
        }
    }

    private void updateUI() {
        char[][] board = game.getBoard();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Circle circle = (Circle) gameBoard.getChildren().get(row * 7 + col);
                circle.getStyleClass().removeAll("connect-four-piece-red", "connect-four-piece-yellow", "winning-cell");
                if (board[row][col] == 'R') {
                    circle.getStyleClass().add("connect-four-piece-red");
                    circle.setFill(Color.RED);
                } else if (board[row][col] == 'Y') {
                    circle.getStyleClass().add("connect-four-piece-yellow");
                    circle.setFill(Color.YELLOW);
                }
            }
        }

        if (game.checkWin()) {
            statusLabel.setText("Player " + (game.getCurrentPlayer() == 'R' ? "Red" : "Yellow") + " wins!");
            User currentUser = UserSession.getCurrentUser();

            if (vsAI && game.getCurrentPlayer() == 'Y') {
                currentUser.incrementLosses();
                currentUser.incrementC4Losses();
                SoundManager.getInstance().playLoseSound(); // Will handle volume automatically
            } else {
                currentUser.incrementWins();
                currentUser.incrementC4Wins();
                SoundManager.getInstance().playWinSound();
            }
        } else if (game.isBoardFull()) {
            statusLabel.setText("Game ended in a draw!");
            User currentUser = UserSession.getCurrentUser();
            currentUser.incrementC4Draws();
            SoundManager.getInstance().playDrawSound();
        } else {
            statusLabel.setText("Your turn: " + (game.getCurrentPlayer() == 'R' ? "Red" : "Yellow"));
            playerLabel.setText(vsAI ? "Player vs AI" : "Player 1 vs Player 2");
            if (vsAI && game.getCurrentPlayer() == 'Y') {
                makeAIMove();
            }
        }
    }

    private void makeAIMove() {
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> {
            int col = ai.makeMove(game);
            if (col != -1) {
                game.makeMove(col);
                updateUI();
            }
        });
        delay.play();
    }

    @FXML
    private void handleColumn(int col) {
        if (!game.isGameOver()) {
            game.makeMove(col);
            updateUI();
        }
    }

    @FXML private void handleColumn0() { handleColumn(0); }
    @FXML private void handleColumn1() { handleColumn(1); }
    @FXML private void handleColumn2() { handleColumn(2); }
    @FXML private void handleColumn3() { handleColumn(3); }
    @FXML private void handleColumn4() { handleColumn(4); }
    @FXML private void handleColumn5() { handleColumn(5); }
    @FXML private void handleColumn6() { handleColumn(6); }

    @FXML
    private void handleRestart() {
        game = new ConnectFourGame();
        initializeBoard();
        updateUI();
    }

    @FXML
    private void handleQuit() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    @FXML
    private void handleBackToLobby() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    public void handleRules(ActionEvent actionEvent) {
        SceneManager.showRulesDialog("rules/connectfour_rules.fxml");
    }
}