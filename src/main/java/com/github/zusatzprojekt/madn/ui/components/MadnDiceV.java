package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.ui.AppManager;
import com.github.zusatzprojekt.madn.ui.components.dice.MadnDice3dV;
import com.github.zusatzprojekt.madn.ui.components.dice.MadnDiceBaseV;
import com.github.zusatzprojekt.madn.ui.components.dice.MadnRollBtnV;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


/**
 * Wrapper-Komponente, die je nach Plattform und Konfiguration
 * entweder eine 3D-Würfel-View oder eine 2D-Würfel-Button-View bereitstellt.
 */
public class MadnDiceV extends Group {
    private final Duration ANIMATION_DURATION = Duration.millis(750);
    private final MadnDiceBaseV dice;

    /**
     * Konstruktor entscheidet, ob 3D- oder 2D-Würfel angezeigt wird.
     * Berücksichtigt Systemfähigkeit (SCENE3D) und Kommandozeilenargument "no3d".
     */
    public MadnDiceV() {
        String no3dArg = AppManager.getArguments().getOrDefault("no3d", "false");

        boolean use3D = Platform.isSupported(ConditionalFeature.SCENE3D) && no3dArg.equals("false");

        if (use3D) {
            dice = new MadnDice3dV(ANIMATION_DURATION);
        } else {
            dice = new MadnRollBtnV(ANIMATION_DURATION);
        }

        this.getChildren().add(dice);
    }

    /**
     * Startet die Würfelanimation mit dem übergebenen Wurfwert.
     *
     * @param roll Gewürfelte Zahl (1–6)
     */
    public void startAnimation(int roll) {
        dice.startAnimation(roll);
    }

    /**
     * Setzt den EventHandler, der bei Klick auf den Würfel ausgelöst wird.
     *
     * @param mouseEventEventHandler EventHandler für Mausklick
     */
    public void setOnDiceClicked(EventHandler<MouseEvent> mouseEventEventHandler) {
        dice.onDiceClickedProperty().setValue(mouseEventEventHandler);
    }

    /**
     * Setzt den EventHandler, der beim Ende der Würfelanimation ausgelöst wird.
     *
     * @param actionEventEventHandler EventHandler für Animationsende
     */
    public void setOnFinished(EventHandler<ActionEvent> actionEventEventHandler) {
        dice.onFinishedProperty().setValue(actionEventEventHandler);
    }

    /**
     * Gibt die Eigenschaft zurück, ob der Würfel aktiviert oder deaktiviert ist.
     *
     * @return BooleanProperty der Aktivierung
     */
    public BooleanProperty enabledProperty() {
        return dice.enabledProperty();
    }

}
