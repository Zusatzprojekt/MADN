package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Eine Komponente zur Anzeige temporärer Info-Overlays (Texte) im Spiel "Mensch ärgere dich nicht".
 *
 * Zeigt eine Nachricht mit Einblende- und Ausblendeffekt für eine bestimmte Dauer an.
 */
public class MadnInfoTextV extends ScalePane {
    private final ObjectProperty<EventHandler<ActionEvent>> onFinishedProp = new SimpleObjectProperty<>();

    @FXML
    private Label text;


    // == Konstruktor =============================================================================

    /**
     * Konstruktor, lädt das zugehörige FXML-Layout und initialisiert die Sichtbarkeit.
     */
    public MadnInfoTextV() {
        AppManager.loadComponentFxml("ui/components/madn-info-text-v.fxml", this, this);
        setVisible(false);
    }


    // == Methoden =============================================================================

    /**
     * Zeigt den übergebenen Text als Overlay an. Der Text wird eingeblendet, bleibt für die angegebene
     * Dauer sichtbar und blendet sich dann automatisch aus.
     *
     * @param s        Der darzustellende Text.
     * @param duration Die Dauer, für die der Text eingeblendet bleibt (ohne Ein-/Ausblendzeit).
     */
    public void showTextOverlay(String s, Duration duration) {
        FadeTransition ftIn = new FadeTransition(Duration.millis(250), this);
        ftIn.setToValue(1.0);

        Timeline tl = new Timeline(new KeyFrame(duration));

        FadeTransition ftOut = new FadeTransition(Duration.millis(500), this);
        ftOut.setToValue(0.0);

        ftIn.setOnFinished(event -> tl.play());

        tl.setOnFinished(event -> ftOut.play());

        ftOut.setOnFinished(event -> {
            setVisible(false);
            if (onFinishedProp.isNotNull().getValue()) {
                onFinishedProp.getValue().handle(event);
            }
        });

        text.setText(s);
        setVisible(true);
        setOpacity(0.0);

        ftIn.play();
    }

    public void setOnFinished(EventHandler<ActionEvent> eventHandler) {
        onFinishedProp.setValue(eventHandler);
    }

}