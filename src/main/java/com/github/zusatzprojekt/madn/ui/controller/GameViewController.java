package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.logic.MadnGameL;
import com.github.zusatzprojekt.madn.logic.MadnPlayerL;
import com.github.zusatzprojekt.madn.ui.AppManager;
import com.github.zusatzprojekt.madn.ui.components.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Map;

/**
 * Controller für die Spielansicht des Spiels „Mensch ärgere dich nicht“.
 * <p>
 * Diese Klasse initialisiert die Spielumgebung, stellt Bindungen zwischen Logik- und UI-Komponenten her
 * und verwaltet Benutzerinteraktionen in der laufenden Partie.
 * </p>
 * <p>
 * Sie implementiert {@link FxmlValueReceiver}, um Konfigurationswerte wie aktive Spieler aus der vorherigen
 * Szene zu übernehmen.
 * </p>
 */
public class GameViewController implements FxmlValueReceiver {
    private Map<String, Object> activePlayers;
    private MadnGameL game;

    @FXML
    private MadnBoardV gameBoard;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private MadnDiceV visualDice;
    @FXML
    private MadnInfoTextV infoText;

    /**
     * Empfängt Werte von der aufrufenden Szene, wie Spielerinformationen und initialisiert das Spiel.
     *
     * @param values Eine Werte-Map mit Konfigurationsdaten (Spielerinformationen).
     */
    @Override
    public void receiveValues(Map<String, Object> values) {
        activePlayers = values;
        game = new MadnGameL(activePlayers, gameBoard, visualDice, infoText);

        setupBoard();
        createBindings();

        game.setupGame();
        infoText.setOnFinished(event -> game.startGame());
        infoText.showTextOverlay("Start!", Duration.millis(500));
    }

    /**
     * Erstellt die visuellen Spielerobjekte und verknüpft sie mit der Spiellogik.
     */
    private void setupBoard() {

        for (MadnPlayerL player: game.getPlayerList()) {
            MadnPlayerV p = new MadnPlayerV(player, gameBoard);
            for (MadnFigureV figure : p.getFigures()) {
                figure.mouseClickEvent().bind(game.figureClickedObservable());
            }
        }

    }

    /**
     * Erstellt UI-Bindungen zwischen Spiellogik und Benutzeroberfläche, z.B. für die Spieleranzeige.
     */
    private void createBindings() {

        game.currentPlayerObservable().addListener((observableValue, oldPlayer, player) -> {
            setCurrentPlayerLabel(player);
        });
    }

    /**
     * Setzt den Text des aktuellen Spielerlabels basierend auf der Spielerfarbe.
     *
     * @param player Der Spieler, dessen Name angezeigt werden soll.
     */
    private void setCurrentPlayerLabel(MadnPlayerL player) {
        currentPlayerLabel.setText(switch (player.getPlayerID()) {
            case BLUE -> "Blau";
            case YELLOW -> "Gelb";
            case GREEN -> "Grün";
            case RED -> "Rot";
            case NONE -> null;
        });
    }

    /**
     * Wird beim Klick auf „Spiel beenden“ aufgerufen.
     * <p>
     * Öffnet einen Bestätigungsdialog zum Beenden der Anwendung.
     * </p>
     *
     * @param actionEvent Das auslösende Event.
     */
    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        ConfirmationDialogYesNo closeDialog = new ConfirmationDialogYesNo(AppManager.getMainStage(), "Beenden", "Möchten Sie das Spiel wirklich beenden?", "Wenn Sie das Spiel beenden, wird der aktuelle Spielfortschritt nicht gespeichert!");
        closeDialog.setDefaultButton(ButtonType.NO);
        closeDialog.onYesButtonPressed(actionEvent1 -> AppManager.closeApplication());
        closeDialog.show();
    }

    /**
     * Wird beim Klick auf „Zurück zum Hauptmenü“ aufgerufen.
     * <p>
     * Öffnet einen Bestätigungsdialog zum Zurückkehren ins Hauptmenü, bzw. Startmenü und lädt ggf. die Startszene.
     * </p>
     *
     * @param actionEvent Das auslösende Event.
     */
    @FXML
    private void backToMainMenu(ActionEvent actionEvent) {
        ConfirmationDialogYesNo mainMenuDialog = new ConfirmationDialogYesNo(AppManager.getMainStage(), "Hauptmenü", "Möchten Sie wirklich zum Hauptmenü?", "Wenn Sie zum Hauptmenü gehen, wird der aktuelle Spielfortschritt nicht gespeichert!");
        mainMenuDialog.setDefaultButton(ButtonType.NO);
        mainMenuDialog.onYesButtonPressed(actionEvent1 -> AppManager.loadScene("ui/start-view.fxml", activePlayers));
        mainMenuDialog.show();
    }

}
