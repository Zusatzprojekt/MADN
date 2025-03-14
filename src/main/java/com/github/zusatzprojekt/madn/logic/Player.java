package com.github.zusatzprojekt.madn.logic;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Player extends Group {
    public enum PlayerID {BLUE, YELLOW, GREEN, RED};

    private Figure[] figures;
    private PlayerID playerID;
    private int startField;
    private Color color;

    public Player(PlayerID playerID, int startField, Color color) {
        this.figures = new Figure[]{
                new Figure(-1, color),
                new Figure(-2, color),
                new Figure(-3, color),
                new Figure(-4, color)
        };
        this.playerID = playerID;
        this.startField = startField;
        this.color = color;
    }

    public Player(Figure[] figures, PlayerID playerID, int startField, Color color) {
        this.figures = figures;
        this.playerID = playerID;
        this.startField = startField;
        this.color = color;
    }

    public Figure[] canFigureMove(int rolledValue){
        // TODO: Implementation
        return null;
    }

    public boolean moveFigure(Figure movableFigure, int rolledValue){
        // TODO: Implementation
        return false;
    }
}
