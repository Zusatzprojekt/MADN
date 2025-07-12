package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

/**
 * Controller für die Endansicht des Spiels „Mensch ärgere dich nicht“.
 * <p>
 * Diese Klasse verwaltet die Anzeige der Platzierungen am Spielende sowie die
 * Interaktionen mit den Schaltflächen „Erneut spielen“, „Zurück zum Start“ und „Spiel beenden“.
 * </p>
 * <p>
 * Sie implementiert außerdem {@link FxmlValueReceiver}, um Werte (z. B. aktive Spieler) von vorherigen
 * Szenen zu übernehmen.
 * </p>
 */
public class EndViewController implements FxmlValueReceiver {
    private Map<String, Object> lastActivePlayers;

    @FXML
    private Label firstPlace, secondPlace, thirdPlace, fourthPlace;

    /**
     * Event-Handler für die Schaltfläche „Erneut spielen“.
     * <p>
     * Lädt die Spielfeldszene erneut mit den zuletzt aktiven Spielern.
     * </p>
     *
     * @param actionEvent Ausgelöstes ActionEvent
     */
    @FXML
    private void onPlayAgain(ActionEvent actionEvent) {
        AppManager.loadScene("ui/game-view.fxml", lastActivePlayers);
    }

    /**
     * Event-Handler für die Schaltfläche „Zurück zum Start“.
     * <p>
     * Lädt die Startansicht neu, wobei Spielerinformationen erhalten bleiben.
     * </p>
     *
     * @param actionEvent Ausgelöstes ActionEvent
     */
    @FXML
    private void onBackToStart(ActionEvent actionEvent) {
        AppManager.loadScene("ui/start-view.fxml", lastActivePlayers);
    }

    /**
     * Event-Handler für die Schaltfläche „Spiel beenden“.
     * <p>
     * Beendet die Anwendung.
     * </p>
     *
     * @param actionEvent Ausgelöstes ActionEvent
     */
    @FXML
    private void onQuitGame(ActionEvent actionEvent) {
        AppManager.closeApplication();
    }

    /**
     * Wird aufgerufen, wenn die Szene initialisiert wird und Werte übergeben werden sollen.
     * <p>
     * Diese Methode speichert die übergebenen Werte zur späteren Verwendung.
     * </p>
     *
     * @param values Eine Map mit Daten aus der vorherigen Szene (Spielerinfos).
     */
    @Override
    public void receiveValues(Map<String, Object> values) {
        lastActivePlayers = values;
    }

}
