package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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
    public void enableCanMove(MadnFigureL[] waypoints, Map<MadnPlayerId, MadnFigureL[]> bases, Map<MadnPlayerId, MadnFigureL[]> homes) {
        int roll = lastRoll.getValue();
        MadnFigureL[] baseFigures = Arrays.stream(bases.get(playerID)).filter(Objects::nonNull).toArray(MadnFigureL[]::new);
        MadnFigureL[] homeFigures = Arrays.stream(homes.get(playerID)).filter(Objects::nonNull).toArray(MadnFigureL[]::new);
        MadnFigureL[] wayFigures = Arrays.stream(waypoints)
                .filter(Objects::nonNull)
                .filter(figure -> figure.getPlayer().getPlayerID() == playerID)
                .filter(figure -> figure.getFigurePosition().getFieldIndex() != figure.getPlayer().getStartIndex())
                .toArray(MadnFigureL[]::new);
        MadnFigureL startFigure = Arrays.stream(waypoints)
                .filter(Objects::nonNull)
                .filter(figure -> figure.getPlayer().getPlayerID() == playerID)
                .filter(figure -> figure.getFigurePosition().getFieldIndex() == figure.getPlayer().getStartIndex())
                .findFirst().orElse(null);


        if (startFigure != null) {
            startFigure.setCanMove(figureMovementCheck(startFigure, waypoints, homes.get(playerID), roll));

            if (baseFigures.length > 0) {
                return;
            }

        } else if (roll == 6 && baseFigures.length > 0) {
            Arrays.stream(baseFigures).forEach(figure -> figure.setCanMove(true));
            return;
        }

        Arrays.stream(homeFigures).forEach(figure -> {
            int oldIndex = figure.getFigurePosition().getFieldIndex();
            figure.setCanMove(figureHomeMovementCheck(oldIndex, oldIndex + roll, homes.get(playerID)));
        });

        Arrays.stream(wayFigures).forEach(figure -> {
            figure.setCanMove(figureMovementCheck(figure, waypoints, homes.get(playerID), roll));
        });


        // TODO rewrite logic
    }

    private boolean figureMovementCheck(MadnFigureL figure, MadnFigureL[] waypoints, MadnFigureL[] home, int roll) {
        int figIndex = figure.getFigurePosition().getFieldIndex();
        int newFigIndex = figIndex + roll;

        if (figIndex <= getHomeIndex() && newFigIndex > getHomeIndex()) {
            return figureHomeMovementCheck(-1, newFigIndex - getHomeIndex() - 1, home);

        } else {
            return waypoints[newFigIndex % waypoints.length] == null || waypoints[newFigIndex % waypoints.length].getPlayer().getPlayerID() != playerID;
        }
    }

    private boolean figureHomeMovementCheck(int oldPosition, int newPosition, MadnFigureL[] home) {

        if (newPosition >= home.length) {
            return false;
        }

        for (int i = oldPosition + 1; i < newPosition + 1; i++) {
            if (home[i] != null) {
                return false;
            }
        }

        return true;
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

