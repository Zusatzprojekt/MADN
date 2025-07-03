package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.enums.MadnFieldType;
import com.github.zusatzprojekt.madn.logic.MadnFigureL;
import com.github.zusatzprojekt.madn.logic.components.MadnFigurePosition;
import com.github.zusatzprojekt.madn.ui.AppManager;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldV;
import javafx.animation.*;
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

/**
 * Visuelle Repräsentation der Spielfiguren im Spiel Mensch ärgere dich nicht.
 * Verantwortlich für Darstellung, Animation, Farbeinstellungen und Interaktionen.
 */
public class MadnFigureV extends Group {
    private final ObjectProperty<EventHandler<? super MouseEvent>> mouseEnterEvent = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<? super MouseEvent>> mouseExitEvent = new SimpleObjectProperty<>();
    private final ObjectProperty<MadnFigurePosition> figurePosition = new SimpleObjectProperty<>();
    private final BooleanProperty highlight = new SimpleBooleanProperty(false);
    private final Duration MOVE_ANIMATION_DURATION = Duration.millis(250);
    private final Duration RING_ANIMATION_DURATION = Duration.seconds(1.5);
    private final ScaleTransition[] transitions;
    private final MadnPlayerV player;
    private final Circle[] rings;

    @FXML
    private Group animationGroup;
    @FXML
    private Circle circle;


    // == Constructor ==================================================================================================

    /**
     * Konstruktor: initialisiert visuelle Elemente, Animationen und Bindungen zur Spiellogik.
     *
     * @param player    Spieler dieser Figur
     * @param figureL   Logische Repräsentation der Figur
     */
    @SuppressWarnings("SuspiciousToArrayCall")
    public MadnFigureV(MadnPlayerV player, MadnFigureL figureL) {

        // Load fxml file with ui structure
        AppManager.loadComponentFxml("ui/components/madn-figure-v.fxml", this, this);

        this.player = player;

        rings = animationGroup.getChildren().toArray(Circle[]::new);

        transitions = initHighlightAnimation();

        initListeners();
        initBindings(figureL);
        initClip();

        setDisable(true);
        initFillColor(player.getPlayerId());
    }

    /**
     * Ermittelt die Füllfarbe basierend auf der Spieler-ID.
     * @param playerId Identifikation des Spielers
     */
    private void initFillColor(MadnPlayerId playerId) {
        Color color = switch (playerId) {
            case BLUE -> Color.web("#3387F5");
            case YELLOW -> Color.web("#FFFF00");
            case GREEN -> Color.web("#009A00");
            case RED -> Color.web("#FF3030");
            case NONE -> Color.web("#000000");
        };

        Stop startColor = new Stop(0, color);
        Stop endColor = new Stop(1, color.deriveColor(0.0, 1.0, 0.4, 1.0));
        RadialGradient gradient = new RadialGradient(0.0, 0.0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, startColor, endColor);

        circle.setFill(gradient);
    }

    /**
     * Erstellt zwei ScaleTransitions für eine pulsierende Highlight-Animation.
     */
    private ScaleTransition[] initHighlightAnimation() {

        return new ScaleTransition[] {
                createTransition(rings[0], RING_ANIMATION_DURATION, 2, 1.0),
                createTransition(rings[1], RING_ANIMATION_DURATION, 2, 1.0, true)
        };
    }


    // == Initialization ===============================================================================================

