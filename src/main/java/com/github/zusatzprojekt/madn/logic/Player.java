package com.github.zusatzprojekt.madn.logic;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class Player extends Group {
    private Figure[] figures;
    private int playerID;
    private int startField;
    private Color color;

    // Visuals
    Circle circle;
    Stop startColor;
//    Stop endColor; = new Stop(0, color);
//    RadialGradient gradient; = new RadialGradient(0, 0.1, circle.getCenterX(), circle.getCenterY(), circle.getRadius(), false, CycleMethod.NO_CYCLE, startColor, endColor);

    public Player(Figure[] figures, int playerID, int startField, Color color) {
        this.figures = figures;
        this.playerID = playerID;
        this.startField = startField;
        this.color = color;

        // Visuals
        circle = new Circle(20);
        startColor = new Stop(0, color);
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
