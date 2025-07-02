package com.github.zusatzprojekt.madn.ui.components.dice;

import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class MadnDice3dV extends MadnDiceBaseV {
    private final Rotate rxProp = new Rotate(0.0, Rotate.X_AXIS);
    private final Rotate ryProp = new Rotate(0.0, Rotate.Y_AXIS);

    @FXML
    private SubScene subScene;
    @FXML
    private Group diceContainer, dotContainer;


    // == Konstruktor =================================================================================================

    public MadnDice3dV(Duration animationDuration) {
        super(animationDuration);

        AppManager.loadComponentFxml("ui/components/dice/madn-dice-3d-v.fxml", this, this);

        PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-2000);

        subScene.setCamera(camera);
        diceContainer.getTransforms().addAll(rxProp, ryProp);

        initBindings();
    }

    private void initBindings() {
        subScene.disableProperty().bind(enabledProp.not());
    }


    // == Methoden =====================================================================================================

    /**
     * Startet die Würfelanimation für den gegebenen Wurfwert.
     *
     * @param roll Der gewürfelte Wert (1–6)
     */
    @Override
    public void startAnimation(int roll) {
        Timeline tl;

        tl = new Timeline(setupKeyframes(roll));

        tl.setOnFinished(this::onAnimationFinished);
        tl.play();
    }

    /**
     * Erstellt die KeyFrames für die 3D-Würfelanimation entsprechend der gewürfelten Zahl.
     *
     * @param roll Gewürfelte Zahl (1–6)
     * @return Ein KeyFrame mit Drehwerten
     */
    private KeyFrame setupKeyframes(int roll) {

        return new KeyFrame(animationDuration, switch (roll) {
            case 6 -> new KeyValue[]{
                    createKeyValue(rxProp.angleProperty(), 180.0),
                    createKeyValue(ryProp.angleProperty(), 0.0)
            };
            case 5 -> new KeyValue[]{
                    createKeyValue(rxProp.angleProperty(), 90.0),
                    createKeyValue(ryProp.angleProperty(), 0.0)
            };
            case 4 -> new KeyValue[]{
                    createKeyValue(rxProp.angleProperty(), 0.0),
                    createKeyValue(ryProp.angleProperty(), 270.0)
            };
            case 3 -> new KeyValue[]{
                    createKeyValue(rxProp.angleProperty(), 0.0),
                    createKeyValue(ryProp.angleProperty(), 90.0)
            };
            case 2 -> new KeyValue[]{
                    createKeyValue(rxProp.angleProperty(), 270.0),
                    createKeyValue(ryProp.angleProperty(), 0.0)
            };
            default -> new KeyValue[]{
                    createKeyValue(rxProp.angleProperty(), 0.0),
                    createKeyValue(ryProp.angleProperty(), 0.0)
            };
        });
    }

    /**
     * Berechnet eine KeyValue, der eine weiche Würfelrotation ermöglicht.
     *
     * @param angleProperty Die Rotations-Property
     * @param endValue Zielwinkel
     * @return KeyValue für die Animation
     */
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

    /**
     * Callback bei Animationsende. Führt ggf. registrierten EventHandler aus.
     *
     * @param actionEvent Auslösendes Event
     */
    private void onAnimationFinished(ActionEvent actionEvent) {

        rxProp.setAngle((rxProp.getAngle() + 360) % 360);
        ryProp.setAngle((ryProp.getAngle() + 360) % 360);

        if (onFinishedProp.isNotNull().getValue()) {
            onFinishedProp.getValue().handle(actionEvent);
        }
    }

    /**
     * Interner Callback bei Mausklick auf Würfel (via FXML gebunden).
     *
     * @param mouseEvent Das Mausklick-Event
     */
    @FXML
    private void onDiceClicked(MouseEvent mouseEvent) {
        if (onDiceClickedProp.isNotNull().getValue()) {
            onDiceClickedProp.getValue().handle(mouseEvent);
        }
    }

}
