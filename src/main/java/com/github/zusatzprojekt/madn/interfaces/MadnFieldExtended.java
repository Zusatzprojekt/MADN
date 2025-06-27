package com.github.zusatzprojekt.madn.interfaces;

import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.enums.MadnFieldType;

/**
 * Interface zur Erweiterung von Spielfeldern im Spiel "Mensch Ärgere Dich Nicht"
 * um zusätzliche Informationen wie Typ und Spielerzuordnung.
 *
 * Dieses Interface eignet sich z.B. für Spielfelder, die spezielle Bedeutung haben
 * (Start-, Endfelder) und einem bestimmten Spieler zugeordnet sind.
 */
public interface MadnFieldExtended {

    /**
     * Gibt den Typ des Spielfelds zurück.
     *
     * @return Der spezielle Feldtyp (z.B. START, END).
     */
    MadnFieldType getFieldType();

    /**
     * Gibt zurück, welchem Spieler dieses Feld zugeordnet ist.
     *
     * @return Die Spieler-ID (z.B. RED, BLUE).
     */
    MadnPlayerId getFieldAssignment();

}
