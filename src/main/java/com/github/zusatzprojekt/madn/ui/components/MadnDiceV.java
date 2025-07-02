package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.ui.AppManager;
import com.github.zusatzprojekt.madn.ui.components.dice.MadnDice3dV;
import com.github.zusatzprojekt.madn.ui.components.dice.MadnDiceBaseV;
import com.github.zusatzprojekt.madn.ui.components.dice.MadnRollBtnV;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


public class MadnDiceV extends Group {
    private final Duration ANIMATION_DURATION = Duration.millis(750);
    private final MadnDiceBaseV dice;

    public MadnDiceV() {
        String no3dArg = AppManager.getArguments().getOrDefault("no3d", "false");

        boolean use3D = Platform.isSupported(ConditionalFeature.SCENE3D) && no3dArg.equals("false");

        if (use3D) {
            dice = new MadnDice3dV(ANIMATION_DURATION);
        } else {
            dice = new MadnRollBtnV(ANIMATION_DURATION);
        }

        this.getChildren().add(dice);
    }

    public void startAnimation(int roll) {
        dice.startAnimation(roll);
    }

    public void setOnDiceClicked(EventHandler<MouseEvent> mouseEventEventHandler) {
        dice.onDiceClickedProperty().setValue(mouseEventEventHandler);
    }

    public void setOnFinished(EventHandler<ActionEvent> actionEventEventHandler) {
        dice.onFinishedProperty().setValue(actionEventEventHandler);
    }

    public BooleanProperty enabledProperty() {
        return dice.enabledProperty();
    }

}
