package org.example.gamehaven.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.gamehaven.core.SceneManager;
import org.example.gamehaven.auth.User;

public class LeaderboardController {
    @FXML private TableView<User> overallTable;
    @FXML private TableView<User> tttTable;
    @FXML private TableView<User> c4Table;
    @FXML private TableView<User> checkersTable;
    @FXML private TabPane leaderboardTabPane;

    @FXML
    public void initialize() {
        setupTableResizing();
        // Initial resize
        Platform.runLater(this::resizeAllTables);
    }

    private void setupTableResizing() {
        // Listen to tab changes to resize the current table
        leaderboardTabPane.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldTab, newTab) -> resizeCurrentTable());

        // Listen to width changes of the tab pane
        leaderboardTabPane.widthProperty().addListener(
                (obs, oldVal, newVal) -> resizeAllTables());

        leaderboardTabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateTabWidths();
        });
    }

    private void resizeCurrentTable() {
        TableView<?> currentTable = getCurrentTableView();
        if (currentTable != null) {
            resizeTable(currentTable);
        }
    }

    private TableView<?> getCurrentTableView() {
        Tab selectedTab = leaderboardTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null && selectedTab.getContent() instanceof TableView) {
            return (TableView<?>) selectedTab.getContent();
        }
        return null;
    }

    private void resizeAllTables() {
        resizeTable(overallTable);
        resizeTable(tttTable);
        resizeTable(c4Table);
        resizeTable(checkersTable);
    }

    private void resizeTable(TableView<?> table) {
        if (table == null || table.getColumns().isEmpty()) return;

        // Get available width (subtract 10px for padding/borders)
        double availableWidth = table.getWidth() - 10;
        int columnCount = table.getColumns().size();

        if (availableWidth > 0 && columnCount > 0) {
            double columnWidth = availableWidth / columnCount;

            // Force all columns to this width (override any prefWidth)
            for (TableColumn<?, ?> column : table.getColumns()) {
                column.setPrefWidth(columnWidth);
            }
        }
    }

    private void updateTabWidths() {
        double tabWidth = leaderboardTabPane.getWidth() / leaderboardTabPane.getTabs().size();
        for (Tab tab : leaderboardTabPane.getTabs()) {
            double finalWidth = Math.max(tabWidth, 100);
            tab.setStyle("-fx-pref-width: " + finalWidth + "px;");
        }
    }

    // Your existing handler methods
    @FXML private void handleRefresh() {SceneManager.loadScene("lobby/main.fxml");}
    @FXML private void handleBack() { /* ... */ }
}