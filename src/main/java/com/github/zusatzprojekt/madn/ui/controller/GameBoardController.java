package com.github.zusatzprojekt.madn.ui.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    Point2D[] fields;
    Point2D[] blueBase = new Point2D[]{new Point2D(85, 850), new Point2D(85, 765), new Point2D(0, 765), new Point2D(0,850)};
    Point2D[] yellowBase = new Point2D[]{new Point2D(0, 85), new Point2D(85, 85), new Point2D(85, 0), new Point2D(0, 0)};
    Point2D[] greenBase = new Point2D[]{new Point2D(765, 0), new Point2D(765, 85), new Point2D(850, 85), new Point2D(850, 0)};
    Point2D[] redBase = new Point2D[]{new Point2D(765, 850), new Point2D(765, 765), new Point2D(850, 765), new Point2D(850, 850)};

    Group player = setupGroup(new Stop(0, Color.web("#FF0000")), new Stop(1, Color.web("#660000")));

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

        player.setTranslateX(redBase[0].getX() - 20);
        player.setTranslateY(redBase[0].getY() - 20);
        playerPane.getChildren().addAll(player);

//        AnimationTimer timer = new AnimationTimer() {
//            long t = 0;
//            int n = 0;
//
//            @Override
//            public void handle(long l) {
//                t++;
//                if (t > 30) {
//                    t = 0;
//                    player.setTranslateX(redBase[n].getX() - 20);
//                    player.setTranslateY(redBase[n].getY() - 20);
//                    n = (n + 1) % 4;
//                }
//            }
//        };
//        timer.start();

        Path path = new Path();
        path.getElements().add(new MoveTo(fields[0].getX(), fields[0].getY()));
        for (int i = 1; i < fields.length; i++) {
            path.getElements().add(new LineTo(fields[i].getX(), fields[i].getY()));
        }

        PathTransition transition = new PathTransition(Duration.seconds(10), path, player);
        transition.setInterpolator(Interpolator.LINEAR);

        transition.play();
    }

    private Group setupGroup(Stop startColor, Stop stopColor) {
        Circle circle = new Circle(20);
        circle.setCenterX(20);
        circle.setCenterY(20);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);
        circle.setFill(new RadialGradient(0, 0.1, circle.getCenterX(), circle.getCenterY(), circle.getRadius(), false, CycleMethod.NO_CYCLE, startColor, stopColor));

        return new Group(circle);
    }

}
