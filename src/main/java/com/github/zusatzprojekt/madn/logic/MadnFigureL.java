package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.logic.components.MadnFigurePosition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

public class MadnFigureL {
    //TODO: Checki if worki
    private final ObjectProperty<MadnFigurePosition> figurePosition = new SimpleObjectProperty<>();
    private final BooleanProperty canMove = new SimpleBooleanProperty(false);


    public MadnFigureL(MadnFigurePlacement placementArea, int fieldIndex) {
        figurePosition.setValue(new MadnFigurePosition(placementArea, fieldIndex));
    }

    public ObservableValue<MadnFigurePosition> figurePositionObservable() {
        return figurePosition;
    }

    public boolean isCanMove() {
        return canMove.get();
    }

    public ObservableBooleanValue canMoveObservable() {
        return canMove;
    }

}
