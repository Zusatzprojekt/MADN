package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

public class EndViewController implements FxmlValueReceiver {
    private Map<String, Object> lastActivePlayers;

    @FXML
    private Label firstPlace, secondPlace, thirdPlace, fourthPlace;

    @FXML
    private void onPlayAgain(ActionEvent actionEvent) {
        //TODO: Testen
        AppManager.loadScene("ui/game-view.fxml", lastActivePlayers);
    }

    @FXML
    private void onBackToStart(ActionEvent actionEvent) {
        AppManager.loadScene("ui/start-view.fxml", lastActivePlayers);
    }

    @FXML
    private void onQuitGame(ActionEvent actionEvent) {
        AppManager.closeApplication();
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        lastActivePlayers = values;
    }

}
