// ConnectFourController.java
package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.games.connect4.ConnectFourGame;

public class ConnectFourController {
    @FXML private GridPane gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;
    @FXML private Button restartButton;
    @FXML private Button quitButton;
    @FXML private Button col0, col1, col2, col3, col4, col5, col6, col7, col8;

    private ConnectFourGame game;

    @FXML
    public void initialize() {
        game = new ConnectFourGame();
        initializeBoard();
        updateUI();
    }

    private void initializeBoard() {
        gameBoard.getChildren().clear();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Circle circle = new Circle(30);
                circle.setFill(Color.TRANSPARENT);
                circle.setStroke(Color.BLACK);
                gameBoard.add(circle, col, row);
            }
        }
    }

    private void updateUI() {
        char[][] board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Circle circle = (Circle) gameBoard.getChildren().get(row * 8 + col);
                if (board[row][col] == 'R') {
                    circle.setFill(Color.RED);
                } else if (board[row][col] == 'Y') {
                    circle.setFill(Color.YELLOW);
                } else {
                    circle.setFill(Color.TRANSPARENT);
                }
            }
        }

        if (game.checkWin()) {
            statusLabel.setText("Player " + (game.getCurrentPlayer() == 'R' ? "Yellow" : "Red") + " wins!");
        } else if (game.isBoardFull()) {
            statusLabel.setText("Game ended in a draw!");
        } else {
            statusLabel.setText("Your turn: " + (game.getCurrentPlayer() == 'R' ? "Red" : "Yellow"));
        }
    }

    private void handleColumn(int col) {
        int row = game.makeMove(col);
        if (row != -1) {
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
    @FXML private void handleColumn7() { handleColumn(7); }

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
}