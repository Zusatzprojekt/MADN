package com.github.zusatzprojekt.madn.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    private Point2D[] runway = new Point2D[40];

    @FXML
    public Pane playerPane;
    @FXML
    public Polygon waypoints;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        double[] points = waypoints.getPoints().toArray(double[].class);

//        System.out.println(Arrays.toString(points));
    }
}
