package org.example.gamehaven.multiplayer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.example.gamehaven.auth.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GameServer {
    private DatabaseReference database;
    private DatabaseReference currentGameRef;
    private String gameId;
    private String playerId;
    private MoveListener moveListener;

    public GameServer() {
        initializeFirebase();
        this.playerId = UserSession.getCurrentUser().getId();
    }

    private void initializeFirebase() {
        try (InputStream serviceAccount = getClass().getResourceAsStream("/firebase-config.json")) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://gamehaven-default-rtdb.firebaseio.com/")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            database = FirebaseDatabase.getInstance().getReference();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    public String createGame(String gameType) {
        gameId = database.child("games").push().getKey();

        Map<String, Object> gameData = new HashMap<>();
        gameData.put("player1", playerId);
        gameData.put("gameType", gameType);
        gameData.put("status", "waiting");
        gameData.put("currentPlayer", playerId);

        database.child("games").child(gameId).setValueAsync(gameData);
        setupGameListeners();

        return gameId;
    }

    public void joinGame(String gameId) {
        this.gameId = gameId;
        database.child("games").child(gameId).child("player2").setValueAsync(playerId);
        database.child("games").child(gameId).child("status").setValueAsync("active");
        setupGameListeners();
    }

    public void makeMove(String gameId, int column) {
        if (this.gameId == null || !this.gameId.equals(gameId)) return;

        Map<String, Object> move = new HashMap<>();
        move.put("playerId", playerId);
        move.put("column", column);
        move.put("timestamp", ServerValue.TIMESTAMP);

        database.child("games").child(this.gameId).child("moves").push().setValueAsync(move);

        // Update current player
        database.child("games").child(this.gameId).child("currentPlayer")
                .setValueAsync(playerId.equals(getOpponentId()) ? playerId : getOpponentId());
    }

    public void setOnOpponentMoveListener(MoveListener listener) {
        this.moveListener = listener;
        listenForMoves();
    }

    private void listenForMoves() {
        if (gameId == null || moveListener == null) return;

        database.child("games").child(gameId).child("moves")
                .orderByChild("timestamp")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String prevChildName) {
                        try {
                            Map<String, Object> move = (Map<String, Object>) snapshot.getValue();
                            if (move != null && !playerId.equals(move.get("playerId"))) {
                                long columnLong = (Long) move.get("column");
                                moveListener.onMoveReceived((int) columnLong);
                            }
                        } catch (Exception e) {
                            System.err.println("Error processing move: " + e.getMessage());
                        }
                    }

                    @Override public void onChildChanged(DataSnapshot snapshot, String prevChildName) {}
                    @Override public void onChildRemoved(DataSnapshot snapshot) {}
                    @Override public void onChildMoved(DataSnapshot snapshot, String prevChildName) {}
                    @Override public void onCancelled(DatabaseError error) {
                        System.err.println("Firebase error: " + error.getMessage());
                    }
                });
    }

    private String getOpponentId() {
        // This would need proper implementation based on your game state
        return ""; // Placeholder
    }

    private void setupGameListeners() {

    }

    public void makeMove(Object gameId, String s) {
    }

    public interface MoveListener {
        void onMoveReceived(int column);
    }
}