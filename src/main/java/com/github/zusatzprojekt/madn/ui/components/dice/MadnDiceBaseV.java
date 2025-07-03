package com.github.zusatzprojekt.madn.ui.components.dice;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Abstrakte Basisklasse für Würfel-Views (UI-Komponenten),
 * die gemeinsame Eigenschaften und Events bereitstellt.
 */
public abstract class MadnDiceBaseV extends Pane {
    protected final Duration animationDuration;
    protected final BooleanProperty enabledProp = new SimpleBooleanProperty(true);
    protected final ObjectProperty<EventHandler<ActionEvent>> onFinishedProp = new SimpleObjectProperty<>();
    protected final ObjectProperty<EventHandler<MouseEvent>> onDiceClickedProp = new SimpleObjectProperty<>();

    /**
     * Konstruktor mit Angabe der Animationsdauer.
     *
     * @param animationDuration Dauer der Würfelanimation
     */
    protected MadnDiceBaseV(Duration animationDuration) {
        this.animationDuration = animationDuration;
    }

    /**
     * Abstrakte Methode zum Starten der Animation mit einem bestimmten Würfelwurf.
     * Muss von Subklassen implementiert werden.
     *
     * @param roll Gewürfelte Zahl (1–6)
     */
    public abstract void startAnimation(int roll);

    /**
     * Gibt die Property zurück, die angibt, ob der Würfel aktiviert ist.
     *
     * @return BooleanProperty zur Steuerung der Aktivierung
     */
    public BooleanProperty enabledProperty() {
        return enabledProp;
    }

    /**
     * Gibt die Property zurück, über die ein EventHandler für Würfel-Klicks registriert werden kann.
     *
     * @return ObjectProperty für MouseEvent-Handler
     */
    public ObjectProperty<EventHandler<MouseEvent>> onDiceClickedProperty() {
        return onDiceClickedProp;
    }

    /**
     * Gibt die Property zurück, über die ein EventHandler für das Ende der Animation registriert werden kann.
     *
     * @return ObjectProperty für ActionEvent-Handler
     */
    public ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty() {
        return onFinishedProp;
    }

}
