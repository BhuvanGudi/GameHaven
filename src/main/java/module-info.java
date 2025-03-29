module org.example.duplicategame {
    // JavaFX Requirements
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires transitive javafx.graphics;

    // Firebase Requirements
    requires firebase.admin;
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires google.api.client;
    requires com.google.gson; // Needed for Firebase JSON serialization
    requires org.slf4j; // For Firebase logging

    // Open packages for FXML and Firebase
    opens org.example.gamehaven.auth to javafx.fxml, firebase.admin, com.google.gson;
    opens org.example.gamehaven.core to javafx.fxml;
    opens org.example.gamehaven.ui.controllers to javafx.fxml, firebase.admin;
    opens org.example.gamehaven.ui.views to javafx.fxml;

    // Export public API packages
    exports org.example.gamehaven.auth;
    exports org.example.gamehaven.core;
    exports org.example.gamehaven.ui.controllers;
    exports org.example.gamehaven.ui.views;
}