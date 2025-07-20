package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.ui.components.MadnDiceV;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Random;

/**
 * Logik-Komponente für den Würfel im Spiel "Mensch ärgere Dich nicht".
 * Diese Klasse verwaltet den Zufallsmechanismus und die Aktivierung des Würfels.
 *
 * <p>Sie ist eng mit der visuellen Darstellung {@link MadnDiceV} verknüpft,
 * indem sie dessen Aktivierungszustand über ein {@link BooleanProperty} steuert.</p>
 */
public class MadnDiceL {
    private final BooleanProperty enabledProp = new SimpleBooleanProperty(false);
    private final Random random;

    /**
     * Konstruktor, der die Würfellogik initialisiert und an die visuelle Komponente bindet.
     *
     * @param visualDice die zugehörige UI-Komponente des Würfels
     */
    public MadnDiceL(MadnDiceV visualDice) {
        random = new Random();

        initBindings(visualDice);
    }

    /**
     * Bindet die Aktivierungseigenschaft des Würfels an die der visuellen Komponente.
     *
     * @param visualDice die UI-Komponente, deren Aktivierung gebunden wird
     */
    private void initBindings(MadnDiceV visualDice) {
        visualDice.enabledProperty().bind(enabledProp);
    }

    /**
     * Führt einen Würfelwurf durch und gibt eine Zufallszahl zwischen 1 und 6 zurück.
     *
     * @return eine zufällige ganze Zahl von 1 bis einschließlich 6
     */
    public int roll() {
        return random.nextInt(1, 7);
    }

    /**
     * Gibt an, ob der Würfel aktuell aktiviert ist.
     *
     * @return {@code true}, wenn der Würfel aktiv ist, sonst {@code false}
     */
    public boolean isEnabled() {
        return enabledProp.getValue();
    }

    /**
     * Setzt, ob der Würfel aktiviert werden soll.
     *
     * @param b {@code true}, um den Würfel zu aktivieren; {@code false}, um ihn zu deaktivieren
     */
    public void setEnabled(boolean b) {
        enabledProp.setValue(b);
    }

}
