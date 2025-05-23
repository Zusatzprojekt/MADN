package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.ui.UIManager;
import javafx.fxml.FXML;

import java.util.Map;

public class EndViewController implements FxmlValueReceiver {
    private Map<String, Object> lastActivePlayers;

    @FXML
    public void quitGame() {
        UIManager.closeApplication();
    }

    @FXML
    public void switchToStartView() {
        UIManager.loadScene("ui/start-view.fxml", lastActivePlayers);
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        lastActivePlayers = values;
    }

}
