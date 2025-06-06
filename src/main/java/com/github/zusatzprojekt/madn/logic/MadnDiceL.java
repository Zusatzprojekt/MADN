package com.github.zusatzprojekt.madn.logic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Random;

public class MadnDiceL {
    private final Random random;
    private final ObjectProperty<EventHandler<ActionEvent>> rollEventProperty = new SimpleObjectProperty<>();

    public MadnDiceL() {
        random = new Random();
    }

    public int roll() {
        return random.nextInt(1, 7);
    }

}
