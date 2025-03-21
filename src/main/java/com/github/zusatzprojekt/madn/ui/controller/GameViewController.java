package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import javafx.event.ActionEvent;
import com.github.zusatzprojekt.madn.ui.components.GameBoard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GameViewController implements Initializable, FxmlController, FxmlValueReceiver {
    private FxmlValueReceiver gameBoardValueReceiver;
    private FxmlControllerConnector connector;
    @FXML
    public GameBoard gameBoard;

//    @FXML
//    public Window gameBoardContainer;
//    @FXML
//    public GameBoardController gameBoardController;

    @Override
    public void setConnector(FxmlControllerConnector connector) {
        this.connector = connector;
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
//        playerBlue = (boolean) values.get("playerBlue");
//        playerYellow = (boolean) values.get("playerYellow");
//        playerGreen = (boolean) values.get("playerGreen");
//        playerRed = (boolean) values.get("playerRed");

        FxmlValueReceiver gameBoardValueReceiver = gameBoard;
        gameBoardValueReceiver.receiveValues(values);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Stage test = (Stage) gameBoardContainer;
//        gameBoardValueReceiver = (FxmlValueReceiver) (Parent) gameBoardContainer.getChildren()[0];

//        FxmlValueReceiver gameBoardValueReceiver = gameBoard;
//        gameBoardValueReceiver.receiveValues(createDataPacket());

        System.out.println("Initialize");
    }


    public void dice(ActionEvent actionEvent) {

    }

    public void restartGame(ActionEvent actionEvent) {
        Alert restartDialog = new Alert(Alert.AlertType.CONFIRMATION, "Wenn Sie das Spiel neu starten, dann wird der aktuelle Fortschritt nicht gespeichert!", ButtonType.YES, ButtonType.NO);
        restartDialog.setTitle("Neustart");
        restartDialog.setHeaderText("Möchten Sie das Spiel wirklich neu starten?");
        restartDialog.setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.YES)){
                try {
                    connector.loadScene("ui/game-view.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return buttonType;
        });
        restartDialog.show();
    }

    public void exitApplication(ActionEvent actionEvent) {
        Alert closeDialog = new Alert(Alert.AlertType.CONFIRMATION, "Wenn Sie das Spiel beenden, dann wird der aktuelle Fortschritt nicht gespeichert!", ButtonType.YES, ButtonType.NO);
        closeDialog.setTitle("Beenden");
        closeDialog.setHeaderText("Möchten Sie das Spiel wirklich beenden?");
        closeDialog.setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.YES)){
                connector.closeApplication();
            }
            return buttonType;
        });
        closeDialog.show();

    }
}
