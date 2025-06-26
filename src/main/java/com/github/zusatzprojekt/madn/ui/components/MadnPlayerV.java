package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.logic.MadnFigureL;
import com.github.zusatzprojekt.madn.logic.MadnPlayerL;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;

public class MadnPlayerV extends Group {
    private final IntegerProperty lastRoll = new SimpleIntegerProperty(0);
    private final MadnFieldContainerV base, home, waypoints;
    private final MadnFigureV[] figures;
    private final MadnPlayerId playerId;
    private final MadnBoardV board;


    // == Constructor ==================================================================================================

    public MadnPlayerV(MadnPlayerL playerL, MadnBoardV board) {
        this.board = board;

        playerId = playerL.getPlayerID();

        base = getBase(playerId);

        home = getHome(playerId);

        waypoints = board.getWaypointContainer();

        figures = initFigures(playerL, board);

        board.getChildren().addAll(figures);
//        getChildren().addAll(figures);
        initBindings(playerL);
    }

    private MadnFigureV[] initFigures(MadnPlayerL playerL, MadnBoardV board) {
        MadnFigureL[] figuresL = playerL.getFigures();
        MadnFigureV[] figuresV = new MadnFigureV[figuresL.length];

        for (int i = 0; i < figuresV.length; i++) {
            figuresV[i] = new MadnFigureV(this, figuresL[i], 20.0);
            figuresV[i].setStrokeWidth(1.0);
            figuresV[i].mouseEnterEventProperty().bind(board.activateHighlightEventProperty());
            figuresV[i].mouseExitEventProperty().bind(board.deactivateHighlightEventProperty());
        }

        return figuresV;
    }


    // == Bindings =====================================================================================================

    private void initBindings(MadnPlayerL playerL) {
        lastRoll.bind(playerL.lastRollObservable());
    }


    // == Helper methods ===============================================================================================

    private MadnFieldContainerV getBase(MadnPlayerId playerId) {

        return switch (playerId) {
            case BLUE -> board.getBaseContainerBlue();
            case YELLOW -> board.getBaseContainerYellow();
            case GREEN -> board.getBaseContainerGreen();
            case RED -> board.getBaseContainerRed();
            case NONE -> null;
        };
    }

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
