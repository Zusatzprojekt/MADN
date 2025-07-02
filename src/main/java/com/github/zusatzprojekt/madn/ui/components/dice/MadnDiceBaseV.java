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

public abstract class MadnDiceBaseV extends Pane {
    protected final Duration animationDuration;
    protected final BooleanProperty enabledProp = new SimpleBooleanProperty(true);
    protected final ObjectProperty<EventHandler<ActionEvent>> onFinishedProp = new SimpleObjectProperty<>();
    protected final ObjectProperty<EventHandler<MouseEvent>> onDiceClickedProp = new SimpleObjectProperty<>();

    protected MadnDiceBaseV(Duration animationDuration) {
        this.animationDuration = animationDuration;
    }

    public abstract void startAnimation(int roll);

    public BooleanProperty enabledProperty() {
        return enabledProp;
    }

    public ObjectProperty<EventHandler<MouseEvent>> onDiceClickedProperty() {
        return onDiceClickedProp;
    }

    public ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty() {
        return onFinishedProp;
    }

}
