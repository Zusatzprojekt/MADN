package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnGamePhase;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.enums.MadnFieldType;
import com.github.zusatzprojekt.madn.logic.MadnGameL;
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

/**
 * Die Klasse {@code MadnBoardV} repräsentiert das visuelle Spielfeld
 * für das Spiel "Mensch ärgere dich nicht" in der JavaFX-Oberfläche.
 *
 * Sie verwaltet Container für Start-, Weg- und Zielfelder,
 * sowie Highlight-Funktionalitäten zum Anzeigen von möglichen Zügen.
 */
public class MadnBoardV extends AnchorPane {
    private final IntegerProperty currentRoll = new SimpleIntegerProperty(1);
    private final ObjectProperty<Shape> clipOverlay = new SimpleObjectProperty<>(new Rectangle(1000, 1000));
    private final ObjectProperty<EventHandler<? super MouseEvent>> activateHighlightEvent = new SimpleObjectProperty<>(this::setHighlightPath);
    private final ObjectProperty<EventHandler<? super MouseEvent>> deactivateHighlightEvent = new SimpleObjectProperty<>(this::removeHighlightPath);
    private MadnGameL game;

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
    private Pane overlayContainer;


    // == Konstruktor =============================================================================

    /**
     * Konstruktor, lädt das zugehörige FXML-Layout und initialisiert die Verbindungen.
     */
    public MadnBoardV() {
        AppManager.loadComponentFxml("ui/components/madn-board-v.fxml", this, this);

        createBindings();
    }


    // == Methoden =============================================================================

    /**
     * Erstellt interne JavaFX-Bindings für das Overlay.
     */
    private void createBindings() {

        overlayContainer.clipProperty().bind(clipOverlay);
    }

/**
 * Zeichnet den Highlight-Pfad für die übergebene Spielfigur abhängig von ihrer Position.
 *
 * @param mouseEvent Das auslösende Mausereignis
 */
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

    /**
     * Berechnet die Highlight-Koordinaten beim Startfeld.
     *
     * @param waypoints Alle Spielfelder
     * @param playerId Spielerkennung
     * @return Koordinaten für Highlight
     */
    private Double[] calcHighlightStart(MadnFieldV[] waypoints, MadnPlayerId playerId) {

        Predicate<MadnFieldV> filter = field -> field.getFieldType() == MadnFieldType.START && field.getFieldAssignment() == playerId;

        MadnFieldV startField = Arrays.stream(waypoints).filter(filter).findFirst().orElseThrow();

        return new Double[] {
                startField.getCenterAbsoluteX(),
                startField.getCenterAbsoluteY(),
                startField.getCenterAbsoluteX(),
                startField.getCenterAbsoluteY()
        };
    }

    /**
     * Berechnet Highlight-Pfad innerhalb des Heimfelds.
     *
     * @param fields Heimfelder des Spielers
     * @param fieldIndex Aktuelle Position
     * @param curRoll Aktueller Würfelwert
     * @return Highlight-Koordinaten
     */
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

    /**
     * Berechnet Highlight-Pfad über Spielfelder und ggf. Übergang in das Heimfeld.
     *
     * @param waypoints Haupt-Spielfelder
     * @param homeFields Heimfelder des Spielers
     * @param fieldIndex Startfeldindex
     * @param playerId Spielerkennung
     * @param curRoll Aktueller Würfelwert
     * @return Highlight-Koordinaten
     */
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

            } else if (curField.getFieldType() == MadnFieldType.END && curField.getFieldAssignment() == playerId) {
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

    /**
     * Entfernt den Highlight-Pfad vom Overlay.
     *
     * @param mouseEvent Das auslösende Event (wird ignoriert)
     */
    private void removeHighlightPath(MouseEvent mouseEvent) {
        clipOverlay.setValue(null);
    }


    // == Getter / Setter =========================================================================

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

    public ObjectProperty<EventHandler<? super MouseEvent>> activateHighlightEventProperty() {
        return activateHighlightEvent;
    }

    public ObjectProperty<EventHandler<? super MouseEvent>> deactivateHighlightEventProperty() {
        return deactivateHighlightEvent;
    }

    public void setGame(MadnGameL game) {
        if (this.game == null) {
            this.game = game;
            overlayContainer.visibleProperty().bind(game.gamePhaseProperty().isEqualTo(MadnGamePhase.FIGURE_SELECT));
        } else {
            throw new RuntimeException(new IllegalAccessError("Field 'game' of MadnBoardV already set; Can only be set once"));
        }
    }

    public MadnGameL getGame() {
        return game;
    }
}