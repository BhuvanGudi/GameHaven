package org.example.gamehaven.ui.controllers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.auth.UserSession;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.games.tictactoe.TicTacToeGame;
import org.example.gamehaven.games.tictactoe.TicTacToeAI;
import org.example.gamehaven.multiplayer.GameServer;
import org.example.gamehaven.core.GameMode;
import org.example.gamehaven.ui.components.ChatBox;
import org.example.gamehaven.utils.SoundManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TicTacToeController {
    public Button restartButton;
    public Button quitButton;
    private static final Logger logger = LoggerFactory.getLogger(TicTacToeController.class);
    public Button rulesButton;
    public StackPane chatContent;
    @FXML private GridPane gameBoard;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;
    @FXML private VBox chatContainer;

    private TicTacToeGame game;
    private TicTacToeAI ai;
    private GameServer gameServer;
    private boolean isMultiplayer;
    private boolean isPlayerTurn;
    private ChatBox chatBoxController;
    private boolean isChatVisible = false;

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
        initializeChatBox();
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

        if (!isMultiplayer && !game.isGameOver()) {
            isPlayerTurn = false;
            makeAIMove();
        }
    }

    private void makeAIMove() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                int[] move = ai.makeMove(game.getBoard());
                if (move != null) {
                    Platform.runLater(() -> {
                        Button button = (Button) getNodeByRowColumnIndex(move[0], move[1]);
                        assert button != null;
                        button.setText(String.valueOf(game.getCurrentPlayer()));
                        game.makeMove(move[0], move[1]);
                        if (Math.random() < 0.3) {
                            String[] moveTaunts = {"*yawns*", "Predictable...", "🤖 *activates doom protocol*"};
                            chatBoxController.addGameMessage("AI: " + moveTaunts[(int) (Math.random() * moveTaunts.length)]);
                        }
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
            User currentUser = UserSession.getCurrentUser();
            if (currentUser != null) {
                if (game.getCurrentPlayer() == 'X') { // Assuming X is always the player
                    currentUser.incrementWins();
                    currentUser.incrementTttWins();
                    SoundManager.getInstance().playWinSound();
                } else {
                    currentUser.incrementLosses();
                    currentUser.incrementTttLosses();
                    SoundManager.getInstance().playLoseSound();
                }
                updateFirebaseStats(currentUser);
            }
        } else if (game.isBoardFull()) {
            statusLabel.setText("It's a draw!");
            User currentUser = UserSession.getCurrentUser();
            if (currentUser != null) {
                currentUser.incrementTttDraws();
                updateFirebaseStats(currentUser);
                SoundManager.getInstance().playDrawSound();
            }
        } else {
            statusLabel.setText("Current turn: " + game.getCurrentPlayer());
            if (!isMultiplayer) {
                playerLabel.setText(isPlayerTurn ? "Your turn" : "Computer thinking...");
            }
        }
    }

    private void updateFirebaseStats(User user) {
        try {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(user.getId());

            Map<String, Object> updates = new HashMap<>();
            updates.put("wins", user.getWins());
            updates.put("losses", user.getLosses());
            updates.put("gamesPlayed", user.getGamesPlayed());
            updates.put("tttWins", user.getTttWins());
            updates.put("tttLosses", user.getTttLosses());
            updates.put("tttDraws", user.getTttDraws());

            userRef.updateChildren(updates, (error, ref) -> {
                if (error != null) {
                    logger.error("Failed to update user stats: {}", error.getMessage());
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Update Error");
                        alert.setHeaderText("Failed to save game results");
                        alert.setContentText("Your stats couldn't be saved to the cloud. Playing offline.");
                        alert.show();
                    });
                } else {
                    logger.info("User stats updated successfully");
                }
            });
        } catch (Exception e) {
            logger.error("Unexpected error updating stats: {}", e.getMessage());
        }
    }

    private void setupMultiplayer() {}

    @FXML
    private void toggleChat() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), chatContainer);
        transition.setToY(isChatVisible ? 0 : -300); // Slides up/down
        transition.play();
        isChatVisible = !isChatVisible;
    }

    private void initializeChatBox() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gamehaven/fxml/chatbox/chatbox.fxml"));
            Parent chatBox = loader.load();
            chatBoxController = loader.getController();
            chatContent.getChildren().add(chatBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
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