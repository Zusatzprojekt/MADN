package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.ui.UIManager;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class MadnBoardV extends AnchorPane {
    private final BooleanProperty showOverlay = new SimpleBooleanProperty(false);
    private final ObjectProperty<Shape> clipOverlay = new SimpleObjectProperty<>(new Rectangle(1000, 1000));
    private final IntegerProperty currentRoll = new SimpleIntegerProperty(1);

    // TODO: Finish implementation
    private final ObjectProperty<EventHandler<? super MouseEvent>> activateHighlightEvent = new SimpleObjectProperty<>(mouseEvent -> setHighlightPath((MadnFigureV) ((Shape) mouseEvent.getSource()).getParent()));
    private final ObjectProperty<EventHandler<? super MouseEvent>> deactivateHighlightEvent = new SimpleObjectProperty<>(mouseEvent -> removeHighlightPath());

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
        UIManager.loadComponentFxml("ui/components/madn-board-v.fxml", this, this);

        createBindings();
    }

    private void createBindings() {

        overlayContainer.visibleProperty().bind(showOverlay);
//        overlayContainer.setVisible(true);
        overlayContainer.clipProperty().bind(clipOverlay);

    }




    private void setHighlightPath(MadnFigureV figure) {
//        MadnFigurePlacement figurePlacement = figure.getFigurePosition().getFigurePlacement();
//        int fieldIndex = figure.getFigurePosition().getFieldIndex();
//        int roll = currentRoll.getValue();
//
//        if (figurePlacement == MadnFigurePlacement.BASE) {
////            MadnFieldV[] fields = Arrays.stream(waypointContainer.getFields()).filter(madnFieldV -> madnFieldV.getFieldType() == MadnFieldType.START).toArray(MadnFieldV[]::new);
//
//            MadnFieldV startField = Arrays.stream(waypointContainer.getFields()).filter(madnFieldV -> madnFieldV.getFieldType() == MadnFieldType.START && madnFieldV.getSpecialFor() == figure.getPlayer().getPlayerID()).toList().getFirst();
//
//            System.out.println(startField);
//
//            Circle circle = new Circle(waypointContainer.getFields()[0].getRadius() + 5);
//            circle.setLayoutX(startField.getCenterX() + circle.getRadius());
//            circle.setLayoutY(startField.getCenterY() + circle.getRadius());
//
//            clipOverlay.setValue(Shape.subtract(new Rectangle(1000.0, 1000.0), circle));
//
//        } else if (figurePlacement == MadnFigurePlacement.HOME) {
//            MadnFieldV[] fields = figure.getHome().getFields();
//            Polyline line = new Polyline();
//            line.getPoints().addAll(fields[fieldIndex].getCenterX(), fields[fieldIndex].getCenterY());
//            line.getPoints().addAll(fields[fieldIndex + roll].getCenterX(), fields[fieldIndex + roll].getCenterY());
//            line.setStrokeWidth((fields[0].getRadius() + 5) * 2);
//            line.setStrokeLineCap(StrokeLineCap.ROUND);
//
//            clipOverlay.setValue(Shape.subtract(new Rectangle(1000.0, 1000.0), line));
//
//        } else if (figurePlacement == MadnFigurePlacement.WAYPOINTS) {
//            MadnFieldV[] fields = waypointContainer.getFields();
//            Polyline line = new Polyline();
//            boolean inHome = false;
//
//            line.getPoints().addAll(fields[fieldIndex].getCenterX(), fields[fieldIndex].getCenterY());
//
//            for (int i = 1; i <= currentRoll.getValue(); i++) {
//                int n = (fieldIndex + i) % fields.length;
//
//                if (fields[n].getFieldType() == MadnFieldType.CORNER) {
//                    line.getPoints().addAll(fields[n].getCenterX(), fields[n].getCenterY());
//                } else if (fields[n].getFieldType() == MadnFieldType.END && fields[n].getSpecialFor() == figure.getPlayer().getPlayerID()) {
//                    inHome = true;
//                    line.getPoints().addAll(fields[n].getCenterX(), fields[n].getCenterY());
//                } else if (i == currentRoll.getValue()) {
//                    line.getPoints().addAll(fields[n].getCenterX(), fields[n].getCenterY());
//                }
//            }
//
//            line.setStrokeWidth((fields[0].getRadius() + 5) * 2);
//            line.setStrokeLineCap(StrokeLineCap.ROUND);
//            line.setStrokeLineJoin(StrokeLineJoin.ROUND);
//
//            clipOverlay.setValue(Shape.subtract(new Rectangle(1000, 1000), line));
//        }
//
//        System.out.println("clipchange");
    }

    private void removeHighlightPath() {
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

    public ObjectProperty<EventHandler<? super MouseEvent>> activateHighlightEventProperty() {
        return activateHighlightEvent;
    }

    public ObjectProperty<EventHandler<? super MouseEvent>> deactivateHighlightEventProperty() {
        return deactivateHighlightEvent;
    }

}