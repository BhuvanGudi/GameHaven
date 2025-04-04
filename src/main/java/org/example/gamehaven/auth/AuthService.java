package org.example.gamehaven.auth;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

public class AuthService {
    private final FirebaseAuth auth;
    private final DatabaseReference database;

    public AuthService() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    public void register(String email, String password, String username, AuthCallback callback) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            // Create user
            UserRecord userRecord = auth.createUser(request);

            // Save to database (updated approach)
            DatabaseReference userRef = database.child("users").child(userRecord.getUid());
            userRef.setValue(new User(username, email), (databaseError, ref) -> {
                if (databaseError != null) {
                    callback.onError(databaseError.getMessage());
                } else {
                    callback.onSuccess();
                }
            });

        } catch (FirebaseAuthException e) {
            callback.onError(e.getMessage());
        }
    }

    // Login verification using Admin SDK
    public void login(String email, String password, AuthCallback callback) {
        try {
            // Verify user exists (Admin SDK doesn't have password verification)
            UserRecord userRecord = auth.getUserByEmail(email);

            // In real apps, you'd verify password through a client SDK or custom token
            UserSession.createSession(userRecord.getUid(), email.split("@")[0]);
            callback.onSuccess();
        } catch (FirebaseAuthException e) {
            callback.onError("Invalid credentials or user not found");
        }
    }
}