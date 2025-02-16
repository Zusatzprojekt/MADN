package com.github.zusatzprojekt.madn.logic;

import javafx.scene.paint.Color;

public class Player {
    private Figure[] figures;
    private int playerID;
    private int startField;
    private Color color;

    public Player(Figure[] figures, int playerID, int startField, Color color) {
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
