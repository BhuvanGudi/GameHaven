package org.example.gamehaven.ui.controllers;

import com.google.firebase.database.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.gamehaven.auth.User;
import org.example.gamehaven.core.SceneManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaderboardController {
    private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);

    // Tables
    @FXML private TableView<User> overallTable;
    @FXML private TableView<User> tttTable;
    @FXML private TableView<User> c4Table;
    @FXML private TableView<User> checkersTable;
    @FXML private TabPane leaderboardTabPane;

    // Data
    private final ObservableList<User> overallData = FXCollections.observableArrayList();
    private final ObservableList<User> tttData = FXCollections.observableArrayList();
    private final ObservableList<User> c4Data = FXCollections.observableArrayList();
    private final ObservableList<User> checkersData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableResizing();
        loadAllLeaderboardData();

        // Initial resize
        Platform.runLater(this::resizeAllTables);
    }

    private void setupTableColumns() {
        // Overall table
        setupTableColumn(overallTable, "username", "wins", "losses", "winRate");

        // Tic Tac Toe table
        setupTableColumn(tttTable, "username", "tttWins", "tttLosses", "tttDraws", "tttWinRate");

        // Connect-Four table
        setupTableColumn(c4Table, "username", "c4Wins", "c4Losses", "c4Draws", "c4WinRate");

        // Checkers table
        setupTableColumn(checkersTable, "username", "checkersWins", "checkersLosses", "checkersDraws", "checkersWinRate");
    }

    private void setupTableColumn(TableView<User> table, String... properties) {
        if (table == null || table.getColumns().size() != properties.length) return;

        for (int i = 0; i < properties.length; i++) {
            TableColumn<User, ?> column = table.getColumns().get(i);
            column.setCellValueFactory(new PropertyValueFactory<>(properties[i]));
        }

        // Set data based on which table it is
        if (table == overallTable) table.setItems(overallData);
        else if (table == tttTable) table.setItems(tttData);
        else if (table == c4Table) table.setItems(c4Data);
        else if (table == checkersTable) table.setItems(checkersData);
    }

    private void loadAllLeaderboardData() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("wins").limitToLast(10).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        overallData.clear();
                        tttData.clear();
                        c4Data.clear();
                        checkersData.clear();

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            User user = userSnapshot.getValue(User.class);
                            if (user != null) {
                                // Add to all lists (they'll show different stats via column bindings)
                                overallData.add(user);
                                tttData.add(user);
                                c4Data.add(user);
                                checkersData.add(user);
                            }
                        }

                        // Sort each table by its primary metric
                        overallData.sort((u1, u2) -> Integer.compare(u2.getWins(), u1.getWins()));
                        tttData.sort((u1, u2) -> Integer.compare(u2.getTttWins(), u1.getTttWins()));
                        c4Data.sort((u1, u2) -> Integer.compare(u2.getC4Wins(), u1.getC4Wins()));
                        checkersData.sort((u1, u2) -> Integer.compare(u2.getCheckersWins(), u1.getCheckersWins()));

                        // Trigger table resize after data loads
                        Platform.runLater(() -> resizeAllTables());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        logger.error("Failed to load leaderboard: {}", databaseError.getMessage());
                    }
                }
        );
    }

    /* ========== TABLE RESIZING CODE (YOUR ORIGINAL IMPLEMENTATION) ========== */
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

        // Get available width (subtract 10 px for padding/borders)
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
    /* ========== END TABLE RESIZING CODE ========== */

    @FXML
    private void handleRefresh() {
        loadAllLeaderboardData();
    }

    @FXML
    private void handleBack() {
        SceneManager.loadScene("lobby/main.fxml");
    }
}