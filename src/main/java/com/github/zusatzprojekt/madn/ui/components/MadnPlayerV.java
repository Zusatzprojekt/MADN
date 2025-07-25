package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.logic.MadnFigureL;
import com.github.zusatzprojekt.madn.logic.MadnPlayerL;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Visuelle Repräsentation eines Spielers im Spiel "Mensch ärgere dich nicht".
 * <p>
 * Diese Klasse verbindet die logische Spielerstruktur ({@link MadnPlayerL})
 * mit der grafischen Darstellung auf dem Spielbrett.
 * Enthält Referenzen zu Basis-, Home- und Wegpunkt-Feldern sowie allen Spielfiguren des Spielers.
 */
public class MadnPlayerV {
    private final IntegerProperty lastRoll = new SimpleIntegerProperty(0);
    private final MadnFieldContainerV base, home, waypoints;
    private final MadnFigureV[] figures;
    private final MadnPlayerId playerId;
    private final MadnBoardV board;


    // == Constructor ==================================================================================================

    /**
     * Erstellt eine visuelle Spielerinstanz und initialisiert alle Felder und Spielfiguren.
     *
     * @param playerL Die logische Repräsentation des Spielers.
     * @param board   Das Spielbrett, auf dem der Spieler aktiv ist.
     */
    public MadnPlayerV(MadnPlayerL playerL, MadnBoardV board) {
        this.board = board;

        playerId = playerL.getPlayerID();

        base = getBase(playerId);

        home = getHome(playerId);

        waypoints = board.getWaypointContainer();

        figures = initFigures(playerL, board);

        board.getChildren().addAll(figures);
        initBindings(playerL);
    }

    /**
     * Initialisiert alle vier Spielfiguren des Spielers und verbindet Maus-Events mit dem Board.
     *
     * @param playerL Logische Spielerinstanz.
     * @param board   Das zugehörige Spielbrett.
     * @return Ein Array mit den erstellten {@link MadnFigureV}-Objekten.
     */
    private MadnFigureV[] initFigures(MadnPlayerL playerL, MadnBoardV board) {
        MadnFigureL[] figuresL = playerL.getFigures();
        MadnFigureV[] figuresV = new MadnFigureV[figuresL.length];

        for (int i = 0; i < figuresV.length; i++) {
            figuresV[i] = new MadnFigureV(this, figuresL[i]);
            figuresV[i].mouseEnterEventProperty().bind(board.activateHighlightEventProperty());
            figuresV[i].mouseExitEventProperty().bind(board.deactivateHighlightEventProperty());
        }

        return figuresV;
    }


    // == Bindings =====================================================================================================

    /**
     * Bindet die letzte Würfelzahl an die logische Spielerinstanz.
     *
     * @param playerL Logischer Spieler.
     */
    private void initBindings(MadnPlayerL playerL) {
        lastRoll.bind(playerL.lastRollObservable());
    }


    // == Helper methods ===============================================================================================

    /**
     * Liefert den passenden Basis-Container (Base) für die Spielerfarbe.
     *
     * @param playerId Spieler-ID.
     * @return Container für Startfelder.
     */
    private MadnFieldContainerV getBase(MadnPlayerId playerId) {

        return switch (playerId) {
            case BLUE -> board.getBaseContainerBlue();
            case YELLOW -> board.getBaseContainerYellow();
            case GREEN -> board.getBaseContainerGreen();
            case RED -> board.getBaseContainerRed();
            case NONE -> null;
        };
    }

    /**
     * Liefert den passenden Ziel-Container für die Spielerfarbe.
     *
     * @param playerId Spieler-ID.
     * @return Container für Zielfelder.
     */
    private MadnFieldContainerV getHome(MadnPlayerId playerId) {

        return switch (playerId) {
            case BLUE -> board.getHomeContainerBlue();
            case YELLOW -> board.getHomeContainerYellow();
            case GREEN -> board.getHomeContainerGreen();
            case RED -> board.getHomeContainerRed();
            case NONE -> null;
        };
    }


    // == Getter / Setter ==============================================================================================

    public MadnPlayerId getPlayerId() {
        return playerId;
    }

    public MadnFieldContainerV getBase() {
        return base;
    }

    public MadnFieldContainerV getHome() {
        return home;
    }

    public MadnFieldContainerV getWaypoints() {
        return waypoints;
    }

    public MadnFigureV[] getFigures() {
        return figures;
    }

    public MadnBoardV getBoard() {
        return board;
    }

}
