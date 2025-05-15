package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.ui.UILoader;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MadnHomeV extends MadnFieldContainerV {
    public enum HomeDirection {UP, DOWN, LEFT, RIGHT}
    private final DoubleProperty radius = new SimpleDoubleProperty(100.0);
    private final DoubleProperty spacing = new SimpleDoubleProperty(0.0);
    private final DoubleProperty strokeWidth = new SimpleDoubleProperty(12.0);
    private final ObjectProperty<Paint> fillColor = new SimpleObjectProperty<>(Color.DODGERBLUE);
    private final ObjectProperty<Paint> strokeColor = new SimpleObjectProperty<>(Color.BLACK);
    private final ObjectProperty<HomeDirection> direction = new SimpleObjectProperty<>(HomeDirection.DOWN);

    @FXML
    private Pane fieldContainer;


    // == Constructor ==================================================================================================

    @SuppressWarnings("SuspiciousToArrayCall")
    public MadnHomeV() {

        UILoader.loadComponentFxml("ui/components/gameboard/madn-home-v.fxml", this, this);

        setFields(fieldContainer.getChildren().toArray(MadnFieldV[]::new));

        createBindings();
    }


    // == Bindings =====================================================================================================

    private void createBindings() {

        // Field bindings
        for (MadnFieldV field : getFields()) {
            field.radiusProperty().bind(radius);
            field.fillProperty().bind(fillColor);
            field.strokeProperty().bind(strokeColor);
            field.strokeWidthProperty().bind(strokeWidth);
        }

        // Initial home field direction setup
        setupHomeDirection(direction.getValue());

        // Reorganize fields when home direction is changed
        direction.addListener((observableValue, oldDirection, newDirection) -> {
            setupHomeDirection(newDirection);
        });
    }


    // == Helper methods ===============================================================================================

    private void setupHomeDirection(HomeDirection direction) {
        MadnFieldV[] fields = getFields();

        for (int i = 0; i < fields.length; i++) {
            int n = fields.length - i - 1;

            switch (direction) {
                case UP:
                    fields[i].layoutXProperty().unbind();
                    fields[i].layoutXProperty().setValue(0.0);
                    fields[i].layoutYProperty().bind(radius.multiply(2.0 * n).add(spacing.multiply(n)));
                    break;
                case DOWN:
                    fields[i].layoutXProperty().unbind();
                    fields[i].layoutXProperty().setValue(0.0);
                    fields[i].layoutYProperty().bind(radius.multiply(2.0 * i).add(spacing.multiply(i)));
                    break;
                case LEFT:
                    fields[i].layoutXProperty().bind(radius.multiply(2.0 * n).add(spacing.multiply(n)));
                    fields[i].layoutYProperty().unbind();
                    fields[i].layoutYProperty().setValue(0.0);
                    break;
                case RIGHT:
                    fields[i].layoutXProperty().bind(radius.multiply(2.0 * i).add(spacing.multiply(i)));
                    fields[i].layoutYProperty().unbind();
                    fields[i].layoutYProperty().setValue(0.0);
                    break;
            }
        }
    }

    // == Getter / Setter ==============================================================================================

    public double getRadius() {
        return radius.getValue();
    }

    public void setRadius(double value) {
        radius.setValue(value);
    }

    public double getSpacing() {
        return spacing.getValue();
    }

    public void setSpacing(double value) {
        spacing.setValue(value);
    }

    public double getStrokeWidth() {
        return strokeWidth.getValue();
    }

    public void setStrokeWidth(double value) {
        strokeWidth.setValue(value);
    }

    public Paint getFill() {
        return fillColor.getValue();
    }

    public void setFill(Paint fill) {
        fillColor.setValue(fill);
    }

    public Paint getStroke() {
        return strokeColor.getValue();
    }

    public void setStroke(Paint stroke) {
        strokeColor.setValue(stroke);
    }

    public HomeDirection getHomeDirection() {
        return direction.getValue();
    }

    public void setHomeDirection(HomeDirection direction) {
        this.direction.setValue(direction);
    }


    // == Getter / Setter properties ===================================================================================

    public DoubleProperty radiusProperty() {
        return radius;
    }

    public DoubleProperty spacingProperty() {
        return spacing;
    }

    public DoubleProperty strokeWidthProperty() {
        return strokeWidth;
    }

    public ObjectProperty<Paint> fillProperty() {
        return fillColor;
    }

    public ObjectProperty<Paint> strokeProperty() {
        return strokeColor;
    }

}
