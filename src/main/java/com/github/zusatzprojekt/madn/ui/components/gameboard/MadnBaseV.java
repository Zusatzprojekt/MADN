package com.github.zusatzprojekt.madn.ui.components.gameboard;

/**
 * MadnBaseV repräsentiert die Felder der Parkpositionen auf dem Spielbrett.
 * Die Klasse ist UI-bezogen und basiert auf einem FXML-Layout, das sie mit der Superklasse lädt.
 */
public class MadnBaseV extends MadnFieldContainerExtV {

    /**
     * Konstruktor lädt das zugehörige FXML und richtet Verbindungen ein.
     */
    public MadnBaseV() {
        super("ui/components/gameboard/madn-base-v.fxml");
    }

}
