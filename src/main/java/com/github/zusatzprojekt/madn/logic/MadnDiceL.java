package com.github.zusatzprojekt.madn.logic;

import java.util.Random;

public class MadnDiceL {
    private final Random random;

    public MadnDiceL() {
        random = new Random();
    }

    public int roll() {
        return random.nextInt(1, 7);
    }
}
