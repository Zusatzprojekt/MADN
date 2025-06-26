package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MadnDiceV extends Pane {
    private final Duration ANIMATION_DURATION = Duration.millis(750);
    private final Rotate rx = new Rotate(0.0, Rotate.X_AXIS);
    private final Rotate ry = new Rotate(0.0, Rotate.Y_AXIS);
    private final ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<MouseEvent>> onDiceClickedProperty = new SimpleObjectProperty<>();
    private final boolean use3D;

    @FXML
    private SubScene subScene;
    @FXML
    private Group diceContainer, dotContainer;
    @FXML
    private Rectangle overlayRect;
    @FXML
    private Label altText;
    @FXML
    private Button altBtn;

    public MadnDiceV() {
        String no3dArg = AppManager.getArguments().getOrDefault("no3d", "false");

        use3D = Platform.isSupported(ConditionalFeature.SCENE3D) && no3dArg.equals("false");

        if (use3D) {
            AppManager.loadComponentFxml("ui/components/madn-dice-v.fxml", this, this);

            PerspectiveCamera camera = new PerspectiveCamera(true);

            camera.setNearClip(1);
            camera.setFarClip(10000);
            camera.translateZProperty().set(-2000);

            subScene.setCamera(camera);
            diceContainer.getTransforms().addAll(rx, ry);
        } else {
            AppManager.loadComponentFxml("ui/components/madn-dice-alt-v.fxml", this, this);
        }
    }

    public void startAnimation(int roll) {
        Timeline tl;

        if (use3D) {
            tl = new Timeline(setupKeyframes(roll));
        } else {
            tl = new Timeline(setupKeyframes2d(roll));
        }

        tl.setOnFinished(this::onAnimationFinished);
        tl.play();
    }

    private KeyFrame setupKeyframes(int roll) {

        return new KeyFrame(ANIMATION_DURATION, switch (roll) {
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

    private KeyFrame[] setupKeyframes2d(int roll) {
        Random random = new Random();
        IntegerProperty dummy = new SimpleIntegerProperty(0);
        int rounds = 15;
        Duration frameTime = ANIMATION_DURATION.divide(rounds);

        List<KeyFrame> keyFrames = new ArrayList<>();

        for (int i = 0; i < rounds - 1; i++) {
            int randNum = random.nextInt(1, 7);
            keyFrames.add(new KeyFrame(frameTime.multiply(i + 1), actionEvent -> altText.setText(String.valueOf(randNum))));
        }

        keyFrames.add(new KeyFrame(ANIMATION_DURATION, actionEvent -> altText.setText(String.valueOf(roll))));

        return keyFrames.toArray(KeyFrame[]::new);
    }

    private void onAnimationFinished(ActionEvent actionEvent) {
        if (use3D) {
            rx.setAngle((rx.getAngle() + 360) % 360);
            ry.setAngle((ry.getAngle() + 360) % 360);
        }

        if (onFinishedProperty.isNotNull().getValue()) {
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
        if (onDiceClickedProperty.isNotNull().getValue()) {
            onDiceClickedProperty.getValue().handle(mouseEvent);
        }
    }

    public void disable() {
        if (use3D) {
            setDisable(true);
        } else {
            altBtn.setDisable(true);
        }
    }

    public void enable() {
        if (use3D) {
            setDisable(false);
        } else {
            altBtn.setDisable(false);
        }
    }

}