    /**
     * Initialisiert Listener für Radiusänderung, Highlight-Zustand und Positionsänderung.
     */
    private void initListeners() {

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

                this.setViewOrder(0.0);
            }
        });

        //TODO: Testen
        figurePosition.addListener((observableValue, oldPosition, position) -> {

            if (player.getBoard().isInitPhase()) {
                placeFigure(position);
            } else {
                moveFigure(oldPosition, position);
            }
        });
    }

    /**
     * Bindet visuelle Komponenten an Properties und Observables des Models.
     *
     * @param figureL Logische Repräsentation der Figur
     */
    private void initBindings(MadnFigureL figureL) {

        // Setup figure
        circle.disableProperty().bind(highlight.not());
        circle.onMouseEnteredProperty().bind(mouseEnterEvent);
        circle.onMouseExitedProperty().bind(mouseExitEvent);

        // Setup animation group
        animationGroup.visibleProperty().bind(highlight);

        // TODO: Testen
        figurePosition.bind(figureL.figurePositionObservable());
        highlight.bind(figureL.canMoveObservable());
    }

    /**
     * Setzt einen Clip, damit die Highlight-Ringe sauber dargestellt werden.
     */
    private void initClip() {
        double radius = circle.getRadius();

        // Clip for animationGroup
        Shape clip = Shape.subtract(
                new Circle(radius, radius, radius * 2.0),
                new Circle(radius, radius, radius + (radius / 4.0))
        );

        animationGroup.setClip(clip);
    }


    // == Helper methods ===============================================================================================

    /**
     * Erstellt eine ScaleTransition ohne Verzögerung.
     * @param node
     * @param duration
     * @param scaleFrom
     * @param scaleTo
     */
    @SuppressWarnings("SameParameterValue")
    private ScaleTransition createTransition(Node node, Duration duration, double scaleFrom, double scaleTo) {
        return createTransition(node, duration, scaleFrom, scaleTo, false);
    }


    private ScaleTransition createTransition(Node node, Duration duration, double scaleFrom, double scaleTo, boolean delay) {
        ScaleTransition transition = new ScaleTransition();

        transition.setDuration(duration);

        if (delay) {
            transition.setDelay(duration.divide(2.0));
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

    private void setFigurePosition(MadnFigurePosition position, MadnFieldContainerV fields) {
        int pos = position.getFieldIndex();
        double radius = circle.getRadius();

        setTranslateX(fields.getFields()[pos].getCenterAbsoluteX() - radius);
        setTranslateY(fields.getFields()[pos].getCenterAbsoluteY() - radius);
    }

    private void placeFigure(MadnFigurePosition position) {

        setFigurePosition(position, switch (position.getFigurePlacement()) {
            case BASE -> player.getBase();
            case HOME -> player.getHome();
            case WAYPOINTS -> player.getWaypoints();
        });
    }

    // TODO: Testen
    private void moveFigure(MadnFigurePosition oldPosition, MadnFigurePosition newPosition) {
        MadnFigurePlacement oldPlacement = oldPosition.getFigurePlacement();
        MadnFigurePlacement newPlacement = newPosition.getFigurePlacement();
        int oldIndex = oldPosition.getFieldIndex();
        int newIndex = newPosition.getFieldIndex();

        SequentialTransition transition = new SequentialTransition();

        if ((oldPlacement == MadnFigurePlacement.BASE && newPlacement == MadnFigurePlacement.WAYPOINTS) || (oldPlacement == MadnFigurePlacement.WAYPOINTS && newPlacement == MadnFigurePlacement.BASE)) {
            transition.getChildren().add(calcAnimationBaseToWaypoints(oldPlacement == MadnFigurePlacement.BASE, oldIndex, newIndex));

        } else if ((oldPlacement == MadnFigurePlacement.WAYPOINTS && newPlacement == MadnFigurePlacement.WAYPOINTS) || (oldPlacement == MadnFigurePlacement.BASE && newPlacement == MadnFigurePlacement.BASE)) {
            transition.getChildren().addAll(calcAnimationSameContainer(newPlacement, oldIndex, newIndex));

        } else if (oldPlacement == MadnFigurePlacement.WAYPOINTS && newPlacement == MadnFigurePlacement.HOME) {
            transition.getChildren().addAll(calcAnimationWaypointsToHome(oldIndex, newIndex));
        }

        double oldViewOrder = getViewOrder();
        setViewOrder(-2.0);

        transition.setOnFinished(actionEvent -> {
            setViewOrder(oldViewOrder);
            System.out.println("Animation Finished");
        });

        transition.play();
    }

    private Animation calcAnimationBaseToWaypoints(boolean flip, int oldIndex, int newIndex) {
        double radius = circle.getRadius();
        MadnFieldV oldField = player.getBase().getFields()[flip ? oldIndex : newIndex];
        MadnFieldV newField = player.getWaypoints().getFields()[flip ? newIndex : oldIndex];
        double distance = Math.sqrt(Math.pow(Math.abs(oldField.getCenterAbsoluteX() - newField.getCenterAbsoluteX()), 2) + Math.pow(Math.abs(oldField.getCenterAbsoluteY() - newField.getCenterAbsoluteY()), 2));

        TranslateTransition tt = new TranslateTransition(MOVE_ANIMATION_DURATION.multiply(distance / 85.0), this);
        tt.setToX(newField.getCenterAbsoluteX() - radius);
        tt.setToY(newField.getCenterAbsoluteY() - radius);

        return tt;
    }

    private Animation[] calcAnimationSameContainer(MadnFigurePlacement placement, int oldIndex, int newIndex) {
        double radius = circle.getRadius();
        MadnFieldV[] fields = placement == MadnFigurePlacement.HOME ? player.getHome().getFields() : player.getWaypoints().getFields();

        List<Animation> animations = new ArrayList<>();

        int animDurationMulti = 1;
        int i = oldIndex + 1;

        while (i != newIndex) {

            if (fields[i].isCornerField()) {
                TranslateTransition tt = new TranslateTransition(MOVE_ANIMATION_DURATION.multiply(animDurationMulti), this);
                tt.setToX(fields[i].getCenterAbsoluteX() - radius);
                tt.setToY(fields[i].getCenterAbsoluteY() - radius);
                animations.add(tt);

                animDurationMulti = 1;
            } else {
                animDurationMulti++;
            }

            i = (i + 1) % fields.length;
        }

        TranslateTransition tt = new TranslateTransition(MOVE_ANIMATION_DURATION.multiply(animDurationMulti), this);
        tt.setToX(fields[newIndex].getCenterAbsoluteX() - radius);
        tt.setToY(fields[newIndex].getCenterAbsoluteY() - radius);
        animations.add(tt);

        return animations.toArray(Animation[]::new);
    }

    private Animation[] calcAnimationWaypointsToHome(int oldIndex, int newIndex) {
        double radius = circle.getRadius();
        MadnFieldV[] homeFields = player.getHome().getFields();
        List<MadnFieldV> waypoints = List.of(player.getWaypoints().getFields());
        Predicate<MadnFieldV> filter = field -> field.getFieldType() == MadnFieldType.END && field.getFieldAssignment() == player.getPlayerId();
        MadnFieldV endField = waypoints.stream().filter(filter).findFirst().orElseThrow();
        int entryIndex = waypoints.indexOf(endField);

        List<Animation> animations = new ArrayList<>();

        if (oldIndex != entryIndex) {
            animations.addAll(List.of(calcAnimationSameContainer(MadnFigurePlacement.WAYPOINTS, oldIndex, entryIndex)));
        }

        TranslateTransition tt = new TranslateTransition(MOVE_ANIMATION_DURATION.multiply(newIndex + 1), this);
        tt.setToX(homeFields[newIndex].getCenterAbsoluteX() - radius);
        tt.setToX(homeFields[newIndex].getCenterAbsoluteY() - radius);

        animations.add(tt);

        return animations.toArray(Animation[]::new);
    }


    // == Getter / Setter ==============================================================================================

    public boolean isHighlight() {
        return highlight.get();
    }

    public void setHighlight(boolean value) {
        highlight.setValue(value);
    }

    //TODO: Testen
    public MadnFigurePosition getFigurePosition() {
        return figurePosition.getValue();
    }

    public MadnPlayerV getPlayer() {
        return player;
    }


    // == Getter / Setter properties ===================================================================================

    public BooleanProperty highlightProperty() {
        return highlight;
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

}