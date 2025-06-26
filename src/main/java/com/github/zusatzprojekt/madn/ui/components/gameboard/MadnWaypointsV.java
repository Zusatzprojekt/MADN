package com.github.zusatzprojekt.madn.ui.components.gameboard;

/**
 * Repräsentiert den Hauptweg (die 40 Felder im Kreis) im "Mensch ärgere dich nicht"-Spiel.
 * Diese Klasse ist eine spezialisierte Version von {@link MadnFieldContainerV},
 * welche ein Container für mehrere Spielfelder ist.
 */
public class MadnWaypointsV extends MadnFieldContainerV {


    // == Constructor ==================================================================================================

    /**
     * Konstruktor lädt die zugehörige FXML-Datei, in der die Wegfelder definiert sind.
     */
    public MadnWaypointsV() {
        super("ui/components/gameboard/madn-waypoints-v.fxml");
    }

}