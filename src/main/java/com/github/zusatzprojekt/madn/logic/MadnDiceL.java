package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.ui.components.MadnDiceV;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Random;

public class MadnDiceL {
    private final BooleanProperty enabled = new SimpleBooleanProperty(false);
    private final Random random;

    public MadnDiceL(MadnDiceV dice) {
        random = new Random();

        initListeners(dice);
    }

    private void initListeners(MadnDiceV dice) {
        disabled.addListener((observable, oldValue, value) -> {

        });
    }

    public int roll() {
        return random.nextInt(1, 7);
    }

    private void setDisabled(boolean b) {
        disabled.setValue(b);
    }

}
