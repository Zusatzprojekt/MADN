package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.logic.MadnGameL;
import com.github.zusatzprojekt.madn.logic.MadnPlayerL;
import com.github.zusatzprojekt.madn.ui.UIManager;
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

        setupPlayers();
        setupBoard();
        createBindings();
    }

    private void setupPlayers() {
        int playerCount = (int) activePlayers.get("playerCount");
        MadnPlayerL[] playerList = new MadnPlayerL[playerCount];

        if ((boolean) activePlayers.get("playerRed")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.RED);
        }

        if ((boolean) activePlayers.get("playerGreen")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.GREEN);
        }

        if ((boolean) activePlayers.get("playerYellow")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.YELLOW);
        }

        if ((boolean) activePlayers.get("playerBlue")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.BLUE);
        }

        game = new MadnGameL(playerList, visualDice);

        game.currentPlayerObservable().addListener((observableValue, lastPlayer, player) -> {
            gameBoard.currentRollProperty().bind(player.lastRollObservable());
        });
    }

    private void setupBoard() {

        for (MadnPlayerL player : game.getPlayerList()) {
            gameBoard.getPlayerContainer().getChildren().add(new MadnPlayerV(player, gameBoard));
        }
    }

    private void createBindings() {

        game.currentPlayerObservable().addListener((observableValue, lastPlayer, currentPlayer) -> {
            setCurrentPlayerLabel(currentPlayer);
        });

        setCurrentPlayerLabel(game.currentPlayerObservable().getValue());
    }

    private void setCurrentPlayerLabel(MadnPlayerL player) {
        currentPlayerLabel.setText(switch (player.getPlayerID()) {
            case BLUE -> "Blau";
            case YELLOW -> "Gelb";
            case GREEN -> "Grün";
            case RED -> "Rot";
        });
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        ConfirmationDialogYesNo closeDialog = new ConfirmationDialogYesNo(UIManager.getMainStage(), "Beenden", "Möchten Sie das Spiel wirklich beenden?", "Wenn Sie das Spiel beenden, wird der aktuelle Spielfortschritt nicht gespeichert!");
        closeDialog.setDefaultButton(ButtonType.NO);
        closeDialog.onYesButtonPressed(actionEvent1 -> UIManager.closeApplication());
        closeDialog.show();
    }

    @FXML
    private void backToMainMenu(ActionEvent actionEvent) {
        ConfirmationDialogYesNo mainMenuDialog = new ConfirmationDialogYesNo(UIManager.getMainStage(), "Hauptmenü", "Möchten Sie wirklich zum Hauptmenü?", "Wenn Sie zum Hauptmenü gehen, wird der aktuelle Spielfortschritt nicht gespeichert!");
        mainMenuDialog.setDefaultButton(ButtonType.NO);
        mainMenuDialog.onYesButtonPressed(actionEvent1 -> UIManager.loadScene("ui/start-view.fxml", activePlayers));
        mainMenuDialog.show();
    }

}
