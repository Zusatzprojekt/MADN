package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerValueReceiver;

import java.util.Map;


public class GameViewController implements FxmlControllerValueReceiver {
    public boolean playerBlue;
    public boolean playerYellow;
    public boolean playerGreen;
    public boolean playerRed;

    @Override
    public void setConnector(FxmlControllerConnector connector) {

    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        playerBlue = (boolean) values.get("playerBlue");
        playerYellow = (boolean) values.get("playerYellow");
        playerGreen = (boolean) values.get("playerGreen");
        playerRed = (boolean) values.get("playerRed");
    }
}
