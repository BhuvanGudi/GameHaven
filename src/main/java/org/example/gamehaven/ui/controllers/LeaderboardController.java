package org.example.gamehaven.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.core.SceneManager;

public class LeaderboardController {
    @FXML private TableView<User> overallTable;
    @FXML private TableView<User> tttTable;
    @FXML private TableView<User> c4Table;
    @FXML private TableView<User> checkersTable;
    @FXML private TabPane leaderboardTabPane;

    @FXML
    public void initialize() {
        setupTables();
        loadData();
        setupTableResizing();
    }

    private void setupTables() {
        overallTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tttTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        c4Table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        checkersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadData() {
        ObservableList<User> sampleData = FXCollections.observableArrayList(
                new User("1", "Player1"),
                new User("2", "Player2")
        );

        overallTable.setItems(sampleData);
        tttTable.setItems(sampleData);
        c4Table.setItems(sampleData);
        checkersTable.setItems(sampleData);
    }

    private void setupTableResizing() {
        leaderboardTabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateTabWidths();
            resizeAllTables();
        });
    }

    private void updateTabWidths() {
        double tabWidth = leaderboardTabPane.getWidth() / leaderboardTabPane.getTabs().size();
        for (Tab tab : leaderboardTabPane.getTabs()) {
            tab.setStyle("-fx-pref-width: " + Math.max(tabWidth, 120) + "px;");
        }
    }

    private void resizeAllTables() {
        resizeTable(overallTable);
        resizeTable(tttTable);
        resizeTable(c4Table);
        resizeTable(checkersTable);
    }

    private void resizeTable(TableView<?> table) {
        if (table != null) {
            table.setPrefWidth(leaderboardTabPane.getWidth() - 20);
        }
    }

    @FXML
    private void handleRefresh() {
        loadData();
    }

    @FXML
    private void handleBack() {
        SceneManager.loadScene("lobby/main.fxml");
    }
}