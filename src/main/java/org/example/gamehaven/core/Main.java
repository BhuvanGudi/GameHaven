package org.example.gamehaven.core;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        System.out.println("Resource paths:");
        System.out.println("1. " + getClass().getResource("/firebase-config.json"));
        System.out.println("2. " + getClass().getClassLoader().getResource("firebase-config.json"));

        // Initialize Firebase
        AppConfig.initialize();

        // Set up scene manager
        SceneManager.setPrimaryStage(primaryStage);

        // Load initial scene
        SceneManager.loadScene("auth/login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}