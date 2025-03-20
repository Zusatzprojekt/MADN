package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.ui.parts.ScalePane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


public class GameViewController implements FxmlController, FxmlValueReceiver, Initializable {
    public boolean playerBlue;
    public boolean playerYellow;
    public boolean playerGreen;
    public boolean playerRed;

    @FXML
    public ScalePane gameBoard;

    @Override
    public void setConnector(FxmlControllerConnector connector) {
        //TODO: Implement
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        playerBlue = (boolean) values.get("playerBlue");
        playerYellow = (boolean) values.get("playerYellow");
        playerGreen = (boolean) values.get("playerGreen");
        playerRed = (boolean) values.get("playerRed");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameBoard.getHeight();
    }
}
