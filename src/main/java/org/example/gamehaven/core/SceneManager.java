package org.example.gamehaven.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void loadScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    SceneManager.class.getResource("/org/example/gamehaven/fxml/" + fxmlPath)
            ));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(
                    SceneManager.class.getResource("/org/example/gamehaven/styles/main.css")
            ).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void showRulesDialog(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    SceneManager.class.getResource("/org/example/gamehaven/fxml/" + fxmlPath)
            ));
            Scene scene = new Scene(root);

            Stage rulesStage = new Stage();
            rulesStage.initModality(Modality.APPLICATION_MODAL);
            rulesStage.initOwner(primaryStage);
            rulesStage.setScene(scene);
            rulesStage.setTitle("Game Rules");
            rulesStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}