package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Window;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class GameViewController implements Initializable, FxmlController, FxmlValueReceiver {
    private boolean playerBlue, playerYellow, playerGreen, playerRed;
    private FxmlValueReceiver gameBoardValueReceiver;

//    @FXML
//    public Window gameBoardContainer;
//    @FXML
//    public GameBoardController gameBoardController;

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
//        Stage test = (Stage) gameBoardContainer;
//        gameBoardValueReceiver = (FxmlValueReceiver) (Parent) gameBoardContainer.getChildren()[0];
        System.out.println("Initialize");
    }
}
