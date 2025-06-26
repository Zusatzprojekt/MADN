package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.enums.MadnFieldFunction;
import com.github.zusatzprojekt.madn.interfaces.MadnFieldExtended;
import com.github.zusatzprojekt.madn.logic.MadnFigureL;
import com.github.zusatzprojekt.madn.logic.components.MadnFigurePosition;
import com.github.zusatzprojekt.madn.ui.AppManager;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldV;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MadnFigureV extends Group {
    private final Duration ANIMATION_DURATION = Duration.millis(250);
    private final DoubleProperty radius = new SimpleDoubleProperty(100.0);
    private final DoubleProperty strokeWidth = new SimpleDoubleProperty(0.0);
    private final BooleanProperty highlight = new SimpleBooleanProperty(false);
    private final ObjectProperty<Paint> fillColor = new SimpleObjectProperty<>(Color.DODGERBLUE);
    private final ObjectProperty<Paint> ringColor = new SimpleObjectProperty<>(Color.ALICEBLUE);
    private final ObjectProperty<Paint> strokeColor = new SimpleObjectProperty<>(Color.BLACK);
    private final ObjectProperty<Duration> animationDuration = new SimpleObjectProperty<>(Duration.seconds(1.5));
    private final ScaleTransition[] transitions;
    private final Circle[] rings;

    //TODO: Testen
    private final ObjectProperty<EventHandler<? super MouseEvent>> mouseEnterEvent = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<? super MouseEvent>> mouseExitEvent = new SimpleObjectProperty<>();
    private final ObjectProperty<MadnFigurePosition> figurePosition = new SimpleObjectProperty<>();
    private final MadnPlayerV player;

    @FXML
    private Group animationGroup;
    @FXML
    private Circle circle;


    // == Constructor ==================================================================================================

    @SuppressWarnings("SuspiciousToArrayCall")
    public MadnFigureV(MadnPlayerV player, MadnFigureL figureL, double radius) {

        // Load fxml file with ui structure
        AppManager.loadComponentFxml("ui/components/madn-figure-v.fxml", this, this);

        this.radius.setValue(radius);

        this.player = player;

        rings = animationGroup.getChildren().toArray(Circle[]::new);

        transitions = initHighlightAnimation();

        initListeners();
        initBindings(figureL);

        setClip(); // TODO: Ist das notwendig?

//        setDisable(true); //TODO: Nach Tests einkommentieren
        setFillDeriveGradient(initFillColor(player.getPlayerId()));
    }

    private Color initFillColor(MadnPlayerId playerId) {

        return switch (playerId) {
            case BLUE -> Color.web("#3387F5");
            case YELLOW -> Color.web("#FFFF00");
            case GREEN -> Color.web("#009A00");
            case RED -> Color.web("#FF3030");
            case NONE -> null;
        };
    }

    private ScaleTransition[] initHighlightAnimation() {

        return new ScaleTransition[] {
                createTransition(rings[0], animationDuration, 2, 1.0),
                createTransition(rings[1], animationDuration, 2, 1.0, true)
        };
    }


    // == Initialization ===============================================================================================

    private void initListeners() {

        // Listener für Radius veränderungen
        radius.addListener((observableValue, oldValue, value) -> {
            // Clip-Layer setzen
            setClip();
        });

        // Listener zum Starten / Stoppen der Highlight-Animation
        highlight.addListener((observableValue, oldValue, value) -> {
            if (value) {
                for (ScaleTransition t : transitions) {
                    t.play();
                }

                this.setViewOrder(-1.0);
            } else {
                for (ScaleTransition t : transitions) {
                    t.stop();
                }
                System.out.println(getViewOrder());

                this.setViewOrder(0.0);
            }
        });

        //TODO: Testen
        figurePosition.addListener((observableValue, oldPosition, position) -> {

            if (getPlayer().getBoard().isInitPhase()) {
                setFigurePosition(position);
            } else {
                moveFigure(oldPosition, position);
            }
        });
    }

    private void initBindings(MadnFigureL figureL) {

        // Setup figure
        circle.layoutXProperty().bind(radius);
        circle.layoutYProperty().bind(radius);
        circle.radiusProperty().bind(radius);
        circle.fillProperty().bind(fillColor);
        circle.strokeProperty().bind(strokeColor);
        circle.strokeWidthProperty().bind(strokeWidth);
//        circle.disableProperty().bind(highlight.not()); // TODO: Nach Tests einkommentieren
        circle.onMouseEnteredProperty().bind(mouseEnterEvent);
        circle.onMouseExitedProperty().bind(mouseExitEvent);


        // Setup rings
        for (Circle ring : rings) {
            ring.layoutXProperty().bind(radius);
            ring.layoutYProperty().bind(radius);
            ring.radiusProperty().bind(radius);
            ring.strokeProperty().bind(ringColor);
            ring.strokeWidthProperty().bind(radius.divide(4.0));
        }

        // Setup animation group
        animationGroup.visibleProperty().bind(highlight);

        // TODO: Testen
        figurePosition.bind(figureL.figurePositionObservable());
        highlight.bind(figureL.canMoveObservable());
    }


    // == Helper methods ===============================================================================================

    private void setClip() {
        double radius = this.radius.getValue();

        // Clip for animationGroup
        Shape clip = Shape.subtract(
                new Circle(radius, radius, radius * 2.0),
                new Circle(radius, radius, radius + (radius / 4.0))
        );

        animationGroup.setClip(clip);
    }

    @SuppressWarnings("SameParameterValue")
    private ScaleTransition createTransition(Node node, ObjectProperty<Duration> duration, double scaleFrom, double scaleTo) {
        return createTransition(node, duration, scaleFrom, scaleTo, false);
    }

    private ScaleTransition createTransition(Node node, ObjectProperty<Duration> duration, double scaleFrom, double scaleTo, boolean delay) {
        ScaleTransition transition = new ScaleTransition();

        transition.durationProperty().bind(duration);

        if (delay) {
            duration.addListener((observableValue, oldValue, value) -> {
                transition.setDelay(value.divide(2.0));
            });
            transition.setDelay(duration.getValue().divide(2.0));
        }

        transition.setNode(node);
        transition.setFromX(scaleFrom);
        transition.setFromY(scaleFrom);
        transition.setToX(scaleTo);
        transition.setToY(scaleTo);
        transition.setCycleCount(ScaleTransition.INDEFINITE);

        transition.statusProperty().addListener((observableValue, oldStatus, status) -> {
            if (status.equals(Animation.Status.STOPPED)) {
                node.setScaleX(0.0);
                node.setScaleY(0.0);
            }
        });

        return transition;
    }


    private void setFigurePosition(MadnFigurePosition position) {
        int pos = position.getFieldIndex();
        double radius = this.radius.getValue();
        MadnFieldContainerV base = player.getBase();
        MadnFieldContainerV home = player.getHome();
        MadnFieldContainerV waypoints = player.getWaypoints();

        switch (position.getFigurePlacement()) {
            case BASE:
                setTranslateX(base.getFields()[pos].getCenterAbsoluteX() - radius);
                setTranslateY(base.getFields()[pos].getCenterAbsoluteY() - radius);
                break;
            case HOME:
                setTranslateX(home.getFields()[pos].getCenterAbsoluteX() - radius);
                setTranslateY(home.getFields()[pos].getCenterAbsoluteY() - radius);
                break;
            case WAYPOINTS:
                setTranslateX(waypoints.getFields()[pos].getCenterAbsoluteX() - radius);
                setTranslateY(waypoints.getFields()[pos].getCenterAbsoluteY() - radius);
                break;
        }

    }

    // TODO: Testen
    private void moveFigure(MadnFigurePosition oldPosition, MadnFigurePosition newPosition) {
        MadnFigurePlacement oldPlacement = oldPosition.getFigurePlacement();
        MadnFigurePlacement newPlacement = newPosition.getFigurePlacement();
        int oldIndex = oldPosition.getFieldIndex();
        int newIndex = newPosition.getFieldIndex();

        SequentialTransition transition = new SequentialTransition();

        if ((oldPlacement == MadnFigurePlacement.BASE && newPlacement == MadnFigurePlacement.WAYPOINTS) || (oldPlacement == MadnFigurePlacement.WAYPOINTS && newPlacement == MadnFigurePlacement.BASE)) {
            transition.getChildren().add(calcAnimationBaseWaypoints(oldPlacement == MadnFigurePlacement.BASE, oldIndex, newIndex));

        } else if ((oldPlacement == MadnFigurePlacement.WAYPOINTS && newPlacement == MadnFigurePlacement.WAYPOINTS) || (oldPlacement == MadnFigurePlacement.BASE && newPlacement == MadnFigurePlacement.BASE)) {
            transition.getChildren().addAll(calcAnimationSameContainer(newPlacement, oldIndex, newIndex));

        } else if (oldPlacement == MadnFigurePlacement.WAYPOINTS && newPlacement == MadnFigurePlacement.HOME) {
            transition.getChildren().addAll(calcAnimationWaypointsHome(oldIndex, newIndex));

        }

        double oldViewOrder = getViewOrder();
        setViewOrder(2.0);

        transition.setOnFinished(actionEvent -> {
            setViewOrder(oldViewOrder);
            System.out.println("Animation Finished");
        });

        transition.play();
    }

    private Animation calcAnimationBaseWaypoints(boolean flip, int oldIndex, int newIndex) {
        MadnFieldV oldField = getPlayer().getBase().getFields()[flip ? oldIndex : newIndex];
        MadnFieldV newField = getPlayer().getWaypoints().getFields()[flip ? newIndex : oldIndex];
        double distance = Math.sqrt(Math.pow(Math.abs(oldField.getCenterAbsoluteX() - newField.getCenterAbsoluteX()), 2) + Math.pow(Math.abs(oldField.getCenterAbsoluteY() - newField.getCenterAbsoluteY()), 2));

        TranslateTransition tt = new TranslateTransition(ANIMATION_DURATION.multiply(distance / 85.0), this);
        tt.setToX(newField.getCenterAbsoluteX() - radius.getValue());
        tt.setToY(newField.getCenterAbsoluteY() - radius.getValue());

        return tt;
    }

    private Animation[] calcAnimationSameContainer(MadnFigurePlacement placement, int oldIndex, int newIndex) {
        MadnFieldV[] fields = placement == MadnFigurePlacement.HOME ? getPlayer().getHome().getFields() : getPlayer().getWaypoints().getFields();
        int fieldCounter = 0;

        List<Animation> animations = new ArrayList<>();

        newIndex %= fields.length;
//TODO: Überarbeiten
        for (int i = oldIndex + 1; i != newIndex; i = (i + 1) % fields.length) {
            MadnFieldV curField = fields[i];
            fieldCounter++;

            if (curField.isCornerField()) {
                TranslateTransition tt = new TranslateTransition(ANIMATION_DURATION.multiply(fieldCounter), this);
                tt.setToX(curField.getCenterAbsoluteX() - radius.getValue());
                tt.setToY(curField.getCenterAbsoluteY() - radius.getValue());
                fieldCounter = 1;

                animations.add(tt);
            }
        }

        fieldCounter++;
        TranslateTransition tt = new TranslateTransition(ANIMATION_DURATION.multiply(fieldCounter), this);
        tt.setToX(fields[newIndex].getCenterAbsoluteX() - radius.getValue());
        tt.setToY(fields[newIndex].getCenterAbsoluteY() - radius.getValue());

        animations.add(tt);

        return animations.toArray(Animation[]::new);
    }

    private Animation[] calcAnimationWaypointsHome(int oldIndex, int newIndex) {
        List<MadnFieldV> waypoints = List.of(getPlayer().getWaypoints().getFields());

        Predicate<MadnFieldV> filter = field -> field.getFieldType() == MadnFieldFunction.END && field.getFieldAssignment() == getPlayer().getPlayerId();

        List<Animation> animations = new ArrayList<>();

        MadnFieldV startField = waypoints.stream().filter(filter).findFirst().orElseThrow();
        MadnFieldV[] homeFields = getPlayer().getHome().getFields();
        int entryIndex = waypoints.indexOf(startField);

        if (oldIndex != entryIndex) {
            animations.addAll(List.of(calcAnimationSameContainer(MadnFigurePlacement.WAYPOINTS, oldIndex, entryIndex)));
        }

        TranslateTransition tt = new TranslateTransition(ANIMATION_DURATION.multiply(newIndex + 1), this);
        tt.setToX(homeFields[newIndex].getCenterAbsoluteX() - radius.getValue());
        tt.setToX(homeFields[newIndex].getCenterAbsoluteY() - radius.getValue());

        animations.add(tt);

        return animations.toArray(Animation[]::new);
    }


    // == Getter / Setter ==============================================================================================

    public double getRadius() {
        return radius.getValue();
    }

    public void setRadius(double value) {
        radius.setValue(value);
    }

    public double getStrokeWidth() {
        return strokeWidth.getValue();
    }

    public void setStrokeWidth(double value) {
        strokeWidth.setValue(value);
    }

    public Paint getFill() {
        return fillColor.getValue();
    }

    public void setFill(Paint fill) {
        fillColor.setValue(fill);
    }

    public Paint getRingColor() {
        return ringColor.getValue();
    }

    public void setRingColor(Paint paint) {
        ringColor.setValue(paint);
    }

    public Paint getStroke() {
        return strokeColor.getValue();
    }

    public void setStroke(Paint stroke) {
        strokeColor.setValue(stroke);
    }

    public boolean isHighlight() {
        return highlight.get();
    }

    public void setHighlight(boolean value) {
        highlight.setValue(value);
    }

    public double getAnimationTimeMs() {
        return animationDuration.getValue().toMillis();
    }

    public void setAnimationTimeMs(double value) {
        animationDuration.setValue(Duration.millis(value));
    }

    //TODO: Testen
    public MadnFigurePosition getFigurePosition() {
        return figurePosition.getValue();
    }

    public MadnPlayerV getPlayer() {
        return player;
    }


    // == Getter / Setter properties ===================================================================================

    public DoubleProperty radiusProperty() {
        return radius;
    }

    public DoubleProperty strokeWidthProperty() {
        return strokeWidth;
    }

    public BooleanProperty highlightProperty() {
        return highlight;
    }

    public ObjectProperty<Paint> fillProperty() {
        return fillColor;
    }

    public ObjectProperty<Paint> ringColorProperty() {
        return ringColor;
    }

    public ObjectProperty<Paint> strokeProperty() {
        return strokeColor;
    }

    public ObjectProperty<Duration> animationDurationProperty() {
        return animationDuration;
    }

    //TODO: Testen
    public ObjectProperty<MadnFigurePosition> figurePositionProperty() {
        return figurePosition;
    }

    public ObjectProperty<EventHandler<? super MouseEvent>> mouseEnterEventProperty() {
        return mouseEnterEvent;
    }

    public ObjectProperty<EventHandler<? super MouseEvent>> mouseExitEventProperty() {
        return mouseExitEvent;
    }


    // == Custom getter / setter =======================================================================================

    public void setFillDeriveGradient(Color color) {
        Stop startColor = new Stop(0, color);
        Stop endColor = new Stop(1, color.deriveColor(0.0, 1.0, 0.4, 1.0));
        RadialGradient gradient = new RadialGradient(0.0, 0.0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, startColor, endColor);

        fillColor.setValue(gradient);
    }

}