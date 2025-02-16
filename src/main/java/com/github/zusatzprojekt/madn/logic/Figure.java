package com.github.zusatzprojekt.madn.logic;

public class Figure {
    private int currentField;
    private boolean onStart;

    public Figure(int currentField) {
        this.currentField = currentField;
        this.onStart = false;
    }

    public boolean canMove(int amount) {
        //TODO: Implementation
        return false;
    }

    public boolean move(int amount) {
        //TODO: Implementation
        return false;
    }

    public int getCurrentField() {
        return this.currentField;
    }

    public boolean isOnStart() {
        return this.onStart;
    }
}
