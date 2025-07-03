package com.github.zusatzprojekt.madn.ui.components.dice;

import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Repräsentiert die Implementierung des alternativen 2D-Würfels mit Ausgabe eines Texts.
 * Erweitert die {@link MadnDiceBaseV} Klasse, welche die grundlegenden Funktionalitäten bereitstellt.
 */
public class MadnRollBtnV extends MadnDiceBaseV {
    @FXML
    private Label text;
    @FXML
    private Button button;


    // == Konstruktor =================================================================================================

    /**
     * Konstruktor mit Animationsdauer.
     * Lädt das zugehörige FXML und initialisiert Bindings.
     *
     * @param animationDuration Dauer der Würfelanimation
     */
    public MadnRollBtnV(Duration animationDuration) {
        super(animationDuration);

        AppManager.loadComponentFxml("ui/components/dice/madn-roll-btn-v.fxml", this, this);

        initBindings();
    }

    /**
     * Initialisiert die Bindungen, z.B. Button-Enable-State an enabledProp.
     */
    private void initBindings() {
        button.disableProperty().bind(enabledProp.not());
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
     * Setzt eine einfache Animation für die 2D-Würfeldarstellung um.
     *
     * @param roll Gewürfelte Zahl (1–6)
     * @return KeyFrame-Array mit zufälliger Würfelanzeige
     */
    private KeyFrame[] setupKeyframes(int roll) {
        int ROUNDS = 15;
        Random random = new Random();
        Duration frameTime = animationDuration.divide(ROUNDS);

        List<KeyFrame> keyFrames = new ArrayList<>();

        int lastRandom = 0;

        for (int i = 0; i < ROUNDS - 1; i++) {
            int randNum;

            do {
                randNum = random.nextInt(1, 7);
            } while (randNum == lastRandom);

            lastRandom = randNum;
            String randNumString = String.valueOf(randNum);

            keyFrames.add(new KeyFrame(frameTime.multiply(i + 1), actionEvent -> text.setText(randNumString)));
        }

        keyFrames.add(new KeyFrame(animationDuration, actionEvent -> text.setText(String.valueOf(roll))));

        return keyFrames.toArray(KeyFrame[]::new);
    }

    /**
     * Callback bei Animationsende. Führt ggf. registrierten EventHandler aus.
     *
     * @param actionEvent Auslösendes Event
     */
    private void onAnimationFinished(ActionEvent actionEvent) {
        if (onFinishedProp.isNotNull().getValue()) {
            onFinishedProp.getValue().handle(actionEvent);
        }
    }

    /**
     * Interner Callback bei Mausklick auf Würfel-Button (via FXML gebunden).
     *
     * @param mouseEvent Das Mausklick-Event
     */
    @FXML
    private void onBtnClicked(MouseEvent mouseEvent) {
        if (onDiceClickedProp.isNotNull().getValue()) {
            onDiceClickedProp.getValue().handle(mouseEvent);
        }
    }

}
