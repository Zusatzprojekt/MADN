package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.enums.MadnFieldFunction;
import com.github.zusatzprojekt.madn.interfaces.MadnFieldExtended;
import com.github.zusatzprojekt.madn.ui.AppManager;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldV;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MadnBoardV extends AnchorPane {
    private final BooleanProperty showOverlay = new SimpleBooleanProperty(false);
    private final ObjectProperty<Shape> clipOverlay = new SimpleObjectProperty<>(new Rectangle(1000, 1000));
    private final IntegerProperty currentRoll = new SimpleIntegerProperty(1);
    private final BooleanProperty initPhase = new SimpleBooleanProperty(true);

    // TODO: Implementierung fertigstellen
    private final ObjectProperty<EventHandler<? super MouseEvent>> activateHighlightEvent = new SimpleObjectProperty<>(this::setHighlightPath);
    private final ObjectProperty<EventHandler<? super MouseEvent>> deactivateHighlightEvent = new SimpleObjectProperty<>(this::removeHighlightPath);

    @FXML
    private MadnFieldContainerV baseContainerBlue,
            baseContainerYellow,
            baseContainerGreen,
            baseContainerRed,
            waypointContainer,
            homeContainerBlue,
            homeContainerYellow,
            homeContainerGreen,
            homeContainerRed;

    @FXML
    private Pane playerContainer, overlayContainer;

    public MadnBoardV() {
        AppManager.loadComponentFxml("ui/components/madn-board-v.fxml", this, this);

        createBindings();
    }

    private void createBindings() {

//        overlayContainer.visibleProperty().bind(showOverlay);
        overlayContainer.setVisible(true);
        overlayContainer.clipProperty().bind(clipOverlay);
    }


    private void setHighlightPath(MouseEvent mouseEvent) {
        MadnFigureV figure = (MadnFigureV) ((Shape) mouseEvent.getSource()).getParent();

        MadnFieldV[] waypoints = waypointContainer.getFields();
        MadnFieldV[] homeFields = figure.getPlayer().getHome().getFields();
        MadnPlayerId playerId = figure.getPlayer().getPlayerId();
        MadnFigurePlacement figPlacement = figure.getFigurePosition().getFigurePlacement();
        int fieldIndex = figure.getFigurePosition().getFieldIndex();

        Polyline highlightLine = new Polyline();

        highlightLine.setStrokeLineCap(StrokeLineCap.ROUND);
        highlightLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
        highlightLine.setStrokeWidth((waypoints[0].getRadius() + 5) * 2);

        highlightLine.getPoints().addAll(switch (figPlacement) {
            case BASE -> calcHighlightStart(waypoints, playerId);
            case HOME -> calcHighlightHome(homeFields, fieldIndex, currentRoll.getValue());
            case WAYPOINTS -> calcHighlightWaypoints(waypoints, homeFields, fieldIndex, playerId, currentRoll.getValue());
        });

        clipOverlay.setValue(Shape.subtract(new Rectangle(overlayContainer.getWidth(), overlayContainer.getHeight()), highlightLine));
    }


    private Double[] calcHighlightStart(MadnFieldV[] waypoints, MadnPlayerId playerId) {

        Predicate<MadnFieldV> filter = field -> field.getFieldType() == MadnFieldFunction.START && field.getFieldAssignment() == playerId;

        MadnFieldV startField = Arrays.stream(waypoints).filter(filter).findFirst().orElseThrow();

        return new Double[] {
                startField.getCenterAbsoluteX(),
                startField.getCenterAbsoluteY(),
                startField.getCenterAbsoluteX(),
                startField.getCenterAbsoluteY()
        };
    }

    private Double[] calcHighlightHome(MadnFieldV[] fields, int fieldIndex, int curRoll) {
        if (curRoll > 3 - fieldIndex) {
            return new Double[] {fields[0].getCenterAbsoluteX(), fields[0].getCenterAbsoluteY()};
        }

        MadnFieldV startField = fields[fieldIndex];
        MadnFieldV endField = fields[fieldIndex + curRoll];

        return new Double[] {
                startField.getCenterAbsoluteX(),
                startField.getCenterAbsoluteY(),
                endField.getCenterAbsoluteX(),
                endField.getCenterAbsoluteY()
        };
    }

    private Double[] calcHighlightWaypoints(MadnFieldV[] waypoints, MadnFieldV[] homeFields, int fieldIndex, MadnPlayerId playerId, int curRoll) {
        ArrayList<Double> points = new ArrayList<>();
        MadnFieldV curField = waypoints[fieldIndex];
        int curIndex;

        points.addLast(curField.getCenterAbsoluteX());
        points.addLast(curField.getCenterAbsoluteY());

        for (int i = 0; i <= curRoll; i++) {
            curIndex = (fieldIndex + i) % waypoints.length;
            curField = waypoints[curIndex];

            if (curField.isCornerField() || i == curRoll) {
                points.addLast(curField.getCenterAbsoluteX());
                points.addLast(curField.getCenterAbsoluteY());

            } else if (curField.getFieldType() == MadnFieldFunction.END && curField.getFieldAssignment() == playerId) {
                points.addLast(curField.getCenterAbsoluteX());
                points.addLast(curField.getCenterAbsoluteY());

                Double[] homePoints = calcHighlightHome(homeFields, 0, curRoll - i - 1);

                if (homePoints.length < 4) {
                    points.clear();
                }

                points.addAll(List.of(homePoints));
                break;
            }
        }

        return points.toArray(Double[]::new);
    }

    private void removeHighlightPath(MouseEvent mouseEvent) {
        clipOverlay.setValue(null);
    }

    public Pane getPlayerContainer() {
        return playerContainer;
    }

    public MadnFieldContainerV getBaseContainerBlue() {
        return baseContainerBlue;
    }

    public MadnFieldContainerV getBaseContainerYellow() {
        return baseContainerYellow;
    }

    public MadnFieldContainerV getBaseContainerGreen() {
        return baseContainerGreen;
    }

    public MadnFieldContainerV getBaseContainerRed() {
        return baseContainerRed;
    }

    public MadnFieldContainerV getHomeContainerBlue() {
        return homeContainerBlue;
    }

    public MadnFieldContainerV getHomeContainerYellow() {
        return homeContainerYellow;
    }

    public MadnFieldContainerV getHomeContainerGreen() {
        return homeContainerGreen;
    }

    public MadnFieldContainerV getHomeContainerRed() {
        return homeContainerRed;
    }

    public MadnFieldContainerV getWaypointContainer() {
        return waypointContainer;
    }

    public IntegerProperty currentRollProperty() {
        return currentRoll;
    }

    public BooleanProperty showOverlayProperty() {
        return showOverlay;
    }

    public ObjectProperty<EventHandler<? super MouseEvent>> activateHighlightEventProperty() {
        return activateHighlightEvent;
    }

    public ObjectProperty<EventHandler<? super MouseEvent>> deactivateHighlightEventProperty() {
        return deactivateHighlightEvent;
    }

    public BooleanProperty initPhaseProperty() {
        return initPhase;
    }

    public boolean isInitPhase() {
        return initPhase.getValue();
    }

}