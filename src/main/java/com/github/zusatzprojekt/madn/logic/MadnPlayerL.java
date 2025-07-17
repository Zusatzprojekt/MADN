package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;

import java.util.Arrays;
import java.util.Map;

public class MadnPlayerL {
    private final MadnPlayerId playerID;
    private final IntegerProperty lastRoll = new SimpleIntegerProperty(0);
    private final MadnFigureL[] figures;
    private final int startIndex;
    private int finishedPos = 0;


    // == Constructor ==================================================================================================

    public MadnPlayerL(MadnPlayerId playerID, int startIndex) {
        this.playerID = playerID;
        this.startIndex = startIndex;

        figures = new MadnFigureL[] {
                new MadnFigureL(MadnFigurePlacement.BASE, this, 0),
                new MadnFigureL(MadnFigurePlacement.BASE, this, 1),
                new MadnFigureL(MadnFigurePlacement.BASE, this, 2),
                new MadnFigureL(MadnFigurePlacement.BASE, this, 3)
        };
    }

    // TODO: Movement-Überprüfung + Fertigstellen
    public void enableCanMove(MadnFigureL[] waypoints, Map<MadnPlayerId, MadnFigureL[]> homes) {
        int roll = lastRoll.getValue();

        if (roll == 6 && (waypoints[startIndex] == null || waypoints[startIndex].getPlayer().getPlayerID() != playerID)) {
            MadnFigureL[] baseFigures = Arrays.stream(figures).filter(figure -> figure.getFigurePosition().getFigurePlacement() == MadnFigurePlacement.BASE).toArray(MadnFigureL[]::new);

            for (MadnFigureL figure : baseFigures) {
                figure.setCanMove(true);
            }
        }

        MadnFigureL[] homeFigures = Arrays.stream(figures).filter(figure -> figure.getFigurePosition().getFigurePlacement() == MadnFigurePlacement.HOME).toArray(MadnFigureL[]::new);

        if (homeFigures.length > 0) {
            MadnFigureL[] home = homes.get(playerID);

            for (MadnFigureL figure : homeFigures) {
                int newFigIndex = figure.getFigurePosition().getFieldIndex() + roll;

                figure.setCanMove(newFigIndex < home.length && home[newFigIndex] == null);
            }
        }

        MadnFigureL[] wayFigures = Arrays.stream(figures).filter(figure -> figure.getFigurePosition().getFigurePlacement() == MadnFigurePlacement.WAYPOINTS).toArray(MadnFigureL[]::new);

        if (wayFigures.length > 0) {

            for (MadnFigureL figure : wayFigures) {
                int newFigIndex = figure.getFigurePosition().getFieldIndex() + roll;

                if (figure.getFigurePosition().getFieldIndex() <= getHomeIndex() && newFigIndex > getHomeIndex()) {
                    MadnFigureL[] home = homes.get(playerID);
                    boolean validMove = true;

                    int inHomeIndex = newFigIndex - getHomeIndex() - 1;

                    if (inHomeIndex < home.length && home[inHomeIndex] == null) {

                        for (int i = 0; i < inHomeIndex; i++) {
                            if (home[i] != null) {
                                validMove = false;
                                break;
                            }
                        }

                        figure.setCanMove(validMove);

                    } else {
                        figure.setCanMove(false);
                    }






                } else {
                    figure.setCanMove(waypoints[newFigIndex % waypoints.length] == null || waypoints[newFigIndex % waypoints.length].getPlayer().getPlayerID() != playerID);
                }
            }
        }

        if (waypoints[startIndex] != null && waypoints[startIndex].canMove()) {

            for (MadnFigureL figure : figures) {
                figure.setCanMove(false);
            }

            waypoints[startIndex].setCanMove(true);
        }
    }

    public void disableCanMove() {
        for (MadnFigureL figure : figures) {
            figure.setCanMove(false);
        }
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

    public int getStartIndex() {
        return startIndex;
    }

    public int getHomeIndex() {
        return (startIndex + 39) % 40;
    }

}

