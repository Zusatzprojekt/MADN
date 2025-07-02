package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.ui.components.MadnDiceV;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Random;

public class MadnDiceL {
    private final BooleanProperty enabledProp = new SimpleBooleanProperty(false);
    private final Random random;

    public MadnDiceL(MadnDiceV visualDice) {
        random = new Random();

        initBindings(visualDice);
    }

    private void initBindings(MadnDiceV visualDice) {
        visualDice.enabledProperty().bind(enabledProp);
    }

    public int roll() {
        return random.nextInt(1, 7);
    }

    public boolean isEnabled() {
        return enabledProp.getValue();
    }

    public void setEnabled(boolean b) {
        enabledProp.setValue(b);
    }

}
