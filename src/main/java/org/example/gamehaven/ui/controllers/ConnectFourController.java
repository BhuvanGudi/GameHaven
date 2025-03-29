package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.games.connect4.ConnectFourGame;
import org.example.gamehaven.multiplayer.GameServer;
import org.example.gamehaven.core.GameMode;

public class ConnectFourController {
    public Button col0;
    public Button col1;
    public Button col2;
    public Button col3;
    public Button col4;
    public Button col5;
    public Button col6;
    public Button col7;
    public Button col8;
    public Button restartButton;
    public Button quitButton;
    @FXML private GridPane gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;

    private ConnectFourGame game;
    private GameServer gameServer;
    private boolean isMultiplayer;

    @FXML
    public void initialize() {
        isMultiplayer = LobbyController.selectedGameMode == GameMode.MULTIPLAYER;
        game = new ConnectFourGame();

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
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Circle circle = new Circle(35);
                circle.setFill(Color.TRANSPARENT);
                circle.setStroke(Color.DARKGRAY);
                gameBoard.add(circle, col, row);
            }
        }
    }

    @FXML
    private void handleColumn0() { handleColumn(0); }
    @FXML
    private void handleColumn1() { handleColumn(1); }
    @FXML
    private void handleColumn2() { handleColumn(2); }
    @FXML
    private void handleColumn3() { handleColumn(3); }
    @FXML
    private void handleColumn4() { handleColumn(4); }
    @FXML
    private void handleColumn5() { handleColumn(5); }
    @FXML
    private void handleColumn6() { handleColumn(6); }
    @FXML
    private void handleColumn7() { handleColumn(7); }
    @FXML
    private void handleColumn8() { handleColumn(8); }

    private void handleColumn(int col) {
        if (game.isGameOver()) return;

        int row = game.makeMove(col);
        if (row == -1) return;

        Circle circle = (Circle) getNodeByRowColumnIndex(row, col);
        circle.setFill(game.getCurrentPlayer() == 'R' ? Color.RED : Color.YELLOW);

        if (isMultiplayer) {
            gameServer.makeMove(game.getGameId(), String.valueOf(col));
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
            statusLabel.setText(game.getCurrentPlayer() == 'R' ? "Red wins!" : "Yellow wins!");
        } else if (game.isBoardFull()) {
            statusLabel.setText("It's a draw!");
        } else {
            statusLabel.setText("Your turn: " + (game.getCurrentPlayer() == 'R' ? "Red" : "Yellow"));
        }
    }

    private void setupMultiplayer() {
        // Implement multiplayer logic using GameServer
    }

    @FXML
    private void handleRestart() {
        SceneManager.loadScene("games/connectfour.fxml");
    }

    @FXML
    private void handleQuit() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    @FXML
    private void handleBackToLobby() {
        SceneManager.loadScene("lobby/main.fxml");
    }

}