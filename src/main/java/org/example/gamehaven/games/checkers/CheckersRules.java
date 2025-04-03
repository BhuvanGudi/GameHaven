package org.example.gamehaven.games.checkers;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class CheckersRules {
    public TabPane rulesTabPane;
    public Tab rulesTabPage1;
    public Tab rulesTabPage2;
    public Tab rulesTabPage3;
    public Tab rulesTabPage4;

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
