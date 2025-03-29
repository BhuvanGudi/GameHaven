package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.games.tictactoe.TicTacToeGame;
import org.example.gamehaven.multiplayer.GameServer;
import org.example.gamehaven.core.GameMode;

import java.util.Arrays;

public class TicTacToeController {
    public Button restartButton;
    public Button quitButton;
    @FXML private GridPane gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;

    private TicTacToeGame game;
    private GameServer gameServer;
    private boolean isMultiplayer;

    @FXML
    public void initialize() {
        isMultiplayer = LobbyController.selectedGameMode == GameMode.MULTIPLAYER;
        game = new TicTacToeGame();

        if (isMultiplayer) {
            gameServer = new GameServer();
            playerLabel.setText("Waiting for opponent...");
            setupMultiplayer();
        } else {
            playerLabel.setText("Single Player");
        }

        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button();
                button.setPrefSize(100, 100);
                button.setStyle("-fx-font-size: 24px;");
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(e -> handleMove(finalRow, finalCol));
                gameBoard.add(button, col, row);
            }
        }
    }

    private void handleMove(int row, int col) {
        if (game.isGameOver()) return;

        Button button = (Button) getNodeByRowColumnIndex(row, col);
        if (!button.getText().isEmpty()) return;

        button.setText(String.valueOf(game.getCurrentPlayer()));
        game.makeMove(row, col);

        if (isMultiplayer) {
            gameServer.makeMove(game.getGameId(), row + "," + col);
        }

        updateGameStatus();
    }

    private Node getNodeByRowColumnIndex(int row, int col) {
        for (Node node : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    private void updateGameStatus() {
        if (game.checkWin()) {
            statusLabel.setText(Arrays.toString(game.getCurrentPlayer()) + " wins!");
        } else if (game.isBoardFull()) {
            statusLabel.setText("It's a draw!");
        } else {
            statusLabel.setText("Your turn: " + Arrays.toString(game.getCurrentPlayer()));
        }
    }

    private void setupMultiplayer() {
        // Implement multiplayer logic using GameServer
    }

    @FXML
    private void handleRestart() {SceneManager.loadScene("games/tictactoe.fxml");}

    @FXML
    private void handleQuit() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    @FXML
    private void handleBackToLobby() {
        SceneManager.loadScene("lobby/main.fxml");
    }
}