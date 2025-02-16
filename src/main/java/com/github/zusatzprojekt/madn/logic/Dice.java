package com.github.zusatzprojekt.madn.logic;

import java.util.Random;

public class Dice {
    private final Random random;

    public Dice() {
        random = new Random();
    }

    public int roll() {
        return random.nextInt(1, 7);
    }
}
