package com.github.zusatzprojekt.madn.ui.components.gameboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Abstrakte Erweiterung von MadnFieldContainerV, die zusätzliche visuelle Eigenschaften bietet:
 * Radius, Abstand, Linienbreite sowie Füll- und Randfarbe.
 * Diese Eigenschaften werden an alle enthaltenen Spielfelder gebunden.
 */
public abstract class MadnFieldContainerExtV extends MadnFieldContainerV {
    private final ObjectProperty<Paint> fillColor = new SimpleObjectProperty<>(Color.DODGERBLUE);


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
     * Verbindet die Felder des Containers mit den Eigenschaften wie Farbe etc.,
     * damit Änderungen an den Eigenschaften automatisch auf die Felder übertragen werden.
     */
    private void createBindings() {

        // Feld Bindings
        for (MadnFieldV field : getFields()) {
            field.fillProperty().bind(fillColor);
        }
    }


    // == Getter / Setter ==============================================================================================

    // Füllfarbe
    public Paint getFill() {
        return fillColor.getValue();
    }

    public void setFill(Paint fill) {
        fillColor.setValue(fill);
    }


    // == Getter / Setter properties ===================================================================================

    public ObjectProperty<Paint> fillProperty() {
        return fillColor;
    }

}
