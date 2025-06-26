package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.logic.MadnGameL;
import com.github.zusatzprojekt.madn.logic.MadnPlayerL;
import com.github.zusatzprojekt.madn.ui.AppManager;
import com.github.zusatzprojekt.madn.ui.components.ConfirmationDialogYesNo;
import com.github.zusatzprojekt.madn.ui.components.MadnBoardV;
import com.github.zusatzprojekt.madn.ui.components.MadnDiceV;
import com.github.zusatzprojekt.madn.ui.components.MadnPlayerV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Map;

public class GameViewController implements FxmlValueReceiver {
    private Map<String, Object> activePlayers;
    private MadnGameL game;

    @FXML
    private MadnBoardV gameBoard;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private MadnDiceV visualDice;

    @Override
    public void receiveValues(Map<String, Object> values) {
        activePlayers = values;
        game = new MadnGameL(activePlayers, gameBoard, visualDice);

        setupBoard();
        createBindings();

        game.startGame();
    }

    private void setupBoard() {

        for (MadnPlayerL player : game.getPlayerList()) {
            gameBoard.getPlayerContainer().getChildren().add(new MadnPlayerV(player, gameBoard));
        }
    }

    private void createBindings() {

        game.currentPlayerObservable().addListener((observableValue, oldPlayer, player) -> {
            setCurrentPlayerLabel(player);
        });
    }

    private void setCurrentPlayerLabel(MadnPlayerL player) {
        currentPlayerLabel.setText(switch (player.getPlayerID()) {
            case BLUE -> "Blau";
            case YELLOW -> "Gelb";
            case GREEN -> "Grün";
            case RED -> "Rot";
            case NONE -> null;
        });
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        ConfirmationDialogYesNo closeDialog = new ConfirmationDialogYesNo(AppManager.getMainStage(), "Beenden", "Möchten Sie das Spiel wirklich beenden?", "Wenn Sie das Spiel beenden, wird der aktuelle Spielfortschritt nicht gespeichert!");
        closeDialog.setDefaultButton(ButtonType.NO);
        closeDialog.onYesButtonPressed(actionEvent1 -> AppManager.closeApplication());
        closeDialog.show();
    }

    @FXML
    private void backToMainMenu(ActionEvent actionEvent) {
        ConfirmationDialogYesNo mainMenuDialog = new ConfirmationDialogYesNo(AppManager.getMainStage(), "Hauptmenü", "Möchten Sie wirklich zum Hauptmenü?", "Wenn Sie zum Hauptmenü gehen, wird der aktuelle Spielfortschritt nicht gespeichert!");
        mainMenuDialog.setDefaultButton(ButtonType.NO);
        mainMenuDialog.onYesButtonPressed(actionEvent1 -> AppManager.loadScene("ui/start-view.fxml", activePlayers));
        mainMenuDialog.show();
    }

}
