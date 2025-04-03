package org.example.gamehaven.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class AuthService {
    private final FirebaseAuth auth;
    private final DatabaseReference databaseRef;

    public interface AuthCallback {
        void onSuccess(User user);
        void onSuccess();
        void onError(String errorMessage);
    }

    public AuthService() {
        if (FirebaseApp.getApps().isEmpty()) {
            try (InputStream serviceAccount = getClass().getResourceAsStream("/firebase-config.json")) {
                if (serviceAccount == null) {
                    throw new RuntimeException("Firebase config file not found");
                }
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://gamehaven-31f9f.firebaseio.com")
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                throw new RuntimeException("Firebase initialization failed", e);
            }
        }
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public CompletableFuture<Void> register(String email, String password, String username) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);
            UserRecord userRecord = auth.createUser(request);
            databaseRef.child("users").child(userRecord.getUid())
                    .setValue(new User(userRecord.getUid(), username), (error, ref) -> {
                        if (error == null) {
                            future.complete(null);
                        } else {
                            future.completeExceptionally(error.toException());
                        }
                    });
        } catch (FirebaseAuthException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public CompletableFuture<User> login(String email, String password) {
        CompletableFuture<User> future = new CompletableFuture<>();
        try {
            UserRecord userRecord = auth.getUserByEmail(email);

            databaseRef.child("users").child(userRecord.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user == null) {
                                user = new User(userRecord.getUid(), email.split("@")[0]);
                            }
                            UserSession.setCurrentUser(user);
                            future.complete(user);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            future.completeExceptionally(databaseError.toException());
                        }
                    });
        } catch (FirebaseAuthException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public void register(String email, String password, String username, AuthCallback callback) {
        register(email, password, username)
                .thenRun(callback::onSuccess)
                .exceptionally(ex -> {
                    callback.onError(ex.getMessage());
                    return null;
                });
    }

    public void login(String email, String password, AuthCallback callback) {
        login(email, password)
                .thenAccept(callback::onSuccess)
                .exceptionally(ex -> {
                    callback.onError(ex.getMessage());
                    return null;
                });
    }
}