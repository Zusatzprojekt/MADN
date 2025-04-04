package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector2;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import javafx.event.ActionEvent;
import com.github.zusatzprojekt.madn.ui.components.GameBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Map;

public class GameViewController implements FxmlController, FxmlValueReceiver {
    private FxmlControllerConnector2 connector;
    private Map<String, Object> activePlayerColors;

    @FXML
    private GameBoard gameBoard;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private Button rollButton;

    @Override
    public void setConnector(FxmlControllerConnector connector) {
        this.connector = (FxmlControllerConnector2) connector;
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        activePlayerColors = values;

        values.put("gameViewController", this);

        FxmlValueReceiver gameBoardValueReceiver = gameBoard;
        gameBoardValueReceiver.receiveValues(values);
    }

    public void dice(ActionEvent actionEvent) {
        //TODO: implement rolling the dice
    }

    public void exitApplication(ActionEvent actionEvent) {
        Alert closeDialog = new Alert(Alert.AlertType.CONFIRMATION, "Wenn Sie das Spiel beenden, dann wird der aktuelle Fortschritt nicht gespeichert!", ButtonType.YES, ButtonType.NO);
        closeDialog.setTitle("Beenden");
        closeDialog.setHeaderText("Möchten Sie das Spiel wirklich beenden?");

        Button yesButton = (Button) closeDialog.getDialogPane().lookupButton( ButtonType.YES );
        yesButton.setDefaultButton( false );

        Button noButton = (Button) closeDialog.getDialogPane().lookupButton( ButtonType.NO );
        noButton.setDefaultButton( true );

        closeDialog.setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.YES)){
                connector.closeApplication();
            }
            return buttonType;
        });

        closeDialog.show();
    }

    public void backToMainMenu(ActionEvent actionEvent) {
        Alert mainMenuDialog = new Alert(Alert.AlertType.CONFIRMATION, "Wenn Sie zurück zum Hauptmenü gehen, dann wird der aktuelle Fortschritt nicht gespeichert!", ButtonType.YES, ButtonType.NO);
        mainMenuDialog.setTitle("Hauptmenü");
        mainMenuDialog.setHeaderText("Möchten Sie wirlich zurück zum Hauptmenü?");

        Button yesButton = (Button) mainMenuDialog.getDialogPane().lookupButton( ButtonType.YES );
        yesButton.setDefaultButton( false );

        Button noButton = (Button) mainMenuDialog.getDialogPane().lookupButton( ButtonType.NO );
        noButton.setDefaultButton( true );

        mainMenuDialog.setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.YES)){
                try {
                    connector.loadScene("ui/start-view.fxml", activePlayerColors);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return buttonType;
        });
        mainMenuDialog.show();
    }

    public Label getCurrentPlayerLabel() {
        return currentPlayerLabel;
    }

    public Button getRollButton() {
        return rollButton;
    }
}
