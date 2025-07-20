package com.github.zusatzprojekt.madn.logic.components;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;

/**
 * Repräsentiert die Position einer Figur im Spiel "Mensch ärgere Dich nicht".
 * Eine Position besteht aus einer Platzierungsart ({@link MadnFigurePlacement})
 * und einem optionalen Feldindex.
 *
 * <p>Beispielhafte Platzierungen sind: in der Basis, auf dem Spielfeld, im Zielbereich.</p>
 */
public class MadnFigurePosition {
        private final MadnFigurePlacement figurePlacement;
        private final int fieldIndex;

    /**
     * Konstruktor zur Initialisierung einer Figurenposition mit Platzierungsart und Feldindex.
     *
     * @param placement die Platzierungsart der Figur
     * @param index     der Index des Feldes
     */
        public MadnFigurePosition(MadnFigurePlacement placement, int index) {
            figurePlacement = placement;
            fieldIndex = index;
        }

    /**
     * Gibt die Platzierungsart der Figur zurück.
     *
     * @return die aktuelle Platzierung der Figur
     */
        public MadnFigurePlacement getFigurePlacement() {
            return figurePlacement;
        }

    /**
     * Gibt den Index des Feldes zurück, auf dem sich die Figur befindet.
     *
     * @return der Feldindex der Figur
     */
        public int getFieldIndex() {
            return fieldIndex;
        }

}
