package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller für die Startansicht des Spiels „Mensch ärgere dich nicht“.
 * <p>
 * Ermöglicht die Auswahl aktiver Spielerfarben vor Spielbeginn. Bei gültiger Auswahl (mindestens 2 Spieler)
 * kann das Spiel über den „Spielen“-Button gestartet werden.
 * </p>
 * <p>
 * Die Klasse implementiert {@link Initializable} zur Initialisierung der Komponentenbindung sowie
 * {@link FxmlValueReceiver} zur Annahme übergebener Zustände (z.B. Rückkehr aus dem aktiven Spiel).
 * </p>
 */
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
    @FXML
    private Label noteLabel;

    /**
     * Initialisiert die UI-Komponenten nach dem Laden des FXML-Dokuments.
     * <p>
     * Stellt Bindungen zwischen den Checkboxen und den internen Eigenschaften her,
     * aktiviert/deaktiviert den „Spielen“-Button abhängig von der Spielerauswahl
     * und zeigt eine Hinweisnachricht bei zu wenig Spielern.
     * </p>
     *
     * @param url            Wird nicht verwendet.
     * @param resourceBundle Wird nicht verwendet.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbBlue.selectedProperty().bindBidirectional(playerBlue);
        cbYellow.selectedProperty().bindBidirectional(playerYellow);
        cbGreen.selectedProperty().bindBidirectional(playerGreen);
        cbRed.selectedProperty().bindBidirectional(playerRed);
        playButton.disableProperty().bind(countPlayer.lessThan(2));
        noteLabel.visibleProperty().bind(countPlayer.lessThan(2));

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

    /**
     * Wird beim Klick auf den „Spielen“-Button aufgerufen.
     * <p>
     * Erstellt ein Datenpaket mit allen aktiven Spielern und lädt die Spielszene.
     * </p>
     *
     * @param actionEvent Das auslösende Ereignis.
     */
    @FXML
    private void clickedPlayButton(ActionEvent actionEvent) {
        AppManager.loadScene("ui/game-view.fxml", createDataPacket());
    }

    /**
     * Erstellt ein Datenpaket mit den aktuellen Spielerauswahlen zur Übergabe an die nächste Szene.
     *
     * @return Eine Map mit Spielerinformationen (Farbe, Anzahl).
     */
    private HashMap<String, Object> createDataPacket(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("playerBlue", playerBlue.get());
        data.put("playerYellow", playerYellow.get());
        data.put("playerGreen", playerGreen.get());
        data.put("playerRed", playerRed.get());
        data.put("playerCount", countPlayer.get());

        return data;
    }

    /**
     * Nimmt Werte entgegen, z.B. wenn man aus der Spielszene zurückkehrt, und stellt die vorherigen
     * Spielerauswahlen wieder her.
     *
     * @param values Eine Map mit gespeicherten Werten.
     */
    @Override
    public void receiveValues(Map<String, Object> values) {
        playerBlue.setValue((boolean) values.get("playerBlue"));
        playerYellow.setValue((boolean) values.get("playerYellow"));
        playerGreen.setValue((boolean) values.get("playerGreen"));
        playerRed.setValue((boolean) values.get("playerRed"));
        countPlayer.setValue((int) values.get("playerCount"));
    }

}
