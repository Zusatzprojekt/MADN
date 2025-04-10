package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.logic.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

import java.io.IOException;

public class MadnField extends StackPane {
    public enum FieldType {NORMAL, CORNER, BASE, HOME, START};
    public enum FieldDirection {UP, DOWN, RIGHT, LEFT, NONE};
    private FieldType fieldType = FieldType.NORMAL;
    private FieldDirection fieldDirection = FieldDirection.NONE;
    private Translate translate = new Translate();
    private double centerX, centerY, layoutWidth, layoutHeight;
    private Player player;

    @FXML
    private Circle circle;
    @FXML
    private Text text;

    public MadnField() throws IOException {
        // Load game-board.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("madn-field.fxml"));

        // Set this class as root and controller for use as custom component
        loader.setRoot(this);
        loader.setController(this);

        loader.load();

        // Setup components
        this.getTransforms().add(translate);
        setupCircle();
        setupText();
    }

    private void setupCircle() {
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(Color.web("#000000"));

        circle.layoutBoundsProperty().addListener((observableValue, oldBounds, bounds) -> updateLayout());
    }

    private void setupText() {
        text.setFill(Color.web("#000000"));

        text.layoutBoundsProperty().addListener((observableValue, oldBounds, bounds) -> updateLayout());
    }

    private void updateLayout() {
        updateLayoutX();
        updateLayoutY();
    }

    private void updateLayoutX() {
        layoutWidth = Math.max(circle.getLayoutBounds().getWidth(), text.getLayoutBounds().getWidth());
        setLayoutX(centerX - (layoutWidth / 2));
    }

    private void updateLayoutY() {
        layoutHeight = Math.max(circle.getLayoutBounds().getHeight(), text.getLayoutBounds().getHeight());
        setLayoutY(centerY - (layoutHeight / 2));
    }

    // MadnField properties

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldDirection(FieldDirection fieldDirection) {
        this.fieldDirection = fieldDirection;
    }

    public FieldDirection getFieldDirection() {
        return fieldDirection;
    }

    public Point2D getCenterPoint() {
        return new Point2D(centerX, centerY);
    }

    // Circle properties

    public void setCenterX(double centerX) {
        this.centerX = centerX;
        updateLayoutX();
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
        updateLayoutY();
    }

    public double getCenterY() {
        return centerY;
    }

    public void setFill(Paint paint) {
        circle.setFill(paint);
    }

    public Paint getFill() {
        return circle.getFill();
    }

    public void setRadius(double radius) {
        circle.setRadius(radius);
    }

    public double getRadius() {
        return circle.getRadius();
    }

    public void setStrokeWidth(double strokeWidth) {
        circle.setStrokeWidth(strokeWidth);
    }

    public double getStrokeWidth() {
        return circle.getStrokeWidth();
    }

    // Text properties

    public void setText(String value) {
        text.setText(value);
    }

    public String getText() {
        return text.getText();
    }

    public void setFont(Font font) {
        text.setFont(font);
    }

    public Font getFont() {
        return text.getFont();
    }

    // Player on field

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
