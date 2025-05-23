package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.ui.UIManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StartViewController implements Initializable, FxmlValueReceiver {
    private final IntegerProperty countPlayer = new SimpleIntegerProperty(0);
    private final BooleanProperty playerBlue = new SimpleBooleanProperty(false);
    private final BooleanProperty playerYellow = new SimpleBooleanProperty(false);
    private final BooleanProperty playerGreen = new SimpleBooleanProperty(false);
    private final BooleanProperty playerRed = new SimpleBooleanProperty(false);

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
        playButton.disableProperty().bind(countPlayer.greaterThanOrEqualTo(2).not());

        playerBlue.addListener((observableValue, invertedValue, value) -> {
            countPlayer.setValue(value ? countPlayer.getValue() + 1 : countPlayer.getValue() - 1);
        });

        playerYellow.addListener((observableValue, invertedValue, value) -> {
            countPlayer.setValue(value ? countPlayer.getValue() + 1 : countPlayer.getValue() - 1);
        });

        playerGreen.addListener((observableValue, invertedValue, value) -> {
            countPlayer.setValue(value ? countPlayer.getValue() + 1 : countPlayer.getValue() - 1);
        });

        playerRed.addListener((observableValue, invertedValue, value) -> {
            countPlayer.setValue(value ? countPlayer.getValue() + 1 : countPlayer.getValue() - 1);
        });
    }

    @FXML
    private void clickedPlayButton(ActionEvent actionEvent) {
        UIManager.loadScene("ui/game-view.fxml", createDataPacket());
    }

    private HashMap<String, Object> createDataPacket(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("playerBlue", playerBlue.get());
        data.put("playerYellow", playerYellow.get());
        data.put("playerGreen", playerGreen.get());
        data.put("playerRed", playerRed.get());
        data.put("playerCount", countPlayer.get());

        return data;
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        playerBlue.setValue((boolean) values.get("playerBlue"));
        playerYellow.setValue((boolean) values.get("playerYellow"));
        playerGreen.setValue((boolean) values.get("playerGreen"));
        playerRed.setValue((boolean) values.get("playerRed"));
        countPlayer.setValue((int) values.get("playerCount"));
    }

}
