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
    //TODO: Testen
    private final ObjectProperty<MadnFigurePosition> figurePosition = new SimpleObjectProperty<>();
    private final BooleanProperty canMoveProp = new SimpleBooleanProperty(false);
    private final MadnFigurePosition basePosition;
    private final MadnPlayerL player;

    public MadnFigureL(MadnFigurePlacement placementArea, MadnPlayerL player, int fieldIndex) {
        basePosition = new MadnFigurePosition(placementArea, fieldIndex);
        figurePosition.setValue(basePosition);
        this.player = player;
    }

    public void setFigurePosition(MadnFigurePosition position) {
        figurePosition.setValue(position);
    }

    public ObservableValue<MadnFigurePosition> figurePositionObservable() {
        return figurePosition;
    }

    public boolean canMove() {
        return canMoveProp.get();
    }

    public ObservableBooleanValue canMoveObservable() {
        return canMoveProp;
    }

    public void setCanMove(boolean b) {
        canMoveProp.setValue(b);
    }

    public MadnPlayerL getPlayer() {
        return player;
    }

    public MadnFigurePosition getBasePosition() {
        return basePosition;
    }

}
