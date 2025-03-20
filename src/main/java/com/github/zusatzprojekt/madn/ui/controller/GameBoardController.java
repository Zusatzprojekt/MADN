package com.github.zusatzprojekt.madn.ui.controller;

import com.github.zusatzprojekt.madn.logic.Player;
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
    Point2D[] blueHome = new Point2D[]{new Point2D(425, 765), new Point2D(425, 680), new Point2D(425, 595), new Point2D(425,510)};
    Point2D[] yellowHome = new Point2D[]{new Point2D(85, 425), new Point2D(170, 425), new Point2D(255, 425), new Point2D(340, 425)};
    Point2D[] greenHome = new Point2D[]{new Point2D(425, 85), new Point2D(425, 170), new Point2D(425, 255), new Point2D(425, 340)};
    Point2D[] redHome = new Point2D[]{new Point2D(510, 425), new Point2D(595, 425), new Point2D(680, 425), new Point2D(765, 425)};

    Group player = setupGroup(new Stop(0, Color.web("#FF0000")), new Stop(1, /*Color.web("#660000")*/Color.web("#FF0000").deriveColor(0, 1, 0.4, 1)));

    Player[] players = new Player[]{new Player(Player.PlayerID.BLUE, 10, Color.BLUE)};

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

//        player.setTranslateX(redBase[0].getX() - 20);
//        player.setTranslateY(redBase[0].getY() - 20);
//        playerPane.getChildren().addAll(player);

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
        path.getElements().add(new MoveTo(redHome[0].getX(), redHome[0].getY()));
        for (int i = 1; i < redHome.length; i++) {
            path.getElements().add(new LineTo(redHome[i].getX(), redHome[i].getY()));
        }

//        player.getViewOrder();


        PathTransition transition = new PathTransition(Duration.seconds(3), path, player);
        transition.setInterpolator(Interpolator.EASE_BOTH);

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
