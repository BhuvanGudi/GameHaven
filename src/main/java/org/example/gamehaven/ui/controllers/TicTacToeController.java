package org.example.gamehaven.ui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.games.tictactoe.TicTacToeGame;
import org.example.gamehaven.games.tictactoe.TicTacToeAI;
import org.example.gamehaven.multiplayer.GameServer;
import org.example.gamehaven.core.GameMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicTacToeController {
    public Button restartButton;
    public Button quitButton;
    private static final Logger logger = LoggerFactory.getLogger(TicTacToeController.class);
    public Button rulesButton;
    @FXML private GridPane gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;

    private TicTacToeGame game;
    private TicTacToeAI ai;
    private GameServer gameServer;
    private boolean isMultiplayer;
    private boolean isPlayerTurn;

    @FXML
    public void initialize() {
        isMultiplayer = LobbyController.selectedGameMode == GameMode.MULTIPLAYER;
        game = new TicTacToeGame();

        if (isMultiplayer) {
            gameServer = new GameServer();
            playerLabel.setText("Waiting for opponent...");
            setupMultiplayer();
        } else {
            ai = new TicTacToeAI();
            playerLabel.setText("Player vs Computer");
        }
        isPlayerTurn = true; // Assuming the first player starts

        initializeBoard();
        updateGameStatus();
    }

    private void initializeBoard() {
        gameBoard.getChildren().clear();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button();
                button.setPrefSize(100, 100);
                button.setStyle("-fx-font-size: 24px;");
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(_ -> handleMove(finalRow, finalCol));
                gameBoard.add(button, col, row);
            }
        }
    }

    private void handleMove(int row, int col) {
        if (game.isGameOver() || (!isMultiplayer && !isPlayerTurn)) return;

        Button button = (Button) getNodeByRowColumnIndex(row, col);
        assert button != null;
        if (!button.getText().isEmpty()) return;

        button.setText(String.valueOf(game.getCurrentPlayer()));
        game.makeMove(row, col);

        if (isMultiplayer) {
            gameServer.makeMove(game.getGameId(), row + "," + col);
        }

        updateGameStatus();

        // If a single player and game isn't over, let AI make a move
        if (!isMultiplayer && !game.isGameOver()) {
            isPlayerTurn = false;
            makeAIMove();
        }
    }

    private void makeAIMove() {
        new Thread(() -> {
            try {
                Thread.sleep(500); // Small delay for better UX
                int[] move = ai.makeMove(game.getBoard());
                if (move != null) {
                    Platform.runLater(() -> {
                        Button button = (Button) getNodeByRowColumnIndex(move[0], move[1]);
                        assert button != null;
                        button.setText(String.valueOf(game.getCurrentPlayer()));
                        game.makeMove(move[0], move[1]);
                        updateGameStatus();
                        isPlayerTurn = true;
                    });
                }
            } catch (InterruptedException e) {
                logger.error("AI move thread was interrupted", e);
                Thread.currentThread().interrupt(); // Restore the interrupted status
            } catch (Exception e) {
                logger.error("Error during AI move execution", e);
            }
        }).start();
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
            statusLabel.setText(game.getCurrentPlayer() + " wins!");
        } else if (game.isBoardFull()) {
            statusLabel.setText("It's a draw!");
        } else {
            statusLabel.setText("Current turn: " + game.getCurrentPlayer());
            if (!isMultiplayer) {
                playerLabel.setText(isPlayerTurn ? "Your turn" : "Computer thinking...");
            }
        }
    }

    private void setupMultiplayer() {
        // Implement multiplayer logic using GameServer
        // This would involve setting up listeners for opponent's moves
        // and updating the UI accordingly
    }

    @FXML
    private void handleRestart() {
        SceneManager.loadScene("games/tictactoe.fxml");
    }

    @FXML
    private void handleQuit() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    @FXML
    private void handleBackToLobby() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    public void handleRules(ActionEvent actionEvent) {SceneManager.showRulesDialog("rules/tictactoe_rules.fxml");}
}