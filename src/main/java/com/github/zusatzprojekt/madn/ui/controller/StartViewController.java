package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector2;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StartViewController implements Initializable, FxmlController, FxmlValueReceiver {
    private int countPlayer;
    private final BooleanProperty playerBlue = new SimpleBooleanProperty(false);
    private final BooleanProperty playerYellow = new SimpleBooleanProperty(false);
    private final BooleanProperty playerGreen = new SimpleBooleanProperty(false);
    private final BooleanProperty playerRed = new SimpleBooleanProperty(false);
    private FxmlControllerConnector2 connector;

    @FXML
    private CheckBox cbBlue, cbYellow, cbRed, cbGreen;
    @FXML
    private Button playButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbBlue.selectedProperty().bindBidirectional(playerBlue);
        cbYellow.selectedProperty().bindBidirectional(playerYellow);
        cbGreen.selectedProperty().bindBidirectional(playerGreen);
        cbRed.selectedProperty().bindBidirectional(playerRed);

        playerBlue.addListener((observableValue, invertedValue, value) -> {
            if (value) {
                countPlayer++;
            } else {
                countPlayer--;
            }
            changeButtonState();
        });

        playerYellow.addListener((observableValue, invertedValue, value) -> {
            if (value) {
                countPlayer++;
            } else {
                countPlayer--;
            }
            changeButtonState();
        });

        playerGreen.addListener((observableValue, invertedValue, value) -> {
            if (value) {
                countPlayer++;
            } else {
                countPlayer--;
            }
            changeButtonState();
        });

        playerRed.addListener((observableValue, invertedValue, value) -> {
            if (value) {
                countPlayer++;
            } else {
                countPlayer--;
            }
            changeButtonState();
        });
    }

    @FXML
    private void clickedPlayButton(ActionEvent actionEvent) throws IOException {
        connector.loadScene("ui/game-view.fxml",createDataPacket());
    }

    private void changeButtonState(){
        playButton.setDisable(countPlayer < 2);
    }

    private HashMap<String, Object> createDataPacket(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("playerBlue", playerBlue.get());
        data.put("playerYellow", playerYellow.get());
        data.put("playerGreen", playerGreen.get());
        data.put("playerRed", playerRed.get());
        data.put("playerCount", countPlayer);

        return data;
    }

    @Override
    public void setConnector(FxmlControllerConnector connector) {
        this.connector = (FxmlControllerConnector2) connector;
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        playerBlue.setValue((boolean) values.get("playerBlue"));
        playerYellow.setValue((boolean) values.get("playerYellow"));
        playerGreen.setValue((boolean) values.get("playerGreen"));
        playerRed.setValue((boolean) values.get("playerRed"));
        countPlayer = (int) values.get("playerCount");

        changeButtonState();
    }

}
