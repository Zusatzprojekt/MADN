package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.logic.MadnFigureL;
import com.github.zusatzprojekt.madn.logic.MadnPlayerL;
import com.github.zusatzprojekt.madn.ui.UIManager;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class MadnPlayerV extends Group {
    private final MadnPlayerId playerID;
    private final MadnFigureV[] figures;
    private final IntegerProperty lastRoll = new SimpleIntegerProperty(0);
    private final MadnFieldContainerV base, home, waypoints;


    // == Constructor ==================================================================================================

    @SuppressWarnings("SuspiciousToArrayCall")
    public MadnPlayerV(MadnPlayerL playerL, MadnBoardV gameBoard) {

        UIManager.loadComponentFxml("ui/components/madn-player-v.fxml", this, this);

        playerID = playerL.getPlayerID();
        figures = getChildren().toArray(MadnFigureV[]::new);

        base = switch (playerID) {
            case BLUE -> gameBoard.getBaseContainerBlue();
            case YELLOW -> gameBoard.getBaseContainerYellow();
            case GREEN -> gameBoard.getBaseContainerGreen();
            case RED -> gameBoard.getBaseContainerRed();
        };

        home = switch (playerID) {
            case BLUE -> gameBoard.getHomeContainerBlue();
            case YELLOW -> gameBoard.getHomeContainerYellow();
            case GREEN -> gameBoard.getHomeContainerGreen();
            case RED -> gameBoard.getHomeContainerRed();
        };

        waypoints = gameBoard.getWaypointContainer();

        createBindings(playerL);
        setupFigures(playerL.getFigures());
    }


    // == Bindings =====================================================================================================

    private void createBindings(MadnPlayerL playerL) {
        lastRoll.bind(playerL.lastRollObservable());
    }


    // == Helper methods ===============================================================================================

    private void setupFigures(MadnFigureL[] figuresL) {
        Color figColor = switch (playerID) {
            case BLUE -> Color.web("#3387F5");
            case YELLOW -> Color.web("#FFFF00");
            case GREEN -> Color.web("#009A00");
            case RED -> Color.web("#FF3030");
        };

        if (figuresL.length != figures.length) {
            throw new IllegalStateException("figure count of logic and visual doesn't match");
        }

        for (int i = 0; i < figures.length; i++) {
            figures[i].setPlayer(this);
            figures[i].setFillDeriveGradient(figColor);
            figures[i].figurePositionProperty().bind(figuresL[i].figurePositionObservable());
            figures[i].highlightProperty().bind(figuresL[i].canMoveObservable());
        }

    }


    // == Getter / Setter ==============================================================================================

    public MadnPlayerId getPlayerID() {
        return playerID;
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

}
