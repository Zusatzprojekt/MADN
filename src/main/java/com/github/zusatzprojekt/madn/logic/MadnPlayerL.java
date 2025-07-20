package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Repräsentiert einen Spieler in der Logik des "Mensch ärgere Dich nicht"-Spiels.
 * Enthält Spielfiguren, Würfelergebnisse, Start- und Endpositionen sowie Spiellogik zur Bewegungsberechnung.
 */
public class MadnPlayerL {
    private final MadnPlayerId playerID;
    private final IntegerProperty lastRoll = new SimpleIntegerProperty(0);
    private final MadnFigureL[] figures;
    private final int startIndex;
    private int finishedPos = 0;


    // == Constructor ==================================================================================================

    /**
     * Konstruktor. Erstellt einen neuen Spieler mit eindeutiger ID und Startindex.
     *
     * @param playerID   die ID des Spielers
     * @param startIndex das Startfeld auf dem Spielfeld (0–39)
     */
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

    /**
     * Aktiviert die {@code canMove}-Eigenschaft für alle Figuren, die sich in der aktuellen Spielsituation bewegen dürfen.
     *
     * @param waypoints das Spielfeld mit allen Positionen (Laufstrecke)
     * @param bases     alle Spielfiguren in der Basis pro Spieler
     * @param homes     alle Spielfiguren im Haus (Ziel) pro Spieler
     */
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

            if (baseFigures.length > 0 && startFigure.canMove()) {
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
    }

    /**
     * Prüft, ob eine Figur aus dem Spielfeld weiterbewegt werden kann (einschließlich Eintritt in das Haus).
     *
     * @param figure    die zu prüfende Figur
     * @param waypoints das Spielfeld (Laufstrecke)
     * @param home      das Haus-Feld-Array des Spielers
     * @param roll      die aktuelle Würfelzahl
     * @return {@code true}, wenn die Bewegung gültig ist; sonst {@code false}
     */
    private boolean figureMovementCheck(MadnFigureL figure, MadnFigureL[] waypoints, MadnFigureL[] home, int roll) {
        int figIndex = figure.getFigurePosition().getFieldIndex();
        int newFigIndex = figIndex + roll;

        if (figIndex <= getHomeIndex() && newFigIndex > getHomeIndex()) {
            return figureHomeMovementCheck(-1, newFigIndex - getHomeIndex() - 1, home);

        } else {
            return waypoints[newFigIndex % waypoints.length] == null || waypoints[newFigIndex % waypoints.length].getPlayer().getPlayerID() != playerID;
        }
    }

    /**
     * Prüft, ob eine Bewegung im Haus möglich ist (kein Überspringen von belegten Feldern möglich).
     *
     * @param oldPosition vorherige Position
     * @param newPosition gewünschte Zielposition
     * @param home        Hausfelder des Spielers
     * @return {@code true}, wenn alle Felder frei sind; sonst {@code false}
     */
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

    /**
     * Deaktiviert die Bewegungsmöglichkeit für alle Spielfiguren dieses Spielers.
     */
    public void disableCanMove() {
        for (MadnFigureL figure : figures) {
            figure.setCanMove(false);
        }
    }

    // == Getter / Setter ==============================================================================================

    /**
     * Gibt die Spieler-ID zurück.
     *
     * @return die ID des Spielers
     */
    public MadnPlayerId getPlayerID() {
        return playerID;
    }

    /**
     * Gibt die zuletzt gewürfelte Augenzahl zurück.
     *
     * @return die letzte Würfelzahl
     */
    public int getLastRoll() {
        return lastRoll.getValue();
    }

    /**
     * Setzt den letzten Würfelwert.
     *
     * @param lastRoll der neue Würfelwert
     */
    public void setLastRoll(int lastRoll) {
        this.lastRoll.setValue(lastRoll);
    }

    /**
     * Gibt die ObservableProperty für den Würfelwert zurück.
     *
     * @return Observable für letzte Würfelzahl
     */
    public ObservableIntegerValue lastRollObservable() {
        return lastRoll;
    }

    /**
     * Gibt alle Spielfiguren des Spielers zurück.
     *
     * @return ein Array mit 4 Spielfiguren
     */
    public MadnFigureL[] getFigures() {
        return figures;
    }

    /**
     * Prüft, ob der Spieler das Spiel bereits beendet hat.
     *
     * @return {@code true}, wenn der Spieler fertig ist; sonst {@code false}
     */
    public boolean isFinished() {
        return finishedPos > 0;
    }

    /**
     * Gibt die Platzierung des Spielers zurück (1 = erster Platz, 2 = zweiter, …).
     *
     * @return die Platzierung
     */
    public int getFinishedPos() {
        return finishedPos;
    }

    /**
     * Setzt die Platzierung des Spielers nach Spielende.
     *
     * @param value Platzierung (1–4)
     */
    public void setFinishedPos(int value) {
        finishedPos = value;
    }

    /**
     * Gibt den Startindex des Spielers im Spielfeld zurück.
     *
     * @return der Startfeldindex (0–39)
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Gibt den Index des Eingangs zum Hausbereich dieses Spielers zurück.
     *
     * @return der Home-Index (gerechnet relativ zu {@code startIndex + 39})
     */
    public int getHomeIndex() {
        return (startIndex + 39) % 40;
    }

}

