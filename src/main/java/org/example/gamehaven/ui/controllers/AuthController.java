package org.example.gamehaven.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gamehaven.auth.AuthService;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.auth.UserSession;
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

        authService.login(email, password, new AuthService.AuthCallback() {
                    @Override
                    public void onSuccess(User user) {}

                    @Override
                    public void onSuccess() {
                        loginButton.setDisable(true);
                        errorLabel.setText("Signing in...");
                        Platform.runLater(() ->
                                errorLabel.setText("Login successful! Enjoy gaming")
                        );
                    }
                    @Override
                    public void onError(String error) {
                        Platform.runLater(() ->
                                errorLabel.setText("Login error: " + error)
                        );
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

        authService.register(email, password, email.split("@")[0], new AuthService.AuthCallback() {
            @Override
            public void onSuccess(User user) {}

            @Override
            public void onSuccess() {
                Platform.runLater(() ->
                        errorLabel.setText("Registration successful! Please login")
                );
            }
            @Override
            public void onError(String error) {
                Platform.runLater(() ->
                        errorLabel.setText("Registration error: " + error)
                );
            }
        });
    }
}