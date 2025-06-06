package com.github.zusatzprojekt.madn.ui.components.gameboard;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Abstrakte Erweiterung von MadnFieldContainerV, die zusätzliche visuelle Eigenschaften bietet:
 * Radius, Abstand, Linienbreite sowie Füll- und Randfarbe.
 * Diese Eigenschaften werden an alle enthaltenen Spielfelder gebunden.
 */
public abstract class MadnFieldContainerExtV extends MadnFieldContainerV {
    protected final DoubleProperty radius = new SimpleDoubleProperty(100.0);
    protected final DoubleProperty spacing = new SimpleDoubleProperty(0.0);
    protected final DoubleProperty strokeWidth = new SimpleDoubleProperty(12.0);
    protected final ObjectProperty<Paint> fillColor = new SimpleObjectProperty<>(Color.DODGERBLUE);
    protected final ObjectProperty<Paint> strokeColor = new SimpleObjectProperty<>(Color.BLACK);


    // == Constructor ==================================================================================================

    /**
     * Konstruktor, der ein FXML-Layout lädt und dann Verbindungen für die Felder erstellt.
     *
     * @param fxmlFile Pfad zur FXML-Datei, die das Layout definiert.
     */
    public MadnFieldContainerExtV(String fxmlFile) {
        super(fxmlFile);

        createBindings();
    }


    // == Bindings =====================================================================================================

    /**
     * Verbindet die Felder des Containers mit den Eigenschaften wie Radius, Farbe etc.,
     * damit Änderungen an den Eigenschaften automatisch auf die Felder übertragen werden.
     */
    private void createBindings() {

        // Feld Bindings
        for (MadnFieldV field : getFields()) {
            field.radiusProperty().bind(radius);
            field.fillProperty().bind(fillColor);
            field.strokeProperty().bind(strokeColor);
            field.strokeWidthProperty().bind(strokeWidth);
        }
    }


    // == Getter / Setter ==============================================================================================

    // Radius
    public double getRadius() {
        return radius.getValue();
    }

    public void setRadius(double value) {
        radius.setValue(value);
    }

    // Abstand
    public double getSpacing() {
        return spacing.getValue();
    }

    public void setSpacing(double value) {
        spacing.setValue(value);
    }

    // Linienbreite
    public double getStrokeWidth() {
        return strokeWidth.getValue();
    }

    public void setStrokeWidth(double value) {
        strokeWidth.setValue(value);
    }

    // Füllfarbe
    public Paint getFill() {
        return fillColor.getValue();
    }

    public void setFill(Paint fill) {
        fillColor.setValue(fill);
    }

    // Randfarbe
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
