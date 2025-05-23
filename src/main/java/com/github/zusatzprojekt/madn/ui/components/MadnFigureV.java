package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.logic.components.MadnFigurePosition;
import com.github.zusatzprojekt.madn.ui.UIManager;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
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

public class MadnFigureV extends Group {
    private final DoubleProperty radius = new SimpleDoubleProperty(100.0);
    private final DoubleProperty strokeWidth = new SimpleDoubleProperty(0.0);
    private final BooleanProperty highlight = new SimpleBooleanProperty(false);
    private final ObjectProperty<Paint> fillColor = new SimpleObjectProperty<>(Color.DODGERBLUE);
    private final ObjectProperty<Paint> ringColor = new SimpleObjectProperty<>(Color.ORANGE);
    private final ObjectProperty<Paint> strokeColor = new SimpleObjectProperty<>(Color.BLACK);
    private final ObjectProperty<Duration> animationDuration = new SimpleObjectProperty<>(Duration.seconds(1.5));
    private final ScaleTransition[] transitions;
    private final Circle[] rings;

    //TODO: Check if this works
    private final ObjectProperty<EventHandler<? super MouseEvent>> mouseEnterEvent = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<? super MouseEvent>> mouseExitEvent = new SimpleObjectProperty<>();
    private final ObjectProperty<MadnFigurePosition> figurePosition = new SimpleObjectProperty<>();
    private MadnPlayerV player;

    @FXML
    private Group animationGroup;
    @FXML
    private Circle figure;


    // == Constructor ==================================================================================================

    @SuppressWarnings("SuspiciousToArrayCall")
    public MadnFigureV() {
        // Load fxml file with ui structure
        UIManager.loadComponentFxml("ui/components/madn-figure-v.fxml", this, this);

        rings = animationGroup.getChildren().toArray(Circle[]::new);
        transitions = new ScaleTransition[] {
                createTransition(rings[0], animationDuration, 2, 1.0),
                createTransition(rings[1], animationDuration, 2, 1.0, true)
        };

        createBindings();
    }


    // == Initialization ===============================================================================================

    private void createBindings() {

        // Setup figure
        figure.layoutXProperty().bind(radius);
        figure.layoutYProperty().bind(radius);
        figure.radiusProperty().bind(radius);
        figure.fillProperty().bind(fillColor);
        figure.strokeProperty().bind(strokeColor);
        figure.strokeWidthProperty().bind(strokeWidth);
//        figure.disableProperty().bind(highlight.not()); // TODO: commented out for test purposes
        figure.onMouseEnteredProperty().bind(mouseEnterEvent);
        figure.onMouseExitedProperty().bind(mouseExitEvent);

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

        // Setup viewport
        setClip();

        // Listener for radii change
        radius.addListener((observableValue, oldValue, value) -> {
            // change viewport
            setClip();
        });

        // Listener for animation / start and stop animation
        highlight.addListener((observableValue, oldValue, value) -> {
            if (value) {
                for (ScaleTransition t : transitions) {
                    t.play();
                }

                this.setViewOrder(1.0);
            } else {
                for (ScaleTransition t : transitions) {
                    t.stop();
                }

                this.setViewOrder(0.0);
            }
        });

        //TODO: Check if this works
        figurePosition.addListener((observableValue, oldPosition, position) -> {
//            moveFigure(oldPosition, position, 0);
            setFigurePosition(position);
        });

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
        MadnFieldContainerV base = player.getBase();
        MadnFieldContainerV home = player.getHome();
        MadnFieldContainerV waypoints = player.getWaypoints();

        switch (position.getFigurePlacement()) {
            case BASE:
                setLayoutX(base.getLayoutX() + base.getFields()[pos].getCenterX() - radius.getValue());
                setLayoutY(base.getLayoutY() + base.getFields()[pos].getCenterY() - radius.getValue());
                break;
            case HOME:
                setLayoutX(home.getLayoutX() + home.getFields()[pos].getCenterX() - radius.getValue());
                setLayoutY(home.getLayoutY() + home.getFields()[pos].getCenterY() - radius.getValue());
                break;
            case WAYPOINTS:
                setLayoutX(waypoints.getFields()[pos].getCenterX() - radius.getValue());
                setLayoutY(waypoints.getFields()[pos].getCenterY() - radius.getValue());
                break;
        }
    }

    private void moveFigure(MadnFigurePosition oldPostion, MadnFigurePosition newPosition, double millis) {
        // TODO Implement Animations
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

    //TODO: Check if this works
    public MadnFigurePosition getFigurePosition() {
        return figurePosition.getValue();
    }

    public void setPlayer(MadnPlayerV player) {
        this.player = player;
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

    //TODO: Check if this works
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