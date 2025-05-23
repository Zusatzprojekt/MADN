package com.github.zusatzprojekt.madn.logic.components;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;

public class MadnFigurePosition {
        private final MadnFigurePlacement figurePlacement;
        private final int fieldIndex;

        public MadnFigurePosition(MadnFigurePlacement placement, int index) {
            figurePlacement = placement;
            fieldIndex = index;
        }

        public MadnFigurePlacement getFigurePlacement() {
            return figurePlacement;
        }

        public int getFieldIndex() {
            return fieldIndex;
        }

}
