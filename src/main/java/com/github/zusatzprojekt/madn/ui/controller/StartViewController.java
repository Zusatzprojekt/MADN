package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector2;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StartViewController implements FxmlController, FxmlValueReceiver {
    private int countPlayer;
    private boolean playerBlue, playerYellow, playerGreen, playerRed;
    private FxmlControllerConnector2 connector;
    @FXML
    private CheckBox cbBlue, cbYellow, cbRed, cbGreen;


    @FXML
    public Button playButton;

    @FXML
    public void blueCheckBox(ActionEvent actionEvent) {
        if (cbBlue.isSelected()){
            playerBlue = true;
            countPlayer++;
        } else {
            playerBlue = false;
            countPlayer--;
        }
        changeButtonState();
    }

    @FXML
    public void yellowCheckBox(ActionEvent actionEvent) {
        if (cbYellow.isSelected()){
            playerYellow = true;
            countPlayer++;
        } else {
            playerYellow = false;
            countPlayer--;
        }
        changeButtonState();
    }

    @FXML
    public void greenCheckBox(ActionEvent actionEvent) {
        if (cbGreen.isSelected()){
            playerGreen = true;
            countPlayer++;
        } else {
            playerGreen = false;
            countPlayer--;
        }
        changeButtonState();
    }

    @FXML
    public void redCheckBox(ActionEvent actionEvent) {
        if (cbRed.isSelected()){
            playerRed = true;
            countPlayer++;
        } else {
            playerRed = false;
            countPlayer--;
        }
        changeButtonState();
    }

    @FXML
    public void clickedPlayButton(ActionEvent actionEvent) throws IOException {
        connector.loadScene("ui/game-view.fxml",createDataPacket());
    }

    public void changeButtonState(){
        playButton.setDisable(countPlayer < 2);
    }

    private HashMap<String, Object> createDataPacket(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("playerBlue", playerBlue);
        data.put("playerYellow", playerYellow);
        data.put("playerGreen", playerGreen);
        data.put("playerRed", playerRed);
        data.put("playerCount", countPlayer);

        return data;
    }

    @Override
    public void setConnector(FxmlControllerConnector connector) {
        this.connector = (FxmlControllerConnector2) connector;
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        cbBlue.setSelected((boolean) values.get("playerBlue"));
        cbYellow.setSelected((boolean) values.get("playerYellow"));
        cbGreen.setSelected((boolean) values.get("playerGreen"));
        cbRed.setSelected((boolean) values.get("playerRed"));
        countPlayer = (int) values.get("playerCount");

        changeButtonState();
    }
}
