package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.ui.UIManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class MadnDiceV extends Pane {
    private final int ANIMATION_DURATION_MILLIS = 750;
    private final Rotate rx = new Rotate(0.0, Rotate.X_AXIS);
    private final Rotate ry = new Rotate(0.0, Rotate.Y_AXIS);
    private ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty;
    private ObjectProperty<EventHandler<MouseEvent>> onDiceClickedProperty;

    @FXML
    private SubScene subScene;
    @FXML
    private Group diceContainer, dotContainer;
    @FXML
    private Rectangle overlayRect;

    public MadnDiceV() {
        UIManager.loadComponentFxml("ui/components/madn-dice-v.fxml", this, this);

        PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-2000);

        subScene.setCamera(camera);
        diceContainer.getTransforms().addAll(rx, ry);
    }

    public void startAnimation(int roll) {
        diceContainer.setDisable(true);

        Timeline tl = new Timeline(setupKeyframes(roll));
        tl.setOnFinished(this::onAnimationFinished);
        tl.play();
    }

    private KeyFrame setupKeyframes(int roll) {

        return new KeyFrame(Duration.millis(ANIMATION_DURATION_MILLIS), switch (roll) {
            case 6 -> new KeyValue[]{
                    createKeyValue(rx.angleProperty(), 180.0),
                    createKeyValue(ry.angleProperty(), 0.0)
            };
            case 5 -> new KeyValue[]{
                    createKeyValue(rx.angleProperty(), 90.0),
                    createKeyValue(ry.angleProperty(), 0.0)
            };
            case 4 -> new KeyValue[]{
                    createKeyValue(rx.angleProperty(), 0.0),
                    createKeyValue(ry.angleProperty(), 270.0)
            };
            case 3 -> new KeyValue[]{
                    createKeyValue(rx.angleProperty(), 0.0),
                    createKeyValue(ry.angleProperty(), 90.0)
            };
            case 2 -> new KeyValue[]{
                    createKeyValue(rx.angleProperty(), 270.0),
                    createKeyValue(ry.angleProperty(), 0.0)
            };
            default -> new KeyValue[]{
                    createKeyValue(rx.angleProperty(), 0.0),
                    createKeyValue(ry.angleProperty(), 0.0)
            };
        });
    }

    private KeyValue createKeyValue(DoubleProperty angleProperty, double endValue) {
        double diff = (angleProperty.getValue() - endValue) % 360;
        double newValue = endValue;

        if (diff >= -90.0 && diff < 0.0) {
            newValue -= 360;
        } else if (diff <= 90 && diff >= 0.0) {
            newValue += 360;
        }

        return new KeyValue(angleProperty, newValue);
    }

    private void onAnimationFinished(ActionEvent actionEvent) {
        rx.setAngle((rx.getAngle() + 360) % 360);
        ry.setAngle((ry.getAngle() + 360) % 360);

        if (onFinishedProperty != null) {
            onFinishedProperty.getValue().handle(actionEvent);
        }
    }

    public void setOnFinished(EventHandler<ActionEvent> actionEventEventHandler) {
        onFinishedProperty.setValue(actionEventEventHandler);
    }

    public void setOnDiceClicked(EventHandler<MouseEvent> mouseEventEventHandler) {
        onDiceClickedProperty.setValue(mouseEventEventHandler);
    }

    @FXML
    private void onDiceClicked(MouseEvent mouseEvent) {
        if (onDiceClickedProperty != null) {
            onDiceClickedProperty.getValue().handle(mouseEvent);
        }
    }

}
