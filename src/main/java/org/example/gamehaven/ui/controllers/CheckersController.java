package org.example.gamehaven.ui.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.auth.UserSession;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.games.checkers.CheckersAI;
import org.example.gamehaven.games.checkers.CheckersGame;
import org.example.gamehaven.games.checkers.Piece;
import org.example.gamehaven.multiplayer.GameServer;
import org.example.gamehaven.core.GameMode;
import org.example.gamehaven.utils.SoundManager;

import java.util.Objects;

public class CheckersController {
    public Button restartButton;
    public Button quitButton;
    public Button rulesButton;
    @FXML private GridPane gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;
    @FXML private ImageView selectedPieceImage;
    @FXML private ImageView currentPlayerImage;

    private CheckersGame game;
    private Piece selectedPiece;
    private CheckersAI ai;
    private boolean vsAI;

    @FXML
    public void initialize() {
        Image whitePieceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/gamehaven/images/avatars/default_male.png")));
        Image blackPieceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/gamehaven/images/avatars/default_female.png")));

        selectedPieceImage.setImage(blackPieceImage);
        currentPlayerImage.setImage(whitePieceImage);
        boolean isMultiplayer = LobbyController.selectedGameMode == GameMode.MULTIPLAYER;
        game = new CheckersGame();

        if (isMultiplayer) {
            GameServer gameServer = new GameServer();
            playerLabel.setText("Waiting for opponent...");
            setupMultiplayer();
        } else {
            vsAI = true;
            ai = new CheckersAI();
            playerLabel.setText("Player vs AI");
        }

        TabPane tabPane = new TabPane();
        tabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double tabWidth = newVal.doubleValue() / tabPane.getTabs().size();
            for (Tab tab : tabPane.getTabs()) {
                tab.setStyle("-fx-pref-width: " + tabWidth + "px;");
            }
        });

        initializeBoard();
        updateCurrentPlayer();
    }

    private void initializeBoard() {
        gameBoard.getChildren().clear();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle cell = new Rectangle(70, 70);
                cell.getStyleClass().add("checkers-cell");
                cell.getStyleClass().add((row + col) % 2 == 0 ? "checkers-cell-light" : "checkers-cell-dark");

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

    private void updateUI() {
        if (game.isGameOver()) {
            Piece.PieceColor winner = game.getWinner();
            User currentUser = UserSession.getCurrentUser();
            if (winner != null) {
                statusLabel.setText(winner + " wins!");
                if (vsAI) {
                    if (winner == Piece.PieceColor.WHITE) {
                        currentUser.incrementWins();
                        currentUser.incrementCheckersWins();
                        SoundManager.getInstance().playWinSound();
                    } else {
                        currentUser.incrementLosses();
                        currentUser.incrementCheckersLosses();
                        SoundManager.getInstance().playLoseSound();
                    }
                }
            } else {
                statusLabel.setText("Game ended in draw!");
                currentUser.incrementCheckersDraws();
                SoundManager.getInstance().playDrawSound();
            }
        } else {
            updateCurrentPlayer();
        }
    }

    private void handleCellClick(int row, int col, MouseEvent event) {
        if (selectedPiece != null && game.isValidMove(selectedPiece, row, col)) {
            game.makeMove(selectedPiece, row, col);
            updateBoard();
            selectedPiece = null;
            updateUI();
        }
    }

    private void handleAIMove() {
        int[] move = ai.makeMove(game);
        if (move != null) {
            Piece piece = game.getPieceAt(move[0], move[1]);
            game.makeMove(piece, move[2], move[3]);
            updateBoard();
            updateUI();
        }
    }

    private void addPieceToBoard(int row, int col, Piece piece) {
        Circle pieceCircle = new Circle(30);
        pieceCircle.getStyleClass().add("checkers-piece");
        pieceCircle.getStyleClass().add(piece.getColor() == Piece.PieceColor.WHITE ?
                "checkers-piece-white" : "checkers-piece-black");

        if (piece.isKing()) {
            Circle kingIndicator = new Circle(12);
            kingIndicator.getStyleClass().add("checkers-king-indicator");

            StackPane pieceStack = new StackPane(pieceCircle, kingIndicator);
            pieceStack.setUserData(piece);
            pieceStack.setOnMouseClicked(this::handlePieceClick);

            GridPane.setHalignment(pieceStack, HPos.CENTER);
            GridPane.setValignment(pieceStack, VPos.CENTER);
            gameBoard.add(pieceStack, col, row);
        } else {
            pieceCircle.setUserData(piece);
            pieceCircle.setOnMouseClicked(this::handlePieceClick);

            GridPane.setHalignment(pieceCircle, HPos.CENTER);
            GridPane.setValignment(pieceCircle, VPos.CENTER);
            gameBoard.add(pieceCircle, col, row);
        }
    }

    private void handlePieceClick(MouseEvent event) {
        Node source = (Node) event.getSource();
        Piece clickedPiece = (Piece) source.getUserData();

        if (clickedPiece.getColor() == game.getCurrentPlayer()) {
            gameBoard.getChildren().forEach(node -> {
                if (node.getEffect() instanceof DropShadow effect) {
                    if (effect.getColor() == Color.YELLOW) {
                        // Reset to normal shadow
                        node.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.7)));
                    }
                }
            });

            selectedPiece = clickedPiece;
            source.setEffect(new DropShadow(15, Color.YELLOW));

            Image pieceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    clickedPiece.getColor() == Piece.PieceColor.WHITE ?
                            "/org/example/gamehaven/images/avatars/default_male.png" :
                            "/org/example/gamehaven/images/avatars/default_female.png" // or different image
            )));
            selectedPieceImage.setImage(pieceImage);
        }
    }

    private void updateBoard() {
        gameBoard.getChildren().removeIf(node ->
                (node instanceof Circle && node.getUserData() instanceof Piece) ||
                        (node instanceof StackPane && node.getUserData() instanceof Piece));

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
        String player = game.getCurrentPlayer() == Piece.PieceColor.WHITE ? "White" : "Black";
        statusLabel.setText("Current turn: " + player);  // fixed: Removed STR. template

        Image playerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                game.getCurrentPlayer() == Piece.PieceColor.WHITE ?
                        "/org/example/gamehaven/images/avatars/default_male.png" :
                        "/org/example/gamehaven/images/avatars/default_female.png" // or different image
        )));
        currentPlayerImage.setImage(playerImage);

        if (vsAI && game.getCurrentPlayer() == Piece.PieceColor.BLACK) {
            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(event -> handleAIMove());
            delay.play();
        }
    }

    private void setupMultiplayer() {

    }

    @FXML private void handleRestart() {
        SceneManager.loadScene("games/checkers_rules.fxml");
    }

    @FXML private void handleQuit() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    @FXML private void handleBackToLobby() {
        SceneManager.loadScene("lobby/main.fxml");
    }

    public void handleRules(ActionEvent actionEvent) {
        SceneManager.showRulesDialog("rules/checkers_rules.fxml");
    }
}