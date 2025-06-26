package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;

/**
 * Abstrakte Klasse, die ein Container für mehrere Spielfelder (MadnFieldV) ist.
 * Sie erweitert JavaFXs Group, um als Knoten im Scene-Graph verwendet werden zu können.
 */
@SuppressWarnings("SuspiciousToArrayCall")
public abstract class MadnFieldContainerV extends Group {
    private final MadnFieldV[] fields;

    @FXML
    private Parent fieldContainer;


    // == Constructor ==================================================================================================
    /**
     * Konstruktor lädt ein FXML-Layout und initialisiert die Spielfelder.
     * @param fxmlFile Der Pfad zur FXML-Datei, die dieses Layout beschreibt.
     */
    public MadnFieldContainerV(String fxmlFile) {
        AppManager.loadComponentFxml(fxmlFile, this, this);

        fields = initFields();
    }


    // == Helper methods ===============================================================================================
    /**
     * Initialisiert die Spielfelder aus den Kindern des fieldContainer.
     * @return Ein Array aller gefundenen MadnFieldV-Knoten
     */
    private MadnFieldV[] initFields() {
        MadnFieldV[] madnFields = fieldContainer.getChildrenUnmodifiable().toArray(MadnFieldV[]::new);

        for (MadnFieldV field : madnFields) {
            field.setContainer(this);
        }

        return madnFields;
    }


    // == Getter / Setter ==============================================================================================
    /**
     * Gibt das Array aller Spielfelder zurück.
     * @return Array von MadnFieldV
     */
    public MadnFieldV[] getFields() {
        return fields;
    }

}

