package org.example.gamehaven.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.games.checkers.CheckersGame;
import org.example.gamehaven.games.checkers.Piece;
import org.example.gamehaven.multiplayer.GameServer;
import org.example.gamehaven.core.GameMode;

public class CheckersController {
    @FXML private GridPane gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;
    @FXML private Circle selectedPieceImage;
    @FXML private Circle currentPlayerImage;

    private CheckersGame game;
    private GameServer gameServer;
    private boolean isMultiplayer;
    private Piece selectedPiece;

    @FXML
    public void initialize() {
        isMultiplayer = LobbyController.selectedGameMode == GameMode.MULTIPLAYER;
        game = new CheckersGame();

        if (isMultiplayer) {
            gameServer = new GameServer();
            playerLabel.setText("Waiting for opponent...");
            setupMultiplayer();
        } else {
            playerLabel.setText("Single Player");
        }

        initializeBoard();
        updateCurrentPlayer();
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Circle cell = new Circle(35);
                cell.setFill((row + col) % 2 == 0 ? Color.valueOf("#4d4d4d") : Color.valueOf("#a0a0a0"));
                int finalRow = row;
                int finalCol = col;
                cell.setOnMouseClicked(e -> handleCellClick(finalRow, finalCol, e));
                gameBoard.add(cell, col, row);

                Piece piece = game.getPieceAt(row, col);
                if (piece != null) {
                    addPieceToBoard(row, col, piece);
                }
            }
        }
    }

    private void addPieceToBoard(int row, int col, Piece piece) {
        Circle pieceCircle = new Circle(30);
        pieceCircle.setFill(piece.getColor().getFxColor());
        pieceCircle.setStroke(Color.DARKGRAY);
        pieceCircle.setUserData(piece);
        pieceCircle.setOnMouseClicked(this::handlePieceClick);
        gameBoard.add(pieceCircle, col, row);
    }

    private void handleCellClick(int row, int col, MouseEvent event) {
        if (selectedPiece != null && game.isValidMove(selectedPiece, row, col)) {
            game.makeMove(selectedPiece, row, col);
            updateBoard();

            if (isMultiplayer) {
                gameServer.makeMove(game.getGameId(),
                        selectedPiece.getRow() + "," + selectedPiece.getCol() + "," + row + "," + col);
            }

            selectedPiece = null;
            selectedPieceImage.setFill(Color.TRANSPARENT);
            updateCurrentPlayer();
        }
    }

    private void handlePieceClick(MouseEvent event) {
        Piece clickedPiece = (Piece) ((Node) event.getSource()).getUserData();
        if (clickedPiece.getColor() == game.getCurrentPlayer()) {
            selectedPiece = clickedPiece;
            selectedPieceImage.setFill(selectedPiece.getColor().getFxColor());
        }
    }

    private void updateBoard() {
        gameBoard.getChildren().removeIf(node -> node instanceof Circle && node.getUserData() instanceof Piece);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = game.getPieceAt(row, col);
                if (piece != null) {
                    addPieceToBoard(row, col, piece);
                }
            }
        }
    }

    private void updateCurrentPlayer() {
        String player = game.getCurrentPlayer() == Piece.PieceColor.RED ? "Red" : "Black";
        statusLabel.setText("Current turn: " + player);  // fixed: Removed STR. template
        currentPlayerImage.setFill(game.getCurrentPlayer().getFxColor());
    }

    private void setupMultiplayer() {
        // Multiplayer implementation would go here
    }

    @FXML private void handleRestart() {
        SceneManager.loadScene("games/checkers.fxml");
    }

    @FXML private void handleQuit() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    @FXML private void handleBackToLobby() {
        SceneManager.loadScene("lobby/main.fxml");
    }
}