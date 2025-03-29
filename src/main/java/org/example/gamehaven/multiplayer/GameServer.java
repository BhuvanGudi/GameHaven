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

    public GameServer() {
        initializeFirebase();
        this.playerId = UserSession.getCurrentUser().getId();
    }

    private void initializeFirebase() {
        try {
            InputStream serviceAccount = getClass().getResourceAsStream("/firebase-config.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://gamehaven-default-rtdb.firebaseio.com/")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            database = FirebaseDatabase.getInstance().getReference();
        } catch (IOException e) {
            e.printStackTrace();
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

    public void makeMove(Object gameId, String moveData) {
        if (this.gameId != null) {
            database.child("games").child(this.gameId).child("moves").push().setValueAsync(moveData);
        }
    }

    public void listenForMoves(MoveListener moveListener) {
        if (gameId != null) {
            database.child("games").child(gameId).child("moves").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                    moveListener.onMoveReceived(snapshot.getValue(String.class));
                }
                // Other required methods with empty implementations
                public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
                public void onChildRemoved(DataSnapshot snapshot) {}
                public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
                public void onCancelled(DatabaseError error) {}
            });
        }
    }

    private void setupGameListeners() {
        currentGameRef = database.child("games").child(gameId);
        currentGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String status = snapshot.child("status").getValue(String.class);
                String currentPlayer = snapshot.child("currentPlayer").getValue(String.class);

                // Handle game state changes
                if ("ended".equals(status)) {
                    // Handle game end
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Firebase error: " + error.getMessage());
            }
        });
    }

    public interface MoveListener {
        void onMoveReceived(String moveData);
    }
}