package org.example.gamehaven.auth;

import org.example.gamehaven.auth.User;

public class UserSession {
    private static User currentUser;

    public static void createSession(String userId, String username) {
        currentUser = new User(userId, username);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }
}