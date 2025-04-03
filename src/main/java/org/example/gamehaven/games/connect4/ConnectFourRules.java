package org.example.gamehaven.games.connect4;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ConnectFourRules {
    public TabPane rulesTabPane;

    public void initialize() {
        updateTabWidths();
        rulesTabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateTabWidths();
        });
    }

    private void updateTabWidths() {
        double tabWidth = rulesTabPane.getWidth() / rulesTabPane.getTabs().size();
        for (Tab tab : rulesTabPane.getTabs()) {
            double finalWidth = Math.max(tabWidth, 100);
            tab.setStyle("-fx-pref-width: " + finalWidth + "px;");
        }
    }
}
