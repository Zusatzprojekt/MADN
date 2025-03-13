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

    public Player(Figure[] figures, int playerID, int startField, Color color) {
        this.figures = figures;
        this.playerID = playerID;
        this.startField = startField;
        this.color = color;

        setupFigure(color);
    }

    private void setupFigure(Color color) {
        // Visuals
        Circle circle = new Circle(20);
        Stop startColor = new Stop(0, color);
        Stop endColor  = new Stop(0, color.deriveColor(0, 1, 0.4, 1));
        RadialGradient gradient = new RadialGradient(0, 0.1, circle.getCenterX(), circle.getCenterY(), circle.getRadius(), false, CycleMethod.NO_CYCLE, startColor, endColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);
        circle.setFill(gradient);

        this.getChildren().add(circle);
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
