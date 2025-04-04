package com.github.zusatzprojekt.madn.logic;

import javafx.scene.paint.Color;

public class Player {
    public enum PlayerID {BLUE, YELLOW, GREEN, RED};

    private Figure[] figures;
    private PlayerID playerID;
    private int startField;
    private boolean finished = false;
    private Color color;
    private int lastRoll = 0;
    private int rollCount = 0;

    public Player(PlayerID playerID, int startField) {
        switch (playerID) {
            case BLUE:
                color = Color.web("#3254f5");
                break;
            case YELLOW:
                color = Color.web("#FFFF00");
                break;
            case GREEN:
                color = Color.web("#009A00");
                break;
            case RED:
                color = Color.web("#FF0000");
                break;
        }

        this.figures = new Figure[]{
                new Figure(-1, color),
                new Figure(-2, color),
                new Figure(-3, color),
                new Figure(-4, color)
        };

        this.playerID = playerID;
        this.startField = startField;
    }

    public Player(Figure[] figures, PlayerID playerID, int startField) {
        switch (playerID) {
            case BLUE:
                color = Color.web("#3254f5");
                break;
            case YELLOW:
                color = Color.web("#FFFF00");
                break;
            case GREEN:
                color = Color.web("#009A00");
                break;
            case RED:
                color = Color.web("#FF0000");
                break;
        }

        this.figures = figures;
        this.playerID = playerID;
        this.startField = startField;
    }

    public Figure[] canFigureMove(int rolledValue){
        // TODO: Implementation
        return null;
    }

    public boolean moveFigure(Figure movableFigure, int rolledValue){
        // TODO: Implementation
        return false;
    }

    public Figure[] getFigures() {
        return figures;
    }

    public PlayerID getPlayerID() {
        return playerID;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getStartField() {
        return startField;
    }

    public int getLastRoll() {
        return lastRoll;
    }

    public void setLastRoll(int lastRoll) {
        this.lastRoll = lastRoll;
    }
}
