package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;

public class MadnPlayerL {
    private final MadnPlayerId playerID;
    private final IntegerProperty lastRoll = new SimpleIntegerProperty(0);
    private final MadnFigureL[] figures;
    private int finishedPos = 0;


    // == Constructor ==================================================================================================

    public MadnPlayerL(MadnPlayerId playerID) {
        this.playerID = playerID;

        figures = new MadnFigureL[] {
                new MadnFigureL(MadnFigurePlacement.BASE, 0),
                new MadnFigureL(MadnFigurePlacement.BASE, 1),
                new MadnFigureL(MadnFigurePlacement.BASE, 2),
                new MadnFigureL(MadnFigurePlacement.BASE, 3)
        };
    }


    // == Getter / Setter ==============================================================================================

    public MadnPlayerId getPlayerID() {
        return playerID;
    }

    public int getLastRoll() {
        return lastRoll.getValue();
    }

    public void setLastRoll(int lastRoll) {
        this.lastRoll.setValue(lastRoll);
    }

    public ObservableIntegerValue lastRollObservable() {
        return lastRoll;
    }

    public MadnFigureL[] getFigures() {
        return figures;
    }

    public boolean isFinished() {
        return finishedPos > 0;
    }

    public int getFinishedPos() {
        return finishedPos;
    }

    public void setFinishedPos(int value) {
        finishedPos = value;
    }

}

