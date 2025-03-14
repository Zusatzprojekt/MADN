package com.github.zusatzprojekt.madn.logic;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class Figure extends Group {
    private int currentField;
    private boolean onStart;

    public Figure(int currentField) {
        this.currentField = currentField;
        this.onStart = false;
    }

    public Figure(int currentField, Color color) {
        this.currentField = currentField;
        this.onStart = false;

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
