package com.github.zusatzprojekt.madn.enums;

/**
 * Repräsentiert die verschiedenen Phasen eines Spielzugs im Spiel „Mensch ärgere dich nicht“.
 * <p>
 * Jede Phase beschreibt einen bestimmten Ablauf innerhalb eines Spielerzugs und wird verwendet,
 * um den aktuellen Zustand des Spiels zu steuern und die Benutzeroberfläche entsprechend anzupassen.
 * </p>
 */
public enum MadnGamePhase {
    INIT,
    START_ROLL,
    FIGURE_SELECT,
    DICE_ROLL,
    MOVE_ANIMATION,
    THROW_TRIGGER
}
