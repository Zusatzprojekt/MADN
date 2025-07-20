package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.logic.components.MadnFigurePosition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

/**
 * Repräsentiert die Spielfigur eines Spielers in der Logik des Spiels "Mensch ärgere Dich nicht".
 * Verwaltet Position, Zugehörigkeit zum Spieler und Statusinformationen wie Beweglichkeit.
 */
public class MadnFigureL {
    private final ObjectProperty<MadnFigurePosition> figurePosition = new SimpleObjectProperty<>();
    private final BooleanProperty canMoveProp = new SimpleBooleanProperty(false);
    private final MadnFigurePosition basePosition;
    private final MadnPlayerL player;

    /**
     * Konstruktor zur Initialisierung einer Spielfigur.
     *
     * @param placementArea die Platzierungsart der Basisposition
     * @param player        der Spieler, dem die Figur gehört
     * @param fieldIndex    der Index innerhalb der Platzierungsart (z.B. Startfeldnummer)
     */
    public MadnFigureL(MadnFigurePlacement placementArea, MadnPlayerL player, int fieldIndex) {
        basePosition = new MadnFigurePosition(placementArea, fieldIndex);
        figurePosition.setValue(basePosition);
        this.player = player;
    }

    /**
     * Setzt die aktuelle Position der Figur.
     *
     * @param position die neue Position der Figur
     */
    public void setFigurePosition(MadnFigurePosition position) {
        figurePosition.setValue(position);
    }

    /**
     * Gibt die aktuelle Position der Figur zurück.
     *
     * @return die aktuelle {@link MadnFigurePosition} der Figur
     */
    public MadnFigurePosition getFigurePosition() {
        return figurePosition.getValue();
    }

    /**
     * Gibt die observable Property der Figurenposition zurück (für UI-Bindung).
     *
     * @return ein ObservableValue für die Figurenposition
     */
    public ObservableValue<MadnFigurePosition> figurePositionObservable() {
        return figurePosition;
    }

    /**
     * Prüft, ob sich die Figur aktuell bewegen darf.
     *
     * @return {@code true}, wenn die Figur beweglich ist, sonst {@code false}
     */
    public boolean canMove() {
        return canMoveProp.get();
    }

    /**
     * Gibt die observable Property für den Beweglichkeitsstatus zurück (für UI-Bindung).
     *
     * @return ein ObservableBooleanValue, das angibt, ob die Figur beweglich ist
     */
    public ObservableBooleanValue canMoveObservable() {
        return canMoveProp;
    }

    /**
     * Setzt, ob die Figur sich bewegen darf.
     *
     * @param b {@code true}, wenn die Figur beweglich sein soll, sonst {@code false}
     */
    public void setCanMove(boolean b) {
        canMoveProp.setValue(b);
    }

    /**
     * Gibt den Spieler zurück, dem diese Figur gehört.
     *
     * @return der zugehörige {@link MadnPlayerL}
     */
    public MadnPlayerL getPlayer() {
        return player;
    }

    /**
     * Gibt die ursprüngliche Basisposition der Figur zurück.
     * Diese verändert sich im Spielverlauf nicht.
     *
     * @return die Ausgangsposition der Figur
     */
    public MadnFigurePosition getBasePosition() {
        return basePosition;
    }

}
