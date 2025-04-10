package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlController;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector;
import com.github.zusatzprojekt.madn.interfaces.FxmlControllerConnector2;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.ui.components.ConfirmationDialogYesNo;
import com.github.zusatzprojekt.madn.ui.components.MadnGameBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Map;

public class GameViewController implements FxmlController, FxmlValueReceiver {
    private FxmlControllerConnector2 connector;
    private Map<String, Object> activePlayerColors;

    @FXML
    private MadnGameBoard gameBoard;
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

//        values.put("gameViewController", this);

        FxmlValueReceiver gameBoardValueReceiver = gameBoard;
        gameBoardValueReceiver.receiveValues(values);
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        ConfirmationDialogYesNo closeDialog = new ConfirmationDialogYesNo(connector.getWindow(), "Beenden", "Möchten Sie das Spiel wirklich beenden?", "Wenn Sie das Spiel beenden, dann wird der aktuelle Fortschritt nicht gespeichert!");
        closeDialog.setDefaultButton(ButtonType.NO);
        closeDialog.onYesButtonPressed(actionEvent1 -> connector.closeApplication());
        closeDialog.show();
    }

    @FXML
    private void backToMainMenu(ActionEvent actionEvent) {
        ConfirmationDialogYesNo mainMenuDialog = new ConfirmationDialogYesNo(connector.getWindow(), "Hauptmenü", "Möchten Sie wirklich zurück zum Hauptmenü?", "Wenn Sie zurück zum Hauptmenü gehen, dann wird der aktuelle Fortschritt nicht gespeichert!");
        mainMenuDialog.setDefaultButton(ButtonType.NO);
        mainMenuDialog.onYesButtonPressed(actionEvent1 -> connector.loadScene("ui/start-view.fxml", activePlayerColors));
        mainMenuDialog.show();
    }

    public Label getCurrentPlayerLabel() {
        return currentPlayerLabel;
    }

    public Button getRollButton() {
        return rollButton;
    }
}
