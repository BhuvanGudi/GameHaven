package org.example.gamehaven.core;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.gamehaven.utils.SoundManager;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {

        // Initialize Firebase
        AppConfig.initialize();

        // Set up scene manager
        SceneManager.setPrimaryStage(primaryStage);

        // Start background music
        SoundManager.getInstance().playBackgroundMusic();

        // Load initial scene
        SceneManager.loadScene("auth/login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}