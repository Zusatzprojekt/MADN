package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.ui.UIManager;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class MadnFieldV extends Group {
    private final DoubleProperty radius = new SimpleDoubleProperty(100.0);
    private final DoubleProperty strokeWidth = new SimpleDoubleProperty(12.0);
    private final ObjectProperty<Paint> fillColor = new SimpleObjectProperty<>(Color.DODGERBLUE);
    private final ObjectProperty<Paint> strokeColor = new SimpleObjectProperty<>(Color.BLACK);
    private final ReadOnlyDoubleProperty centerX = createDoubleProperty(
            getParent() != null ? getParent().layoutXProperty().add(layoutXProperty()).add(radius) : layoutXProperty().add(radius)
    );
    private final ReadOnlyDoubleProperty centerY = createDoubleProperty(
            getParent() != null ? getParent().layoutYProperty().add(layoutYProperty()).add(radius) : layoutYProperty().add(radius)
    );
    private boolean cornerField = false;

    @FXML
    private Circle circle;


    // == Constructor ==================================================================================================

    public MadnFieldV() {
        // Load fxml file with ui structure
        UIManager.loadComponentFxml("ui/components/gameboard/madn-field-v.fxml", this, this);

        // Create bindings to/between the UI elements
        createBindings();
    }


    // == Bindings =====================================================================================================

    private void createBindings() {
        circle.radiusProperty().bind(radius);
        circle.layoutXProperty().bind(radius);
        circle.layoutYProperty().bind(radius);
        circle.fillProperty().bind(fillColor);
        circle.strokeProperty().bind(strokeColor);
        circle.strokeWidthProperty().bind(strokeWidth);
    }


    // == Helper methods ===============================================================================================

    private DoubleProperty createDoubleProperty(ObservableValue<? extends Number> binding) {
        DoubleProperty property = new SimpleDoubleProperty();
        property.bind(binding);

        return property;
    }


    // == Getter / Setter ==============================================================================================

    public double getRadius() {
        return radius.getValue();
    }

    public void setRadius(double value) {
        radius.setValue(value);
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

    public boolean isCornerField() {
        return cornerField;
    }

    public void setCornerField(boolean isCorner) {
        cornerField = isCorner;
    }

    public double getCenterX() {
        return centerX.getValue();
    }

    public double getCenterY() {
        return centerY.getValue();
    }


    // == Getter / Setter properties ===================================================================================

    public DoubleProperty radiusProperty() {
        return radius;
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

    public ReadOnlyDoubleProperty centerXProperty() {
        return centerX;
    }

    public ReadOnlyDoubleProperty centerYProperty() {
        return centerY;
    }

}
