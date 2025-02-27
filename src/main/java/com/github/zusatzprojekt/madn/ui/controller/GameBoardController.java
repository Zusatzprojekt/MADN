package com.github.zusatzprojekt.madn.ui.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.ColorInput;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    Point2D[] fields;

    Group player = setupGroup();

    @FXML
    public Pane playerPane;
    @FXML
    public Polygon waypoints;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Double[] allPoints = waypoints.getPoints().toArray(new Double[0]);
        fields = new Point2D[allPoints.length / 2];

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Point2D(allPoints[i * 2], allPoints[i * 2 + 1]);
        }

        player.setTranslateX(fields[0].getX() - 25);
        player.setTranslateY(fields[0].getY() - 25);
        playerPane.getChildren().addAll(player);

        AnimationTimer timer = new AnimationTimer() {
            long t = 0;
            int n = 0;

            @Override
            public void handle(long l) {
                t++;
                if (t > 30) {
                    t = 0;
                    player.setTranslateX(fields[n].getX() - 25);
                    player.setTranslateY(fields[n].getY() - 25);
                    n = (n + 1) % 40;
                }
            }
        };
        timer.start();
    }

    private Group setupGroup() {
        Circle circle = new Circle(25);
        circle.setCenterX(25);
        circle.setCenterY(25);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3);
        circle.setFill(new RadialGradient(0, 0.1, circle.getCenterX(), circle.getCenterY(), circle.getRadius(), false, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#FF0000")), new Stop(1, Color.web("#500000"))));

        return new Group(circle);
    }

}
