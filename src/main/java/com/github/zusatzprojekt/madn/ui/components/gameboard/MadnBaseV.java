package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.ui.UILoader;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MadnBaseV extends MadnFieldContainerV {
    private final DoubleProperty radius = new SimpleDoubleProperty(100.0);
    private final DoubleProperty spacing = new SimpleDoubleProperty(0.0);
    private final DoubleProperty strokeWidth = new SimpleDoubleProperty(12.0);
    private final ObjectProperty<Paint> fillColor = new SimpleObjectProperty<>(Color.DODGERBLUE);
    private final ObjectProperty<Paint> strokeColor = new SimpleObjectProperty<>(Color.BLACK);
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(new Font("Arial Bold", 166.66));

    @FXML
    private Pane fieldContainer;
    @FXML
    private StackPane textContainer;
    @FXML
    private Text baseText;


    // == Constructor ==================================================================================================

    @SuppressWarnings("SuspiciousToArrayCall")
    public MadnBaseV() {
        // Load fxml file with ui structure
        UILoader.loadComponentFxml("ui/components/gameboard/madn-base-v.fxml", this, this);

        setFields(fieldContainer.getChildren().toArray(MadnFieldV[]::new));

        // Create bindings to/between the UI elements
        createBindings();
    }


    // == Bindings =====================================================================================================

    private void createBindings() {
        MadnFieldV[] fields = getFields();

        // Field properties
        for (int i = 0; i < fields.length; i++) {
            fields[i].radiusProperty().bind(radius);
            fields[i].fillProperty().bind(fillColor);
            fields[i].strokeProperty().bind(strokeColor);
            fields[i].strokeWidthProperty().bind(strokeWidth);
            fields[i].layoutXProperty().bind(radius.multiply(2.0 * (i % 2)).add(spacing.multiply(i % 2)));
            fields[i].layoutYProperty().bind(radius.multiply(i > 1 ? 2.0 : 0.0).add(spacing.multiply(i > 1 ? 1 : 0)));
        }

        baseText.fontProperty().bind(font);

        textContainer.prefWidthProperty().bind(radius.multiply(4.0).add(spacing));
        textContainer.prefHeightProperty().bind(radius.multiply(4.0).add(spacing));

        radius.addListener((observableValue, oldValue, value) -> {
            font.setValue(new Font("Arial Bold", value.doubleValue() / 0.6));
        });
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
