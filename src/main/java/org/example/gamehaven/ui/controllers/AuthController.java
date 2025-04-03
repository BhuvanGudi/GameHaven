package org.example.gamehaven.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gamehaven.auth.AuthService;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.core.SceneManager;

public class AuthController {
    public Button loginButton;
    public Button registerButton;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password");
            return;
        }

        loginButton.setDisable(true);
        errorLabel.setText("Signing in...");

        authService.login(email, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Platform.runLater(() -> {
                    errorLabel.setText("Login successful! Welcome " + user.getUsername());
                    SceneManager.loadScene("lobby/main.fxml");
                });
            }

            @Override
            public void onSuccess() {
                // Not used for login
            }

            @Override
            public void onError(String error) {
                Platform.runLater(() -> {
                    errorLabel.setText("Login failed: " + error);
                    loginButton.setDisable(false);
                });
            }
        });
    }

    @FXML
    private void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password");
            return;
        }

        registerButton.setDisable(true);
        errorLabel.setText("Registering...");

        authService.register(email, password, email.split("@")[0], new AuthService.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                // Not used for registration
            }

            @Override
            public void onSuccess() {
                Platform.runLater(() -> {
                    errorLabel.setText("Registration successful! You can now login");
                    registerButton.setDisable(false);
                    // Clear password field for security
                    passwordField.clear();
                });
            }

            @Override
            public void onError(String error) {
                Platform.runLater(() -> {
                    errorLabel.setText("Registration failed: " + error);
                    registerButton.setDisable(false);
                });
            }
        });
    }
}